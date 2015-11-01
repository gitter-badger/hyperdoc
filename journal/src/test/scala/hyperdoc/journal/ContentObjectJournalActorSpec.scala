package hyperdoc.journal

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.core.HyperdocTestObjects
import hyperdoc.journal.ContentObjectJournalActor.CreateContentObject
import hyperdoc.journal.HyperdocJournalActor.{GetCurrentObject, NonexistentObject, RemoveObject, RemovedObject}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/** Tests for [[hyperdoc.journal.ContentObjectJournalActor]] */
class ContentObjectJournalActorSpec extends TestKit(HyperdocJournalActorUtil.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

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
}