package hyperdoc.journal

import akka.actor.ActorSystem

object HyperdocJournalActorUtil {
  implicit val system = ActorSystem("hyperdoc_test")
}
