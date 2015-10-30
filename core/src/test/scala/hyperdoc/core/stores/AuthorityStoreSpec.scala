package hyperdoc.core.stores

import hyperdoc.core._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.stores.AuthorityStore]]
 */
class AuthorityStoreSpec extends FlatSpec with Matchers {
  "An authority" should "not be saved if already exists" in {
    DummyBackendHyperdoc.authorityStore.save(HyperdocTestObjects.TEST_USER).isFailure shouldBe true
  }
  it should "be saved correctly if replacing is forced" in {
    val result = DummyBackendHyperdoc.authorityStore.save(HyperdocTestObjects.TEST_USER, replace = true)
    result.isSuccess shouldEqual true
    result.get shouldEqual HyperdocTestObjects.TEST_USER
  }
  it should "be get correctly by its user reference" in {
    val opt = DummyBackendHyperdoc.authorityStore.get[User](HyperdocTestObjects.TEST_USER_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_USER
  }
  it should "be get correctly by its group reference" in {
    val opt = DummyBackendHyperdoc.authorityStore.get[User](HyperdocTestObjects.TEST_GROUP_REF)
    opt shouldBe defined
    opt.get shouldEqual HyperdocTestObjects.TEST_GROUP
  }
  it should "not be get by an invalid authority reference" in {
    DummyBackendHyperdoc.authorityStore.get[User](Authority.generateUserRef("invalidUser")) should not be defined
  }
}
