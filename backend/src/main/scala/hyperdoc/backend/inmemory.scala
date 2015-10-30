package hyperdoc

import hyperdoc.core._
import hyperdoc.core.backend.{AuthorityBackend, ModelBackend, NodeBackend, ObjectBackend}

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.{ClassTag, classTag}

/** in memory backend */
package object inmemory {

  /** In memory authority */
  class InMemoryAuthorityBackend extends AuthorityBackend {
    private val authorities = Map[HyperdocRef, Authority]()

    override def save[A <: Authority : ClassTag](authority: A): Future[A] = Future {
      authorities += (authority.ref -> authority)
      authority
    }

    override def get[A <: Authority : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      authorities.get(ref) match {
        case Some(authority) if classTag[A] == classTag[authority.type] => Some(authority.asInstanceOf[A])
        case _ => None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {
      authorities.remove(ref)
    }
  }

  /** In memory model backend */
  class InMemoryModelBackend extends ModelBackend {
    /** registered models */
    private var modelDefinitions = Map[HyperdocRef, ModelObjectDefinition]()

    override def save[A <: ModelObjectDefinition : ClassTag](obj: A): Future[A] = Future {
      if (classTag[A] != classTag[ModelDefinition]) {
        saveModel(obj.asInstanceOf[ModelDefinition])
      } else if (classTag[A] != classTag[ModelDefinition]) {
        saveSchema(obj.asInstanceOf[SchemaDefinition])
      } else if (classTag[A] != classTag[ModelDefinition]) {
        saveProperty(obj.asInstanceOf[PropertyDefinition])
      }
      obj
    }

    private def saveModel(model: ModelDefinition): ModelDefinition = {
      modelDefinitions += (model.ref -> model)
      model.schemas.foreach(saveSchema)
      model
    }

    private def saveSchema(schema: SchemaDefinition): SchemaDefinition = {
      // TODO validate model exists
      modelDefinitions += (schema.ref -> schema)
      schema.properties.foreach(saveProperty)
      schema
    }

    private def saveProperty(property: PropertyDefinition): PropertyDefinition = {
      // TODO validate schema exists
      modelDefinitions += (property.ref -> property)
      property
    }

    override def get[A <: ModelObjectDefinition : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      modelDefinitions.get(ref) match {
        case Some(model) if classTag[A] == classTag[model.type] => Some(model.asInstanceOf[A])
        case _ => None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {
      modelDefinitions.get(ref) match {
        case Some(model: ModelDefinition) =>
          modelDefinitions.remove(ref)

          model.schemas.foreach(schema => {
            modelDefinitions.remove(schema.ref)
            schema.properties.foreach(property => {
              modelDefinitions.remove(property.ref)
            })
          })

          Some(())
        case _ => None
      }
    }
  }

  /** In memory node backend */
  class InMemoryNodeBackend extends NodeBackend {
    private val nodes = Map[HyperdocRef, Node]()

    override def save[A <: Node : ClassTag](node: A): Future[A] = Future {
      nodes += (node.ref -> node)
      node
    }

    override def get[A <: Node : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      nodes.get(ref) match {
        case Some(node) if classTag[A] == classTag[node.type] => Some(node.asInstanceOf[A])
        case _ => None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {
      nodes.remove(ref)
    }
  }

  /** In memory object backend */
  class InMemoryObjectBackend extends ObjectBackend {
    private val objects = Map[HyperdocRef, ContentObject]()

    override def save[A <: ContentObject : ClassTag](obj: A): Future[A] = Future {
      objects += (obj.ref -> obj)
      obj
    }

    override def get[A <: ContentObject : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      objects.get(ref) match {
        case Some(obj) if classTag[A] == classTag[obj.type] => Some(obj.asInstanceOf[A])
        case _ => None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {
      objects.remove(ref)
    }
  }

  implicit val inMemoryAuthorityBackend = new InMemoryAuthorityBackend
  implicit val inMemoryModelBackend = new InMemoryModelBackend
  implicit val inMemoryNodeBackend = new InMemoryNodeBackend
  implicit val inMemoryObjectBackend = new InMemoryObjectBackend
}