package hyperdoc.journal

import akka.actor.{ActorRef, ActorSystem}
import hyperdoc.core.HyperdocRef

object HyperdocJournalManager {
  def getJournalActor(ref: HyperdocRef)(implicit system: ActorSystem): ActorRef = {
    system.actorOf(ref.store.name match {
      case "authority" => AuthorityJournalActor.props(ref)
      case "node" => NodeJournalActor.props(ref)
//      case "model" => ModelJournalActor.props(ref)
      case "object" => ContentObjectJournalActor.props(ref)
    }, generateActorPath(ref))
  }

  private def generateActorPath(ref: HyperdocRef): String = {
    ref.toString.replaceAll("/", "~")
  }
}
