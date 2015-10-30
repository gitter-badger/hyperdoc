package hyperdoc.model

import hyperdoc.core.DummyBackendHyperdoc
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.model.ModelBootstrap]]
 */
class ModelBootstrapSpec extends FlatSpec with Matchers {
  implicit val hyperdoc = DummyBackendHyperdoc

  "Model bootstrap" should "start correctly" in {
    object MyBS extends ModelBootstrap
    val result = MyBS.bootstrap
    result.isSuccess shouldBe true
  }
}
