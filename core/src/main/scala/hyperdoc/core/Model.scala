package hyperdoc.core

/** Property type */
sealed trait PropertyType

/** String property type */
case object StringPropertyType extends PropertyType

/** Integer property type */
case object IntegerPropertyType extends PropertyType

/** Double property type */
case object DoublePropertyType extends PropertyType

/** Boolean property type */
case object BooleanPropertyType extends PropertyType

/** Reference property type */
case object ReferencePropertyType extends PropertyType

/** Property constraint */
trait PropertyConstraint

/**
 * Model object definition. All classes that defines a model should extend this trait.
 */
sealed trait ModelObjectDefinition extends HyperdocObject {
  // ref should be in the 'model' store
  require(ref.store.equals(Store.ModelStoreRef))

  def name: String
}

/** Model definition
  *
  * @param ref Hyperdoc reference
  * @param name Model name
  * @param version Model version
  * @param schemas Model schemas
  * @param title: Model title
  */
case class ModelDefinition(ref: HyperdocRef,
                           name: String,
                           version: String,
                           schemas: List[SchemaDefinition],
                           title: Option[String]) extends ModelObjectDefinition {
  // ref must has no filter
  require(ref.filter.isEmpty)
  // TODO add path regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+"))
}

/** Schema definition
  *
  * @param ref Hyperdoc reference
  * @param name Schema name
  * @param properties Schema properties
  * @param title Schema title
  * @param description Schema description
  */
case class SchemaDefinition(ref: HyperdocRef,
                            name: String,
                            properties: List[PropertyDefinition],
                            title: Option[String],
                            description: Option[String]) extends ModelObjectDefinition {
  // ref must has no filter
  require(ref.filter.isEmpty)
  // TODO add schema regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+/\\w+"))
}

/** Property definition
  *
  * @param ref Hyperdoc reference
  * @param name Property name
  * @param propertyType Property type
  * @param title Property title
  * @param required Is property required?
  * @param multiple Is property multivalued?
  * @param description Property description
  * @param propertyConstraints Property constraints
  */
case class PropertyDefinition(ref: HyperdocRef,
                              name: String,
                              propertyType: PropertyType,
                              title: Option[String],
                              description: Option[String],
                              required: Option[Boolean],
                              multiple: Option[Boolean],
                              propertyConstraints: Option[List[PropertyConstraint]]) extends ModelObjectDefinition {
  // ref must has no filter
  require(ref.filter.isDefined)
  // TODO add schema regular expression
  require(ref.path.getOrElse("").matches("/\\w+/[^/]+/\\w+"))
}

/** Models utility object */
object Model {
  /** Generate an hyperdoc reference base on a model identifier
    *
    * @param name Model name
    * @param version Model version
    * @return Hyperdoc reference
    */
  def generateModelRef(name: String, version: String): HyperdocRef =
    HyperdocRef(Store.ModelStoreRef, Some(s"/$name/$version"), None)

  /** Generate an hyperdoc reference base on an schema identifier and a model reference
    *
    * @param modelRef Model hyperdoc reference
    * @param name Schema name
    * @return Hyperdoc reference
    */
  def generateSchemaRef(modelRef: HyperdocRef, name: String) =
    HyperdocRef(Store.ModelStoreRef, Some(s"${modelRef.path.getOrElse("")}/$name"), None)

  /** Generate an hyperdoc reference base on a property identifier and a schema reference
    *
    * @param schemaRef Schema hyperdoc reference
    * @param name Property name
    * @return Hyperdoc reference
    */
  def generatePropertyRef(schemaRef: HyperdocRef, name: String): HyperdocRef =
    HyperdocRef(Store.ModelStoreRef, Some(s"${schemaRef.path.getOrElse("/")}"), Some(name))

  /** Core model 1.0 reference */
  val CoreModelRef_1_0 = HyperdocRef(Store.ModelStoreRef, Some("/core/1.0"), None)
}