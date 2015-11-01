package hyperdoc.journal

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.core.HyperdocTestObjects
import hyperdoc.journal.HyperdocJournalActor.{GetCurrentObject, NonexistentObject, RemoveObject, RemovedObject}
import hyperdoc.journal.NodeJournalActor.CreateNode
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/** Tests for [[hyperdoc.journal.NodeJournalActor]] */
class NodeJournalActorSpec extends TestKit(HyperdocJournalActorUtil.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  "A node journal" must {
    "not find a nonexistent node" in {
      val ref = HyperdocTestObjects.randomContentNode.ref
      val aggregate = HyperdocJournalManager.getJournalActor(ref)

      aggregate ! GetCurrentObject
      expectMsg(NonexistentObject(ref))
    }
    "create a valid node" in {
      val node = HyperdocTestObjects.randomContentNode
      val aggregate = HyperdocJournalManager.getJournalActor(node.ref)
      aggregate ! CreateNode(node)
      expectMsg(node)
    }
    "remove an existing node" in {
      val node = HyperdocTestObjects.randomContentNode
      val aggregate = HyperdocJournalManager.getJournalActor(node.ref)
      aggregate ! CreateNode(node)
      expectMsg(node)
      aggregate ! RemoveObject
      expectMsg(RemovedObject(node.ref))
    }
  }
}