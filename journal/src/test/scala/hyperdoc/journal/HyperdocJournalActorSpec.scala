package hyperdoc.journal

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.core.HyperdocTestObjects
import hyperdoc.journal.AuthorityJournalActor.CreateAuthority
import hyperdoc.journal.ContentObjectJournalActor.CreateContentObject
import hyperdoc.journal.HyperdocJournalActor.{GetCurrentObject, NonexistentObject, RemoveObject, RemovedObject}
import hyperdoc.journal.NodeJournalActor.CreateNode
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/** Tests for:
  * - [[hyperdoc.journal.AuthorityJournalActor]]
  * - [[hyperdoc.journal.ContentObjectJournalActor]]
  * - [[hyperdoc.journal.NodeJournalActor]]
  */
class HyperdocJournalActorSpec extends TestKit(HyperdocJournalActorUtil.randomActorSystem) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  //  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "An authority journal" must {
    "not find a nonexistent authority" in {
      val ref = HyperdocTestObjects.randomGroup.ref
      val aggregate = HyperdocJournalManager.getJournalActor(ref)

      aggregate ! GetCurrentObject
      expectMsg(NonexistentObject(ref))
    }
    "create a valid authority" in {
      val group = HyperdocTestObjects.randomGroup
      val aggregate = HyperdocJournalManager.getJournalActor(group.ref)
      aggregate ! CreateAuthority(group)
      expectMsg(group)
    }
    "remove an existing authority" in {
      val group = HyperdocTestObjects.randomGroup
      val aggregate = HyperdocJournalManager.getJournalActor(group.ref)
      aggregate ! CreateAuthority(group)
      expectMsg(group)
      aggregate ! RemoveObject
      expectMsg(RemovedObject(group.ref))
    }
  }

  "A content object journal" must {
    "not find a nonexistent content object" in {
      val ref = HyperdocTestObjects.randomBinaryContentObject.ref
      val aggregate = HyperdocJournalManager.getJournalActor(ref)

      aggregate ! GetCurrentObject
      expectMsg(NonexistentObject(ref))
    }
    "create a valid content object" in {
      val obj = HyperdocTestObjects.randomBinaryContentObject
      val aggregate = HyperdocJournalManager.getJournalActor(obj.ref)
      aggregate ! CreateContentObject(obj)
      expectMsg(obj)
    }
    "remove an existing content object" in {
      val obj = HyperdocTestObjects.randomBinaryContentObject
      val aggregate = HyperdocJournalManager.getJournalActor(obj.ref)
      aggregate ! CreateContentObject(obj)
      expectMsg(obj)
      aggregate ! RemoveObject
      expectMsg(RemovedObject(obj.ref))
    }
  }

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
