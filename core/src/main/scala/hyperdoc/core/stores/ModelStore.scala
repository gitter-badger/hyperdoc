package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.ModelBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Manager for [[hyperdoc.core.ModelEntityDefinition]] entities.
  *
  * @param modelBackend Model backend
  *
  * @author Ezequiel foncubierta
  */
class ModelStore(implicit modelBackend: ModelBackend) extends Store[ModelEntityDefinition] {
  /** Save a model definition.
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
   * Get a model entity.
   *
   * @param ref Model entity reference
   * @tparam A Model entity type
   * @return Model entity
   */
  def getModelEntity[A <: ModelEntityDefinition : ClassTag](ref: HyperdocRef): Option[A] = try {
    Await.result(modelBackend.get[A](ref), 1.seconds) match {
      case Some(modelObject) if classTag[A] == classTag[modelObject.type] => Some(modelObject.asInstanceOf[A])
      case _ => None
    }
  } catch {
    case e: Throwable => None
  }

  /** Get a model definition.
    *
    * @param ref Model reference
    * @return Model definition
    */
  def getModel(ref: HyperdocRef): Option[ModelDefinition] =
    getModelEntity[ModelDefinition](ref)

  /** Get an schema definition.
    *
    * @param ref Schema reference
    * @return Schema definition
    */
  def getSchema(ref: HyperdocRef): Option[SchemaDefinition] =
    getModelEntity[SchemaDefinition](ref)

  /** Get a property definition.
    *
    * @param ref Property reference
    * @return Property definition
    */
  def getProperty(ref: HyperdocRef): Option[PropertyDefinition] =
    getModelEntity[PropertyDefinition](ref)

  /** Get a model entity.
    *
    * @param ref Model entity reference
    * @return Model entity
    */
  def apply(ref: HyperdocRef): Option[ModelEntityDefinition] = getModelEntity(ref)
}