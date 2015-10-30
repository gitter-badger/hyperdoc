package hyperdoc.core.stores

import hyperdoc.core._
import org.scalatest.{FlatSpec, Matchers}

/** Tests for [[hyperdoc.core.stores.NodeStore]] */
class NodeStoreSpec extends FlatSpec with Matchers {
  "An node" should "not be saved if already exists" in {
    DummyBackendHyperdoc.nodeStore.save(HyperdocTestObjects.TEST_CONTAINER_NODE).isFailure shouldBe true
  }
  it should "be saved correctly if replacing is forced" in {
    val result = DummyBackendHyperdoc.nodeStore.save(HyperdocTestObjects.TEST_CONTAINER_NODE, replace = true)
    result.isSuccess shouldEqual true
    result.get shouldEqual HyperdocTestObjects.TEST_CONTAINER_NODE
  }
  it should "be get correctly by its container node reference" in {
    val opt = DummyBackendHyperdoc.nodeStore.get[ContainerNode](HyperdocTestObjects.TEST_CONTAINER_NODE_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_CONTAINER_NODE
  }
  it should "be get correctly by its content node reference" in {
    val opt = DummyBackendHyperdoc.nodeStore.get[ContentNode](HyperdocTestObjects.TEST_CONTENT_NODE_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_CONTENT_NODE
  }
  it should "not be get by an invalid node reference" in {
    DummyBackendHyperdoc.nodeStore.get[Node](Node.generateNodeRef) should not be defined
  }
}
