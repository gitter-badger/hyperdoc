package hyperdoc.core.bootstrap

import java.io.File

import argonaut.Argonaut._
import argonaut.CodecJson
import hyperdoc.core._
import hyperdoc.core.utils._

import scala.io.Source
import scala.util.{Failure, Success, Try}
import scalaz.{-\/, \/-}

/** Model parser */
object ModelParser {

  /** Property class to parsing the JSON object into
    *
    * @param name Property name
    * @param ptype Property type
    * @param title Property title
    * @param description Property description
    * @param required Property required flag
    * @param multiple Property multivalued flag
    */
  sealed case class JProperty(name: String, ptype: String, title: Option[String], description: Option[String], required: Option[Boolean], multiple: Option[Boolean])

  /** Argonaut parsing configuration for property objects */
  private object JProperty {
    implicit def PropertyCodecJson: CodecJson[JProperty] =
      casecodec6(JProperty.apply, JProperty.unapply)("name", "type", "title", "description", "required", "multiple")
  }

  /** Schema class to parsing the JSON object into
    *
    * @param name Schema name
    * @param properties Schema properties
    * @param title Schema title
    * @param description Schema description
    */
  sealed case class JSchema(name: String, properties: List[JProperty], title: Option[String], description: Option[String])

  /** Argonaut parsing configuration for schema objects */
  object JSchema {
    implicit def SchemaCodecJson: CodecJson[JSchema] =
      casecodec4(JSchema.apply, JSchema.unapply)("name", "properties", "title", "description")
  }

  /** Model class to parsing the JSON object into
    *
    * @param name Model name
    * @param version Model version
    * @param schemas Model schemas
    * @param title Model title
    */
  sealed case class JModel(name: String, version: String, schemas: List[JSchema], title: Option[String])

  /** Argonaut parsing configuration for model objects */
  object JModel {
    implicit def ModelCodecJson: CodecJson[JModel] =
      casecodec4(JModel.apply, JModel.unapply)("name", "version", "schemas", "title")
  }

  /** Allowed property types */
  private val ALLOWED_PROPERTY_TYPES = List("string", "integer", "double", "boolean", "reference")

  /** Import model from a JSON file
    *
    * @param file JSON file
    * @return Exception or model definition
    */
  def importFromJsonFile(file: File): Try[ModelDefinition] = {
    try {
      importFromJson(Source.fromFile(file).mkString)
    } catch {
      case e: Throwable => Failure(e)
    }
  }

  /** Import model from a JSON string
    *
    * @param json JSON String
    * @return Exception or model definition
    */
  def importFromJson(json: String): Try[ModelDefinition] = {
    val model = json.decodeEither[JModel] match {
      case \/-(m) => Success(m)
      case -\/(m) => Failure(new Exception(m))
    }

    for {
      m <- model
      modelDefinition <- parseModel(m)
    } yield modelDefinition
  }

  /** Parse a model
    *
    * @param model Model
    * @return Exception or model definition
    */
  private def parseModel(model: JModel): Try[ModelDefinition] = {
    // model reference
    val modelRef = Model.generateModelRef(model.name, model.version)

    // parse model schemas
    val result = model.schemas.map(schema => parseSchema(modelRef, schema))
    result.sequence match {
      case Success(schemaDefinitions) =>
        val modelDefinition = ModelDefinition(modelRef, model.name, model.version, schemaDefinitions, model.title)
        Success(modelDefinition)
      case Failure(e) => Failure(e)
    }
  }

  /** Parse an schema
    *
    * @param ref Model reference
    * @param schema Schema
    * @return Exception or schema definition
    */
  private def parseSchema(ref: HyperdocRef, schema: JSchema): Try[SchemaDefinition] = {
    // schema reference
    val schemaRef = Model.generateSchemaRef(ref, schema.name)

    // parse schema properties
    val result = schema.properties.map(property => parseProperty(schemaRef, property))
    result.sequence match {
      case Success(propertyDefinitions) =>
        val schemaDefinition = SchemaDefinition(schemaRef, schema.name, propertyDefinitions, schema.title, schema.description)
        Success(schemaDefinition)
      case Failure(e) => Failure(e)
    }
  }

  /** Parse a property
    *
    * @param ref Schema reference
    * @param property Property
    * @return Exception or property definition
    */
  private def parseProperty(ref: HyperdocRef, property: JProperty): Try[PropertyDefinition] = {
    // property reference
    val propertyRef = Model.generatePropertyRef(ref, property.name)

    // validate property type
    if (!ALLOWED_PROPERTY_TYPES.contains(property.ptype)) {
      return Failure(new Exception(s"Property '$propertyRef' has an invalid type '${property.ptype}'"))
    }

    // validate property type
    val propertyType = property.ptype match {
      case "string" => StringPropertyType
      case "integer" => IntegerPropertyType
      case "double" => DoublePropertyType
      case "boolean" => BooleanPropertyType
      case "reference" => ReferencePropertyType
    }

    // create property definition
    val propertyDefinition = PropertyDefinition(propertyRef, property.name, propertyType, property.title,
      property.description, property.required, property.multiple, None)
    Success(propertyDefinition)
  }
}
