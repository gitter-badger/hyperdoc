package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.NodeBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Manager for [[hyperdoc.core.Node]] entities.
  *
  * @param nodeBackend Node backend
  *
  * @author Ezequiel Foncubierta
  */
class NodeStore(implicit nodeBackend: NodeBackend) extends Store[Node] {

  /** Save a node.
    *
    * @param node Node
    * @param replace Flag to replace an existing node
    * @tparam A Node type
    * @return Saved node
    */
  def save[A <: Node : ClassTag](node: A, replace: Boolean = false): Try[A] = try {
    if (!replace && Await.result(nodeBackend.get(node.ref), 1.seconds).isDefined) {
      Failure(new Exception(s"Node ${node.ref} already exists"))
    } else {
      Success(Await.result(nodeBackend.save(node), 1.seconds))
    }
  } catch {
    case e: Throwable => Failure(e)
  }

  /** Remove a node.
    *
    * @param ref Node reference
    */
  def remove(ref: HyperdocRef): Unit =
    Await.result(nodeBackend.remove(ref), 1.seconds)

  /** Get a node.
    *
    * @param ref Node reference
    * @tparam A Node type
    * @return Node
    */
  def get[A <: Node : ClassTag](ref: HyperdocRef): Option[A] =
    try {
      Await.result(nodeBackend.get[A](ref), 1.seconds) match {
        case Some(node) if classTag[A] == classTag[node.type] => Some(node)
        case _ => None
      }
    } catch {
      case e: Throwable => None
    }


  /** Get a node.
    *
    * @param ref Node reference
    * @return Node
    */
  def getNode(ref: HyperdocRef): Option[Node] = get[Node](ref)

  /** Get a structural node.
    *
    * @param ref Node reference
    * @return Node
    */
  def getStructuralNode(ref: HyperdocRef): Option[StructuralNode] = get[StructuralNode](ref)

  /** Get a container node.
    *
    * @param ref Node reference
    * @return Node
    */
  def getContainerNode(ref: HyperdocRef): Option[ContainerNode] = get[ContainerNode](ref)

  /** Get a content node.
    *
    * @param ref Node reference
    * @return Node
    */
  def getContentNode(ref: HyperdocRef): Option[ContentNode] = get[ContentNode](ref)

  /** Check whether a node exists.
    *
    * @param ref Node reference
    * @return True if the node exists
    */
  def exists(ref: HyperdocRef): Boolean = get(ref).isDefined

  /** Get a node.
    *
    * @param ref Node reference
    * @return Node
    */
  def apply(ref: HyperdocRef): Option[Node] = getNode(ref)
}
