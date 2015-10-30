package hyperdoc.core

/** Node property */
sealed trait Property

/** String property
  *
  * @param value Value
  */
case class StringProperty(value: String) extends Property

/** Integer property
  *
  * @param value Value
  */
case class IntegerProperty(value: Int) extends Property

/** Double property
  *
  * @param value Value
  */
case class DoubleProperty(value: Double) extends Property

/** Boolean property
  *
  * @param value Value
  */
case class BooleanProperty(value: Boolean) extends Property

/** Reference property
  *
  * @param value Reference reference
  */
case class ReferenceProperty(value: HyperdocRef) extends Property

/** Multivalued property
  *
  * @param values List of values
  */
case class MultivaluedProperty(values: List[Property]) extends Property
