package hyperdoc.core

/** Store manager */
trait Store[A] {
  def apply(ref: HyperdocRef): Option[A]
}

/** Stores utility object */
object Store {
  /** Authority store reference */
  val AuthorityStoreRef = HyperdocStoreRef("authority")

  /** Node store reference */
  val NodeStoreRef = HyperdocStoreRef("node")

  /** Content store reference */
  val ObjectStoreRef = HyperdocStoreRef("object")

  /** Model store reference */
  val ModelStoreRef = HyperdocStoreRef("model")
}