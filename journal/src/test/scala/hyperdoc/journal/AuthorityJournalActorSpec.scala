package hyperdoc.journal

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.core.HyperdocTestObjects
import hyperdoc.journal.AuthorityJournalActor.CreateAuthority
import hyperdoc.journal.HyperdocJournalActor.{GetCurrentObject, NonexistentObject, RemoveObject, RemovedObject}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/** Tests for [[hyperdoc.journal.AuthorityJournalActor]] */
class AuthorityJournalActorSpec extends TestKit(HyperdocJournalActorUtil.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

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
}
