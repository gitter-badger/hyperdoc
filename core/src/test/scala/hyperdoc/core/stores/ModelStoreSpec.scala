package hyperdoc.core.stores

import hyperdoc.core._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.stores.ModelStore]]
 */
class ModelStoreSpec extends FlatSpec with Matchers {
  "A model object definition" should "not be saved if already exists" in {
    DummyBackendHyperdoc.modelStore.saveModel(HyperdocTestObjects.TEST_MODEL_DEFINITION).isFailure shouldBe true
  }
  it should "be saved correctly if replacing is forced" in {
    val result = DummyBackendHyperdoc.modelStore.saveModel(HyperdocTestObjects.TEST_MODEL_DEFINITION, replace = true)
    result.isSuccess shouldEqual true
    result.get shouldEqual HyperdocTestObjects.TEST_MODEL_DEFINITION
  }
  it should "be get correctly by its model reference" in {
    val opt = DummyBackendHyperdoc.modelStore.getModelDefinition(HyperdocTestObjects.TEST_MODEL_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_MODEL_DEFINITION
  }
  it should "be get correctly by its schema reference" in {
    val opt = DummyBackendHyperdoc.modelStore.getSchemaDefinition(HyperdocTestObjects.TEST_SCHEMA_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_SCHEMA_DEFINITION
  }
  it should "be get correctly by its property reference" in {
    val opt = DummyBackendHyperdoc.modelStore.getPropertyDefinition(HyperdocTestObjects.TEST_STRING_PROPERTY_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_STRING_PROPERTY_DEFINITION
  }
  it should "not be get by an invalid model reference" in {
    DummyBackendHyperdoc.modelStore.getModelDefinition(Model.generateModelRef("invalidModel", "1.0")) should not be defined
  }
}
