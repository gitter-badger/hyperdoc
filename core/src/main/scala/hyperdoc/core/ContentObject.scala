package hyperdoc.core

import java.util.UUID

/** Content object */
sealed trait ContentObject extends HyperdocObject {
  require(ref.store.equals(Store.ObjectStoreRef))
  require(size >= 0)

  def size: Integer

  def mimetype: String
}

/** Binary content
  *
  * @param ref Node reference
  * @param size Content size
  * @param mimetype Content mime type
  */
case class BinaryContentObject(ref: HyperdocRef,
                               size: Integer,
                               mimetype: String) extends ContentObject

/** Text content
  *
  * @param ref Node reference
  * @param size Content size
  * @param mimetype Content mime type
  * @param charset Text charset
  */
case class TextContentObject(ref: HyperdocRef,
                             size: Integer,
                             mimetype: String,
                             charset: String) extends ContentObject

/** Contents utility object */
object ContentObject {

  /** Generate an hyperdoc reference using a uuid
    *
    * @param uuid Object uuid
    * @return Hyperdoc reference
    */
  def buildNodeRef(uuid: String): HyperdocRef =
    HyperdocRef(Store.ObjectStoreRef, Some(s"/$uuid"), None)

  /**
   * Generate a random object reference
   *
   * @return Hyperdoc reference
   */
  def generateObjectRef: HyperdocRef = buildNodeRef(UUID.randomUUID().toString)
}