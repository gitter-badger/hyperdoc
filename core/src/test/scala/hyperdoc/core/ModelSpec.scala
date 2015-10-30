package hyperdoc.core

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.Model]]
 */
class ModelSpec extends FlatSpec with PropertyChecks with Matchers {
  "A Model reference" should s"should be located at 'hyperdoc://${Store.ModelStoreRef}/{model.name}/{model.version}' and has no schema nor property" in {
    forAll(Table(
      ("store", "path", "filter"),
      (Store.AuthorityStoreRef, Some("/testModel/1.0"), None),
      (Store.NodeStoreRef, Some("/testModel/1.0"), None),
      (Store.ObjectStoreRef, Some("/testModel/1.0"), None),
      (Store.ModelStoreRef, Some("/testModel"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0/invalidSchema"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0"), Some("invalidProperty"))
    )) { (store: HyperdocStoreRef, path: Option[String], filter: Option[String]) =>
      val ref = HyperdocRef(store, path, filter)
      intercept[IllegalArgumentException] {
        ModelDefinition(ref, "testModel", "1.0", Nil, None)
      }
    }
  }
  "An Schema reference" should s"should be located at 'hyperdoc://${Store.ModelStoreRef}/{model.name}/{model.version}/{schema.name}' and has no property" in {
    forAll(Table(
      ("store", "path", "filter"),
      (Store.AuthorityStoreRef, Some("/testModel/1.0/testSchema"), None),
      (Store.NodeStoreRef, Some("/testModel/1.0/testSchema"), None),
      (Store.ObjectStoreRef, Some("/testModel/1.0/testSchema"), None),
      (Store.ModelStoreRef, Some("/testModel"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0/testSchema"), Some("invalidProperty"))
    )) { (store: HyperdocStoreRef, path: Option[String], filter: Option[String]) =>
      val ref = HyperdocRef(store, path, filter)
      intercept[IllegalArgumentException] {
        SchemaDefinition(ref, "testSchema", Nil, None, None)
      }
    }
  }
  "A Property reference" should s"should be located at 'hyperdoc://${Store.ModelStoreRef}/{model.name}/{model.version}/{schema.name}#{property.name}'" in {
    forAll(Table(
      ("store", "path", "filter"),
      (Store.AuthorityStoreRef, Some("/testModel/1.0/testSchema#testProperty"), None),
      (Store.NodeStoreRef, Some("/testModel/1.0/testSchema#testProperty"), None),
      (Store.ObjectStoreRef, Some("/testModel/1.0/testSchema#testProperty"), None),
      (Store.ModelStoreRef, Some("/testModel"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0"), None),
      (Store.ModelStoreRef, Some("/testModel/1.0/testSchema"), None)
    )) { (store: HyperdocStoreRef, path: Option[String], filter: Option[String]) =>
      val ref = HyperdocRef(store, path, filter)
      intercept[IllegalArgumentException] {
        PropertyDefinition(ref, "testProperty", StringPropertyType, None, None, None, None, None)
      }
    }
  }
}
