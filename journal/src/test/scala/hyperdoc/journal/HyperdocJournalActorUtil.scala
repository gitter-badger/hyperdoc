package hyperdoc.journal

import akka.actor.ActorSystem

import scala.util.Random

object HyperdocJournalActorUtil {
  def randomActorSystem = ActorSystem(Random.alphanumeric.take(10).mkString(""))
}
