package hyperdoc.core

/** A property type is used as an enumeration of the allowed types of properties that a model can define.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait PropertyType

/** String property type. */
case object StringPropertyType extends PropertyType

/** Integer property type. */
case object IntegerPropertyType extends PropertyType

/** Double property type. */
case object DoublePropertyType extends PropertyType

/** Boolean property type. */
case object BooleanPropertyType extends PropertyType

/** Reference property type. */
case object ReferencePropertyType extends PropertyType

/** Date property type */
case object DatePropertyType extends PropertyType

/** A property constraint is used to validate the property values
  *
  * @author Ezequiel Foncubierta
  */
trait PropertyConstraint

/** A model entity definition is any entity within the model (i.e. model, schema, property). Any model entity
  * must extend this trait. Model entities live in the 'model' hyperdoc store, like
  * hyperdoc://model/core/1.0/naming#name
  *
  * @author Ezequiel Foncubierta
  */
sealed trait ModelEntityDefinition extends HyperdocObject {
  // ref should be in the 'model' store
  require(ref.store.equals(Store.ModelStoreRef))

  /**
   * Model entity name
   *
   * @return Name
   */
  def name: String
}

/** A model definition describes an entire model. Hyperdoc allows to define any number of models as they live
  * in different locations in the 'model' store. A model is composed by a list of schemas.
  *
  * @param ref Hyperdoc reference
  * @param name Model name
  * @param version Model version
  * @param schemas Model schemas
  * @param title Model title
  *
  * @author Ezequiel Foncubierta
  */
case class ModelDefinition(ref: HyperdocRef,
                           name: String,
                           version: String,
                           schemas: List[SchemaDefinition],
                           title: Option[String]) extends ModelEntityDefinition {
  // ref must has no filter
  require(ref.filter.isEmpty)
  // TODO add path regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+"))
}

/** An schema definition is a set of properties that can be applied to any hyperdoc node.
  *
  * @param ref Hyperdoc reference
  * @param name Schema name
  * @param properties Schema properties
  * @param title Schema title
  * @param description Schema description
  *
  * @author Ezequiel Foncubierta
  */
case class SchemaDefinition(ref: HyperdocRef,
                            name: String,
                            properties: List[PropertyDefinition],
                            title: Option[String],
                            description: Option[String]) extends ModelEntityDefinition {
  // ref must has no filter
  require(ref.filter.isEmpty)
  // TODO add schema regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+/\\w+"))
}

/** A property definition describes how a property should be manage.
  *
  * @param ref Hyperdoc reference
  * @param name Property name
  * @param propertyType Property type
  * @param title Property title
  * @param required Is property required?
  * @param multiple Is property multivalued?
  * @param description Property description
  * @param propertyConstraints Property constraints
  *
  * @author Ezequiel Foncubierta
  */
case class PropertyDefinition(ref: HyperdocRef,
                              name: String,
                              propertyType: PropertyType,
                              title: Option[String],
                              description: Option[String],
                              required: Option[Boolean],
                              multiple: Option[Boolean],
                              propertyConstraints: Option[List[PropertyConstraint]]) extends ModelEntityDefinition {
  // ref must has no filter
  require(ref.filter.isDefined)
  // TODO add schema regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+/\\w+"))
}

/** Model utility object */
object Model {
  /** Generate an hyperdoc reference base on a model identifier.
    *
    * @param name Model name
    * @param version Model version
    * @return Hyperdoc reference
    */
  def generateModelRef(name: String, version: String): HyperdocRef =
    HyperdocRef(Store.ModelStoreRef, Some(s"/$name/$version"), None)

  /** Generate an hyperdoc reference base on an schema identifier and a model reference.
    *
    * @param modelRef Model hyperdoc reference
    * @param name Schema name
    * @return Hyperdoc reference
    */
  def generateSchemaRef(modelRef: HyperdocRef, name: String) =
    HyperdocRef(Store.ModelStoreRef, Some(s"${modelRef.path.getOrElse("")}/$name"), None)

  /** Generate an hyperdoc reference base on a property identifier and a schema reference.
    *
    * @param schemaRef Schema hyperdoc reference
    * @param name Property name
    * @return Hyperdoc reference
    */
  def generatePropertyRef(schemaRef: HyperdocRef, name: String): HyperdocRef =
    HyperdocRef(Store.ModelStoreRef, Some(s"${schemaRef.path.getOrElse("/")}"), Some(name))

  /** Core model 1.0 reference. */
  val CoreModelRef_1_0 = HyperdocRef(Store.ModelStoreRef, Some("/core/1.0"), None)
}