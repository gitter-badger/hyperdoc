package hyperdoc.core.backend

import hyperdoc.core.{HyperdocObject, HyperdocRef}

import scala.concurrent.Future
import scala.reflect.ClassTag

/** Basic CRUD operations for backends
  *
  * @tparam T Hyperdoc object type
  */
trait CrudOperationsBackend[T <: HyperdocObject] {
  /** Save an hyperdoc object
    *
    * @param obj Hyperdoc object
    * @tparam A Hyperdoc object subtype
    * @return Saved hyperdoc object
    */
  def save[A <: T : ClassTag](obj: A): Future[A]

  /** Get an hyperdoc object
    *
    * @param ref Hyperdoc object reference
    * @tparam A Hyperdoc object subtype
    * @return Hyperdoc object
    */
  def get[A <: T : ClassTag](ref: HyperdocRef): Future[Option[A]]

  /** Remove an hyperdoc object
    *
    * @param ref Hyperdoc object reference
    * @return Unit
    */
  def remove(ref: HyperdocRef): Future[Unit]
}
