package hyperdoc.journal

import akka.actor.Props
import hyperdoc.core.{Authority, HyperdocRef}
import hyperdoc.journal.HyperdocJournalActor.{HyperdocCommand, HyperdocEvent}

/** Authority command */
sealed trait AuthorityCommand extends HyperdocCommand

/** Authority event */
sealed trait AuthorityEvent extends HyperdocEvent

/** Companion object for the authority journal.
  *
  * @author Ezequiel Foncubierta
  */
object AuthorityJournalActor {

  /** Command for creating an authority.
    *
    * @param authority Authority
    * @tparam A Authority type
    */
  case class CreateAuthority[A <: Authority](authority: A) extends AuthorityCommand

  /** Event produced when an authority has been created.
    *
    * @param authority Created authority
    * @tparam A Authority type
    */
  case class AuthorityCreated[A <: Authority](authority: A) extends AuthorityEvent

  /** Event produced when an authority has been removed. */
  case object AuthorityRemoved extends AuthorityEvent

  /** Authority journal actor properties */
  def props(ref: HyperdocRef): Props = Props(new AuthorityJournalActor(ref))
}

/** Journal actor for managing authorities.
  *
  * @param href Authority reference
  * @author Ezequiel Foncubierta
  */
class AuthorityJournalActor(href: HyperdocRef) extends HyperdocJournalActor {

  import AuthorityJournalActor._
  import HyperdocJournalActor._

  override def ref: HyperdocRef = href

  override def update(event: HyperdocEvent): Unit = event match {
    case AuthorityCreated(authority) =>
      context.become(enabled)
      currentObject = authority
    case AuthorityRemoved =>
      context.become(removed)
      currentObject = RemovedObject(ref)
  }

  def initial: Receive = {
    case CreateAuthority(authority) =>
      persist(AuthorityCreated(authority))(afterEventPersisted)
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
  }

  def enabled: Receive = {
    case RemoveObject =>
      persist(AuthorityRemoved)(afterEventPersisted)
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
