package hyperdoc.core

/** A property value is the actual property set on hyperdoc nodes.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait PropertyValue

/** String property value.
  *
  * @param value String value
  */
case class StringPropertyValue(value: String) extends PropertyValue

/** Integer property value.
  *
  * @param value Integer value
  */
case class IntegerPropertyValue(value: Int) extends PropertyValue

/** Double property value.
  *
  * @param value Double value
  */
case class DoublePropertyValue(value: Double) extends PropertyValue

/** Boolean property value.
  *
  * @param value Boolean value
  */
case class BooleanPropertyValue(value: Boolean) extends PropertyValue

/** Reference property value.
  *
  * @param value Hyperdoc reference value
  */
case class ReferencePropertyValue(value: HyperdocRef) extends PropertyValue

/** Date property value.
  *
  * @param value Date value
  */
case class DatePropertyValue(value: HyperdocRef) extends PropertyValue

/** Multivalued property value.
  *
  * @param values List of values
  */
case class MultivaluedPropertyValue(values: List[PropertyValue]) extends PropertyValue
