package hyperdoc.core

import java.util.UUID

/** An hyperdoc node is the basic piece of information managed by Hyperdoc. Nodes contain properties, which are
  * based on schemas, which are defined in the 'model' hyperdoc store.
  *
  * Nodes live in the 'node' hyperdoc store, like hyperdoc://node/00000000-0000-0000-0000-000000000000.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait Node extends HyperdocObject {
  // ref should be in the 'node' store
  require(ref.store.equals(Store.NodeStoreRef))
  // and has no filter
  require(ref.filter.isEmpty)

  /** Node schemas
    *
    * @return List of node schemas
    */
  def schemas: List[HyperdocRef]

  /** Node properties
    *
    * @return Map of node properties
    */
  def properties: Map[HyperdocRef, PropertyValue]
}

/** An structural node represent a node in a typical document hierarchy. An structural node can be either a
  * container or a content, and will always has a parent node. Except for the root node.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait StructuralNode extends Node {
  /** Parent node reference
    *
    * @return Parent node reference
    */
  def parentRef: HyperdocRef
}

/** A meta node describes information about a node. It can be located anywhere in the hierarchy and is
  * always linked to another node. For example, a link to a content is meta node.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait MetaNode extends Node {
  /** Target node reference.
    *
    * @return Target node reference
    */
  def targetRef: HyperdocRef
}

/** A container node is a node that contains other nodes, either structural or meta nodes. If a container node
  * is deleted, all the nodes underneath are deleted too. This is, all the nodes within a container node depend
  * on the container node.
  *
  * @param ref Hyperdoc reference
  * @param parentRef Parent node reference
  * @param schemas Node schemas
  * @param properties Node properties
  * @param children Children node references
  *
  * @author Ezequiel Foncubierta
  */
case class ContainerNode(ref: HyperdocRef,
                         parentRef: HyperdocRef,
                         schemas: List[HyperdocRef],
                         properties: Map[HyperdocRef, PropertyValue],
                         children: List[HyperdocRef]) extends StructuralNode

/** A content node is a node that may have a content object associated, although it is not required. Also, even
  * that a content node is an structural node, it can contains other nodes underneath.
  *
  * @param ref Hyperdoc reference
  * @param parentRef Parent node reference
  * @param schemas Node schemas
  * @param properties Node properties
  * @param objectRef Object node reference
  *
  * @author Ezequiel Foncubierta
  */
case class ContentNode(ref: HyperdocRef,
                       parentRef: HyperdocRef,
                       schemas: List[HyperdocRef],
                       properties: Map[HyperdocRef, PropertyValue],
                       objectRef: Option[HyperdocRef]) extends StructuralNode

/** A link node is a meta node that links to another node. Permissions and other characteristics are inherited from
  * the linked node.
  *
  * @param ref Hyperdoc reference
  * @param targetRef Target node reference
  * @param schemas Node schemas
  * @param properties Node properties
  *
  * @author Ezequiel Foncubierta
  */
case class LinkNode(ref: HyperdocRef,
                    targetRef: HyperdocRef,
                    schemas: List[HyperdocRef],
                    properties: Map[HyperdocRef, PropertyValue]) extends MetaNode

/** Node utility object */
object Node {

  /** Generate an hyperdoc reference using a node uuid.
    *
    * @param uuid Node uuid
    * @return Hyperdoc reference
    */
  def buildNodeRef(uuid: String): HyperdocRef =
    HyperdocRef(Store.NodeStoreRef, Some(s"/$uuid"), None)

  /**
   * Generate a random node reference.
   *
   * @return Hyperdoc reference
   */
  def generateNodeRef: HyperdocRef = buildNodeRef(UUID.randomUUID().toString)

  /** Root node reference of node store. */
  val RootNodeRef = buildNodeRef("000000000-0000-0000-0000-000000000000")
}