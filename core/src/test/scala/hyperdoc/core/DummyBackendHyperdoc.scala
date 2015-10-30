package hyperdoc.core

import hyperdoc.core.stores.{AuthorityStore, ModelStore, NodeStore, ObjectStore}

/** Hyperdoc main object with dummy backend */
object DummyBackendHyperdoc extends Hyperdoc {

  import hyperdoc.core.backend.DummyBackend._

  object TestAuthorityStore extends AuthorityStore

  val authorityStore = TestAuthorityStore

  object TestModelStore extends ModelStore

  val modelStore = TestModelStore

  object TestNodeStore extends NodeStore

  val nodeStore = TestNodeStore

  object TestObjectStore extends ObjectStore

  val objectStore = TestObjectStore

}
