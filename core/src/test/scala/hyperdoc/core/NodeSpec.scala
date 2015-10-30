package hyperdoc.core

import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.core.Node]]
 */
class NodeSpec extends FlatSpec with Matchers {
  "An Node" should s"not be defined outside '${Store.NodeStoreRef}' store" in {
    val nodeRef = HyperdocRef(Store.AuthorityStoreRef, Some("/test"), None)
    intercept[IllegalArgumentException] {
      ContainerNode(nodeRef, null, null, null, null)
    }
  }

  "A node reference" should "be valid when created randomly" in {
    Node.generateNodeRef.toString should fullyMatch regex
      "hyperdoc://node/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
  }
}
