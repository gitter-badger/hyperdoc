package hyperdoc.journal

import akka.actor.Props
import akka.persistence.SnapshotMetadata
import hyperdoc.core.{ContentObject, HyperdocObject, HyperdocRef}
import hyperdoc.journal.HyperdocJournalActor.{HyperdocCommand, HyperdocEvent}

/** Content object command */
sealed trait ContentObjectCommand extends HyperdocCommand

/** Content object event */
sealed trait ContentObjectEvent extends HyperdocEvent

/** Companion object for the content object journal.
  *
  * @author Ezequiel Foncubierta
  */
object ContentObjectJournalActor {

  /** Command for creating a content object.
    *
    * @param obj Content object
    * @tparam A Content object type
    */
  case class CreateContentObject[A <: ContentObject](obj: A) extends ContentObjectCommand

  /** Event produced when a content object has been created.
    *
    * @param obj Content object
    * @tparam A Content object type
    */
  case class ContentObjectCreated[A <: ContentObject](obj: A) extends ContentObjectEvent

  /** Event produced when a content object has been removed. */
  case object ContentObjectRemoved extends ContentObjectEvent

  /** Content object journal actor properties */
  def props(ref: HyperdocRef): Props = Props(new ContentObjectJournalActor(ref))
}

/** Journal actor for managing content objects.
  *
  * @param href Content object reference
  * @author Ezequiel Foncubierta
  */
class ContentObjectJournalActor(href: HyperdocRef) extends HyperdocJournalActor {

  import ContentObjectJournalActor._
  import HyperdocJournalActor._

  override def ref: HyperdocRef = href

  override protected def restoreFromSnapshot(metadata: SnapshotMetadata, obj: HyperdocObject): Unit = {
    currentObject = obj
    obj match {
      case NonexistentObject(_) => context become initial
      case RemovedObject(_) => context become removed
      case _: HyperdocObject => context become enabled
    }
  }

  override def update(event: HyperdocEvent): Unit = event match {
    case ContentObjectCreated(obj) =>
      context.become(enabled)
      currentObject = obj
    case ContentObjectRemoved =>
      context.become(removed)
      currentObject = RemovedObject(ref)
  }

  def initial: Receive = {
    case CreateContentObject(obj) =>
      persist(ContentObjectCreated(obj))(afterEventPersisted)
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
  }

  def enabled: Receive = {
    case RemoveObject =>
      persist(ContentObjectRemoved)(afterEventPersisted)
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
    // FIXME sending CreateAuthority when in 'enabled' state will fail
  }

  def removed: Receive = {
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
  }
}