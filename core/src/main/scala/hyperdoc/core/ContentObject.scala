package hyperdoc.core

import java.util.UUID

/** An hyperdoc content object is the representation of document bodies (i.e. image data, text, etc.). Content
  * objects live in the 'object' hyperdoc store, like hyperdoc://object/00000000-0000-0000-0000-000000000000.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait ContentObject extends HyperdocObject {
  // ref should be in the 'authority' store
  require(ref.store.equals(Store.ObjectStoreRef))
  // and has no filter
  require(ref.filter.isEmpty)
  // empty files are not allowed
  require(size >= 0)

  /** Content object size
    *
    * @return Size
    */
  def size: Integer

  /**
   * Content object mimetype
   *
   * @return Mimetype
   */
  def mimetype: String
}

/** A binary content object is an implementation of [[hyperdoc.core.ContentObject]] for binary contents.
  *
  * @param ref Node reference
  * @param size Content size
  * @param mimetype Content mime type
  *
  * @author Ezequiel Foncubierta
  */
case class BinaryContentObject(ref: HyperdocRef,
                               size: Integer,
                               mimetype: String) extends ContentObject

/** A text content object is an implementation of [[hyperdoc.core.ContentObject]] for text contents.
  *
  * @param ref Node reference
  * @param size Content size
  * @param mimetype Content mime type
  * @param charset Text charset
  *
  * @author Ezequiel Foncubierta
  */
case class TextContentObject(ref: HyperdocRef,
                             size: Integer,
                             mimetype: String,
                             charset: String) extends ContentObject

/** Content objects utility object */
object ContentObject {

  /** Generate an hyperdoc reference using a uuid.
    *
    * @param uuid Object uuid
    * @return Hyperdoc reference
    */
  def buildNodeRef(uuid: String): HyperdocRef =
    HyperdocRef(Store.ObjectStoreRef, Some(s"/$uuid"), None)

  /**
   * Generate a random object reference.
   *
   * @return Hyperdoc reference
   */
  def generateObjectRef: HyperdocRef = buildNodeRef(UUID.randomUUID().toString)
}