package hyperdoc.core

/** Hyperdoc store ref
  *
  * @param name Store name
  */
sealed case class HyperdocStoreRef(name: String) {
  override def toString = s"$name"
}

/** Hyperdoc unique reference
  *
  * @param store Hyperdoc store
  * @param path Reference path
  * @param filter Reference filter
  */
sealed case class HyperdocRef(store: HyperdocStoreRef, path: Option[String], filter: Option[String]) {
  override def toString = s"hyperdoc://$store${path.getOrElse("")}${if(filter.isDefined) "#" else ""}${filter.getOrElse("")}"
}
