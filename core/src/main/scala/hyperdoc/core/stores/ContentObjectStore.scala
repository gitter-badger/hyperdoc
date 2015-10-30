package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.ContentObjectBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Manager for [[hyperdoc.core.ContentObject]] entities.
  *
  * @param contentObjectBackend Content object backend
  *
  * @author Ezequiel Foncubierta
  */
class ContentObjectStore(implicit contentObjectBackend: ContentObjectBackend) extends Store[ContentObject] {
  /** Save a content object.
    *
    * @param obj Content object
    * @param replace Flag to replace existing content object
    * @tparam A Content object type
    * @return Saved content object
    */
  def save[A <: ContentObject : ClassTag](obj: A, replace: Boolean = false): Try[A] = try {
    if (!replace && Await.result(contentObjectBackend.get(obj.ref), 1.seconds).isDefined) {
      Failure(new Exception(s"Node ${obj.ref} already exists"))
    } else {
      Success(Await.result(contentObjectBackend.save(obj), 1.seconds))
    }
  } catch {
    case e: Throwable => Failure(e)
  }

  /** Remove a content object.
    *
    * @param ref Content object reference
    */
  def remove(ref: HyperdocRef): Unit =
    Await.result(contentObjectBackend.remove(ref), 1.seconds)

  /** Get a content object.
    *
    * @param ref Content object reference
    * @tparam A Content object type
    * @return Content object
    */
  def get[A <: ContentObject : ClassTag](ref: HyperdocRef): Option[A] =
    try {
      Await.result(contentObjectBackend.get[A](ref), 1.seconds) match {
        case Some(node) if classTag[A] == classTag[node.type] => Some(node)
        case _ => None
      }
    } catch {
      case e: Throwable => None
    }

  /** Get a content object.
    *
    * @param ref Content object reference
    * @return Content object
    */
  def getObject(ref: HyperdocRef): Option[ContentObject] = get[ContentObject](ref)

  /** Get a binary content object.
    *
    * @param ref Binary object reference
    * @return Binary content object
    */
  def getBinaryObject(ref: HyperdocRef): Option[BinaryContentObject] = get[BinaryContentObject](ref)

  /**
   * Get a text content object.
   *
   * @param ref Text object reference
   * @return Text content object
   */
  def getTextObject(ref: HyperdocRef): Option[TextContentObject] = get[TextContentObject](ref)

  /** Check whether a content object exists.
    *
    * @param ref Content object reference
    * @return True if the content object exists
    */
  def exists(ref: HyperdocRef): Boolean = get(ref).isDefined

  /** Get a generic content object
    *
    * @param ref Content object reference
    * @return Content object
    */
  def apply(ref: HyperdocRef): Option[ContentObject] = getObject(ref)
}
