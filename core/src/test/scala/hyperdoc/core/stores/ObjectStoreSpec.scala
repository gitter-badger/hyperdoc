package hyperdoc.core.stores

import hyperdoc.core._
import org.scalatest.{FlatSpec, Matchers}

/** Tests for [[hyperdoc.core.stores.ObjectStore]]
  */
class ObjectStoreSpec extends FlatSpec with Matchers {
  "An object" should "not be saved if already exists" in {
    DummyBackendHyperdoc.objectStore.save(HyperdocTestObjects.TEST_BINARY_OBJECT).isFailure shouldBe true
  }
  it should "be saved correctly if replacing is forced" in {
    val result = DummyBackendHyperdoc.objectStore.save(HyperdocTestObjects.TEST_BINARY_OBJECT, replace = true)
    result.isSuccess shouldEqual true
    result.get shouldEqual HyperdocTestObjects.TEST_BINARY_OBJECT
  }
  it should "be get correctly by its binary object reference" in {
    val opt = DummyBackendHyperdoc.objectStore.get[BinaryContentObject](HyperdocTestObjects.TEST_BINARY_OBJECT_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_BINARY_OBJECT
  }
  it should "be get correctly by its text object reference" in {
    val opt = DummyBackendHyperdoc.objectStore.get[TextContentObject](HyperdocTestObjects.TEST_TEXT_OBJECT_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_TEXT_OBJECT
  }
  it should "not be get by an invalid object reference" in {
    DummyBackendHyperdoc.objectStore.get[ContentObject](ContentObject.generateObjectRef) should not be defined
  }
}
