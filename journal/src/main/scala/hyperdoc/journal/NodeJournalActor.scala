package hyperdoc.journal

import akka.actor.Props
import hyperdoc.core.{HyperdocRef, Node}
import hyperdoc.journal.HyperdocJournalActor.{HyperdocCommand, HyperdocEvent}

/** Node command */
sealed trait NodeCommand extends HyperdocCommand

/** Node event */
sealed trait NodeEvent extends HyperdocEvent

/** Companion object for the node journal.
  *
  * @author Ezequiel Foncubierta
  */
object NodeJournalActor {

  /** Command for creating a node.
    *
    * @param node Node
    * @tparam A Node type
    */
  case class CreateNode[A <: Node](node: A) extends NodeCommand

  /** Event produced when a node has been created.
    *
    * @param node Node
    * @tparam A Node type
    */
  case class NodeCreated[A <: Node](node: A) extends NodeEvent

  /** Event produced when a node has been removed. */
  case object NodeRemoved extends NodeEvent

  /** Node journal actor properties */
  def props(ref: HyperdocRef): Props = Props(new NodeJournalActor(ref))
}

/** Journal actor for managing nodes.
  *
  * @param href Node reference
  * @author Ezequiel Foncubierta
  */
class NodeJournalActor(href: HyperdocRef) extends HyperdocJournalActor {

  import HyperdocJournalActor._
  import NodeJournalActor._

  override def ref: HyperdocRef = href

  override def update(event: HyperdocEvent): Unit = event match {
    case NodeCreated(node) =>
      context.become(enabled)
      currentObject = node
    case NodeRemoved =>
      context.become(removed)
      currentObject = RemovedObject(ref)
  }

  def initial: Receive = {
    case CreateNode(node) =>
      persist(NodeCreated(node))(afterEventPersisted)
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
  }

  def enabled: Receive = {
    case RemoveObject =>
      persist(NodeRemoved)(afterEventPersisted)
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
    // FIXME sending CreateNode when in 'enabled' state will fail
  }

  def removed: Receive = {
    case GetCurrentObject =>
      respond()
    case KillObjectJournal =>
      context.stop(self)
  }
}