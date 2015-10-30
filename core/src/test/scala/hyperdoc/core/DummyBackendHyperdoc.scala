package hyperdoc.core

import com.typesafe.config.{Config, ConfigFactory}
import hyperdoc.core.stores.{AuthorityStore, ContentObjectStore, ModelStore, NodeStore}

/** Hyperdoc main object with dummy backend */
object DummyBackendHyperdoc extends Hyperdoc {

  val config: Config = ConfigFactory.load().getConfig("hyperdoc")

  import hyperdoc.core.backend.DummyBackend._

  object TestAuthorityStore extends AuthorityStore

  val authorityStore = TestAuthorityStore

  object TestModelStore extends ModelStore

  val modelStore = TestModelStore

  object TestNodeStore extends NodeStore

  val nodeStore = TestNodeStore

  object TestContentObjectStore extends ContentObjectStore

  val contentObjectStore = TestContentObjectStore

}
