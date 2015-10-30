package hyperdoc.core

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.Authority]]
 */
class AuthoritySpec extends FlatSpec with PropertyChecks with Matchers {
  "A User reference" should s"should be located at 'hyperdoc://${Store.AuthorityStoreRef}/user/*' and has no filter parameter" in {
    // test invalid user references
    forAll(Table(
      ("store", "path", "filter"),
      (Store.NodeStoreRef, Some("/user/test"), None),
      (Store.ModelStoreRef, Some("/user/test"), None),
      (Store.ObjectStoreRef, Some("/user/test"), None),
      (Store.AuthorityStoreRef, Some("/group/test"), None),
      (Store.AuthorityStoreRef, Some("/user/test"), Some("invalidFilter")),
      (Store.AuthorityStoreRef, Some("/user/test/invalidSubPath"), None)
    )) { (store: HyperdocStoreRef, path: Option[String], filter: Option[String]) =>
      val ref = HyperdocRef(store, path, filter)
      intercept[IllegalArgumentException] {
        User(ref, "", null, Map(), Nil)
      }
    }

    // valid user reference
    User(Authority.generateUserRef("test"), "", null, Map(), Nil)
  }
  "A Group reference" should s"should be located at 'hyperdoc://${Store.AuthorityStoreRef}/group/*' and has no filter parameter" in {
    // test invalid group references
    forAll(Table(
      ("store", "path", "filter"),
      (Store.NodeStoreRef, Some("/group/test"), None),
      (Store.ModelStoreRef, Some("/group/test"), None),
      (Store.ObjectStoreRef, Some("/group/test"), None),
      (Store.AuthorityStoreRef, Some("/user/test"), None),
      (Store.AuthorityStoreRef, Some("/group/test"), Some("invalidFilter")),
      (Store.AuthorityStoreRef, Some("/group/test/invalidSubPath"), None)
    )) { (store: HyperdocStoreRef, path: Option[String], filter: Option[String]) =>
      val ref = HyperdocRef(store, path, filter)
      intercept[IllegalArgumentException] {
        Group(ref, null, null)
      }
    }

    // valid group reference
    Group(Authority.generateGroupRef("test"), null, null)
  }
}
