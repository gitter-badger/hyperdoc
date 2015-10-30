package hyperdoc.core

/** An store manages entities in the backend.
  *
  * @tparam A Hyperdoc object type
  */
trait Store[A <: HyperdocObject] {
  /** Get an hyperdoc object by its reference.
    *
    * @param ref Hyperdoc reference
    * @return Hyperdoc object
    */
  def apply(ref: HyperdocRef): Option[A]
}

/** Store utility object */
object Store {
  /** Authority store reference. */
  val AuthorityStoreRef = HyperdocStoreRef("authority")

  /** Node store reference. */
  val NodeStoreRef = HyperdocStoreRef("node")

  /** Content store reference. */
  val ObjectStoreRef = HyperdocStoreRef("object")

  /** Model store reference. */
  val ModelStoreRef = HyperdocStoreRef("model")
}