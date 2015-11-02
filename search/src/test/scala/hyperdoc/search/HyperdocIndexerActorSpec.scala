package hyperdoc.search

import akka.testkit.{ImplicitSender, TestKit}
import hyperdoc.journal.HyperdocJournalActorUtil
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class HyperdocIndexerActorSpec extends TestKit(HyperdocJournalActorUtil.randomActorSystem) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

//  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "An hyperdoc search actor" must {
    "listen to create node events" in {
//      val node = HyperdocTestObjects.randomContentNode
//      val aggregate = HyperdocJournalManager.getJournalActor(node.ref)
//      aggregate ! CreateNode(node)
//      expectMsg(node)
//
//      implicit val mat = ActorMaterializer()(system)
//      val journal = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)
//      val source = journal.eventsByTag("node", 0L)
//      println(source.runWith(TestSink.probe[EventEnvelope])
//        .request(1).expectNext())
    }
  }
}