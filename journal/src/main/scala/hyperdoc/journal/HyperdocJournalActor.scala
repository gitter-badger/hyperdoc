package hyperdoc.journal

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotMetadata, SnapshotOffer}
import hyperdoc.core.{HyperdocObject, HyperdocRef}

/** Error message */
case class Error(message: String)

/** Acknowledge */
case class Acknowledge(ref: HyperdocRef)

/** Hyperdoc journal.
  *
  * @author Ezequiel Foncubierta
  */
object HyperdocJournalActor {

  /** Nonexistent hyperdoc object.
    *
    * @param ref Hyperdoc reference
    */
  case class NonexistentObject(ref: HyperdocRef) extends HyperdocObject

  /** Disabled hyperdoc object.
    *
    * @param ref Hyperdoc reference
    * @param obj Disabled object
    * @tparam A Hyperdoc object type
    */
  case class DisabledObject[A <: HyperdocObject](ref: HyperdocRef, obj: Option[A] = None) extends HyperdocObject

  /** Removed hyperdoc object.
    *
    * @param ref Hyperdoc reference.
    */
  case class RemovedObject(ref: HyperdocRef) extends HyperdocObject

  /** Hyperdoc event */
  trait HyperdocEvent

  /** Hyperdoc command */
  trait HyperdocCommand

  /** Remove hyperdoc object */
  case object RemoveObject extends HyperdocCommand

  /** Get hyperdoc object */
  case object GetCurrentObject extends HyperdocCommand

  /** Kill object journal */
  case object KillObjectJournal extends HyperdocCommand

  val eventsPerSnapshot = 10
}

/** Principal actor trait for Hyperdoc's persistent actors. All hyperdoc objects
  * are persisted in a journal database.
  *
  * @author Ezequiel Foncubierta
  */
trait HyperdocJournalActor extends PersistentActor with ActorLogging {

  import HyperdocJournalActor._

  // number of events since last snapshot
  private var eventsSinceLastSnapshot = 0

  override def persistenceId: String = ref.toString

  /** Current object */
  protected var currentObject: HyperdocObject = NonexistentObject(ref)

  /** Common callback for handling hyperdoc events.
    *
    * @param event Hyperdoc event
    */
  protected def afterEventPersisted(event: HyperdocEvent): Unit = {
    eventsSinceLastSnapshot += 1
    if (eventsSinceLastSnapshot >= eventsPerSnapshot) {
      log.debug("{} events reached, saving snapshot", eventsPerSnapshot)
      saveSnapshot(currentObject)
      eventsSinceLastSnapshot = 0
    }
    updateAndRespond(event)
    publish(event)
  }

  /** Update current object and send response to requester.
    *
    * @param event Hyperdoc event
    */
  private def updateAndRespond(event: HyperdocEvent): Unit = {
    update(event)
    respond()
  }

  /** Publish hyperdoc event to stream.
    *
    * @param event Hyperdoc event
    */
  private def publish(event: HyperdocEvent) = context.system.eventStream.publish(event)

  /** Send current object to requester */
  protected def respond(): Unit = {
    sender() ! currentObject
    context.parent ! Acknowledge(ref)
  }

  /** Restore aggregator object from snapshot.
    *
    * @param metadata Snapshot metadata
    * @param obj Hyperdoc object
    */
  protected def restoreFromSnapshot(metadata: SnapshotMetadata, obj: HyperdocObject) = {
    currentObject = obj
    obj match {
      case NonexistentObject(_) => context become initial
      case RemovedObject(_) => context become removed
      case _: HyperdocObject => context become enabled
    }
  }

  /** @inheritdoc */
  override def receiveRecover: Receive = {
    case evt: HyperdocEvent =>
      eventsSinceLastSnapshot += 1
      update(evt)
    case SnapshotOffer(metadata, obj: HyperdocObject) =>
      restoreFromSnapshot(metadata, obj)
      log.debug("recovering aggregate from snapshot")
  }

  /** @inheritdoc */
  override def receiveCommand: Receive = initial

  /** Hyperdoc object reference
    *
    * @return Hyperdoc object reference
    */
  def ref: HyperdocRef

  /** Update current hyperdoc object from event.
    *
    * @param event Hyperdoc event
    */
  def update(event: HyperdocEvent): Unit

  /** initial state */
  def initial: Receive

  /** enabled state */
  def enabled: Receive

  /** Removed state */
  def removed: Receive
}