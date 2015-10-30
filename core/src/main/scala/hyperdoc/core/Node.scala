package hyperdoc.core

import java.util.UUID

/** Node */
sealed trait Node extends HyperdocObject {
  require(ref.store.equals(Store.NodeStoreRef))

  /**
   * Node schemas
   *
   * @return List of node schemas
   */
  def schemas: List[HyperdocRef]

  /**
   * Node properties
   *
   * @return Map of node properties
   */
  def properties: Map[HyperdocRef, Property]
}

/** Structural node */
sealed trait StructuralNode extends Node {
  require(parentRef.store.equals(Store.NodeStoreRef))

  /** Parent node reference
    *
    * @return Parent node reference
    */
  def parentRef: HyperdocRef
}

/** Meta node */
sealed trait MetaNode extends Node {
  /** Target node reference
    *
    * @return Target node reference
    */
  def targetRef: HyperdocRef
}

/** Container node
  *
  * @param ref Hyperdoc reference
  * @param parentRef Parent node reference
  * @param children Children node references
  * @param schemas Node schemas
  * @param properties Node properties
  */
case class ContainerNode(ref: HyperdocRef,
                         parentRef: HyperdocRef,
                         children: List[HyperdocRef],
                         schemas: List[HyperdocRef],
                         properties: Map[HyperdocRef, Property]) extends StructuralNode

/** Content node
  *
  * @param ref Hyperdoc reference
  * @param parentRef Parent node reference
  * @param objectRef Object node reference
  * @param schemas Node schemas
  * @param properties Node properties
  */
case class ContentNode(ref: HyperdocRef,
                       parentRef: HyperdocRef,
                       objectRef: HyperdocRef,
                       schemas: List[HyperdocRef],
                       properties: Map[HyperdocRef, Property]) extends StructuralNode

/** Link node
  *
  * @param ref Hyperdoc reference
  * @param targetRef Target node reference
  * @param schemas Node schemas
  * @param properties Node properties
  */
case class LinkNode(ref: HyperdocRef,
                    targetRef: HyperdocRef,
                    schemas: List[HyperdocRef],
                    properties: Map[HyperdocRef, Property]) extends MetaNode

/** Render node
  *
  * @param ref Hyperdoc reference
  * @param targetRef Target node reference
  * @param schemas Node schemas
  * @param properties Node properties
  */
case class RenderNode(ref: HyperdocRef,
                      targetRef: HyperdocRef,
                      schemas: List[HyperdocRef],
                      properties: Map[HyperdocRef, Property]) extends MetaNode

/** Nodes utility object */
object Node {

  /** Generate an hyperdoc reference using a node uuid
    *
    * @param uuid Node uuid
    * @return Hyperdoc reference
    */
  def buildNodeRef(uuid: String): HyperdocRef =
    HyperdocRef(Store.NodeStoreRef, Some(s"/$uuid"), None)

  /**
   * Generate a random node reference
   *
   * @return Hyperdoc reference
   */
  def generateNodeRef: HyperdocRef = buildNodeRef(UUID.randomUUID().toString)

  /** Root node reference of node store */
  val RootNodeRef = buildNodeRef("000000000-0000-0000-0000-000000000000")
}