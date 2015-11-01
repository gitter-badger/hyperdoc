package hyperdoc.journal

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.core.HyperdocTestObjects
import hyperdoc.journal.HyperdocJournalActor.{GetCurrentObject, NonexistentObject}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/** Tests for [[hyperdoc.journal.ModelJournalActorSpec]] */
class ModelJournalActorSpec extends TestKit(HyperdocJournalActorUtil.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  "A model journal" must {
    "not find a nonexistent model" in {
      val ref = HyperdocTestObjects.randomGroup.ref
      val aggregate = HyperdocJournalManager.getJournalActor(ref)

      aggregate ! GetCurrentObject
      expectMsg(NonexistentObject(ref))
    }
    //    "create a valid authority" in {
    //      val group = HyperdocTestObjects.randomGroup
    //      val aggregate = HyperdocJournalManager.getJournalActor(group.ref)
    //      aggregate ! CreateModel(group)
    //      expectMsg(group)
    //    }
    //    "remove a valid authority" in {
    //      val group = HyperdocTestObjects.randomGroup
    //      val aggregate = HyperdocJournalManager.getJournalActor(group.ref)
    //      aggregate ! CreateModel(group)
    //      expectMsg(group)
    //      aggregate ! RemoveObject
    //      expectMsg(RemovedObject(group.ref))
    //    }
  }
}
