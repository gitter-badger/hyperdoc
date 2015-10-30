package hyperdoc.core.bootstrap

import hyperdoc.core.DummyBackendHyperdoc
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.bootstrap.ModelBootstrap]]
 */
class ModelBootstrapSpec extends FlatSpec with Matchers {
  implicit val hyperdoc = DummyBackendHyperdoc

  "Model bootstrap" should "start correctly" in {
    object MyBS extends ModelBootstrap
    val result = MyBS.bootstrap
    result.isSuccess shouldBe true
  }
}
