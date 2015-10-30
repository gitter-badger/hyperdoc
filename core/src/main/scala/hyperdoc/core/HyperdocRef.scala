package hyperdoc.core

/** Hyperdoc store reference.
  *
  * Uniquely identifies an hyperdoc store.
  *
  * @param name Store name
  *
  * @author Ezequiel Foncubierta
  */
sealed case class HyperdocStoreRef(name: String) {
  /** String representation of an hyperdoc store reference */
  override def toString = s"$name"
}

/** Hyperdoc unique reference
  *
  * Uniquely identifies an hyperdoc object. It uses a URI format to representing the reference.
  *
  * @example hyperdoc://model/core/1.0/naming#name
  *
  * @param store Hyperdoc store
  * @param path Reference path
  * @param filter Reference filter
  *
  * @author Ezequiel Foncubierta
  */
sealed case class HyperdocRef(store: HyperdocStoreRef, path: Option[String], filter: Option[String]) {
  /** String representation of an hyperdoc reference as URI format */
  override def toString = s"hyperdoc://$store${path.getOrElse("")}${if (filter.isDefined) "#" else ""}${filter.getOrElse("")}"
}
