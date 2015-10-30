package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.ModelBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Model store */
class ModelStore(implicit modelBackend: ModelBackend) extends Store[ModelObjectDefinition] {
  /** Save a model definition
    *
    * @param model Model definition
    * @param replace Flag to replace existing model definition
    * @return Model definition
    */
  def saveModel(model: ModelDefinition, replace: Boolean = false): Try[ModelDefinition] = try {
    if (!replace && Await.result(modelBackend.get[ModelDefinition](model.ref), 1.seconds).isDefined) {
      Failure(new Exception(s"Model '${model.name}' has already been defined"))
    } else {
      Success(Await.result(modelBackend.save(model), 1.seconds))
    }
  } catch {
    case e: Throwable => Failure(e)
  }

  /**
   * Get a generic definition
   *
   * @param ref Definition reference
   * @tparam A Definition type
   * @return Definition
   */
  def getDefinition[A <: ModelObjectDefinition : ClassTag](ref: HyperdocRef): Option[A] = try {
    Await.result(modelBackend.get[A](ref), 1.seconds) match {
      case Some(modelObject) if classTag[A] == classTag[modelObject.type] => Some(modelObject.asInstanceOf[A])
      case _ => None
    }
  } catch {
    case e: Throwable => None
  }

  /** Get a model definition
    *
    * @param ref Model reference
    * @return Model definition
    */
  def getModelDefinition(ref: HyperdocRef): Option[ModelDefinition] =
    getDefinition[ModelDefinition](ref)

  /** Get an schema definition
    *
    * @param ref Schema reference
    * @return Schema definition
    */
  def getSchemaDefinition(ref: HyperdocRef): Option[SchemaDefinition] =
    getDefinition[SchemaDefinition](ref)

  /** Get a property definition
    *
    * @param ref Property reference
    * @return Property definition
    */
  def getPropertyDefinition(ref: HyperdocRef): Option[PropertyDefinition] =
    getDefinition[PropertyDefinition](ref)

  /** Get any model object definition
    *
    * @param ref Definition reference
    * @return Definition
    */
  def apply(ref: HyperdocRef): Option[ModelObjectDefinition] = ref match {
    case HyperdocRef(_, Some(_), Some(_)) => getPropertyDefinition(ref) //get property
    case HyperdocRef(_, Some(path), None) if path.count(_ == '/') == 2 => getModelDefinition(ref) // get model
    case HyperdocRef(_, Some(path), None) => getSchemaDefinition(ref) // get schema
    case _ => None
  }
}