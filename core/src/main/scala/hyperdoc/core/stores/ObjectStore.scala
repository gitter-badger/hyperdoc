package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.ObjectBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Object store */
class ObjectStore(implicit objectBackend: ObjectBackend) extends Store[ContentObject] {
  /**
   * Save an object
   *
   * @param obj Object
   * @param replace Flag to replace existing object
   * @tparam A Object type
   * @return Exception or saved object
   */
  def save[A <: ContentObject : ClassTag](obj: A, replace: Boolean = false): Try[A] = try {
    if (!replace && Await.result(objectBackend.get(obj.ref), 1.seconds).isDefined) {
      Failure(new Exception(s"Node ${obj.ref} already exists"))
    } else {
      Success(Await.result(objectBackend.save(obj), 1.seconds))
    }
  } catch {
    case e: Throwable => Failure(e)
  }

  /**
   * Remove an object
   *
   * @param ref Object reference
   */
  def remove(ref: HyperdocRef): Unit =
    Await.result(objectBackend.remove(ref), 1.seconds)

  /** Get a generic object
    *
    * @param ref Object reference
    * @tparam A Object type
    * @return Object or None
    */
  def get[A <: ContentObject : ClassTag](ref: HyperdocRef): Option[A] =
    try {
      Await.result(objectBackend.get[A](ref), 1.seconds) match {
        case Some(node) if classTag[A] == classTag[node.type] => Some(node)
        case _ => None
      }
    } catch {
      case e: Throwable => None
    }

  /**
   * Get an object
   *
   * @param ref Object reference
   * @return Object or None
   */
  def getObject(ref: HyperdocRef): Option[ContentObject] = get[ContentObject](ref)

  /**
   * Get a binary object
   *
   * @param ref Object reference
   * @return Object or None
   */
  def getBinaryObject(ref: HyperdocRef): Option[BinaryContentObject] = get[BinaryContentObject](ref)

  /**
   * Get a text object
   *
   * @param ref Object reference
   * @return Object or None
   */
  def getTextObject(ref: HyperdocRef): Option[TextContentObject] = get[TextContentObject](ref)

  /** Check whether an object exists
    *
    * @param ref Object reference
    * @return True if the object exists
    */
  def exists(ref: HyperdocRef): Boolean = get(ref).isDefined

  /** Get a generic object
    *
    * @param ref Object reference
    * @return Object or Nonde
    */
  def apply(ref: HyperdocRef): Option[ContentObject] = getObject(ref)
}
