package hyperdoc.core.backend

import hyperdoc.core._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.ClassTag

/** dummy backend for testing */
object DummyBackend {

  /** dummy authority backend */
  class DummyAuthorityBackend extends AuthorityBackend {
    override def save[A <: Authority : ClassTag](authority: A): Future[A] = Future {
      authority
    }

    override def get[A <: Authority : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      if (HyperdocTestObjects.TEST_USER_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_USER.asInstanceOf[A])
      } else if (HyperdocTestObjects.TEST_GROUP_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_GROUP.asInstanceOf[A])
      } else {
        None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {}
  }

  /** dummy model backend */
  class DummyModelBackend extends ModelBackend {
    override def save[A <: ModelObjectDefinition : ClassTag](model: A): Future[A] = Future {
      model
    }

    override def get[A <: ModelObjectDefinition : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      if (HyperdocTestObjects.TEST_MODEL_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_MODEL_DEFINITION.asInstanceOf[A])
      } else if (HyperdocTestObjects.TEST_SCHEMA_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_SCHEMA_DEFINITION.asInstanceOf[A])
      } else if (HyperdocTestObjects.TEST_STRING_PROPERTY_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_STRING_PROPERTY_DEFINITION.asInstanceOf[A])
      } else {
        None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {}
  }

  /** dummy node backend */
  class DummyNodeBackend extends NodeBackend {
    override def save[A <: Node : ClassTag](node: A): Future[A] = Future {
      node
    }

    override def get[A <: Node : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      if (HyperdocTestObjects.TEST_CONTAINER_NODE_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_CONTAINER_NODE.asInstanceOf[A])
      } else if (HyperdocTestObjects.TEST_CONTENT_NODE_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_CONTENT_NODE.asInstanceOf[A])
      } else {
        None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {}
  }

  /** dummy object backend */
  class DummyObjectBackend extends ObjectBackend {
    override def save[A <: ContentObject : ClassTag](obj: A): Future[A] = Future {
      obj
    }

    override def get[A <: ContentObject : ClassTag](ref: HyperdocRef): Future[Option[A]] = Future {
      if (HyperdocTestObjects.TEST_BINARY_OBJECT_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_BINARY_OBJECT.asInstanceOf[A])
      } else if (HyperdocTestObjects.TEST_TEXT_OBJECT_REF.equals(ref)) {
        Some(HyperdocTestObjects.TEST_TEXT_OBJECT.asInstanceOf[A])
      } else {
        None
      }
    }

    override def remove(ref: HyperdocRef): Future[Unit] = Future {}
  }

  implicit val dummyAuthorityBackend = new DummyAuthorityBackend
  implicit val dummyModelBackend = new DummyModelBackend
  implicit val dummyNodeBackend = new DummyNodeBackend
  implicit val dummyObjectBackend = new DummyObjectBackend
}