package hyperdoc.core

/** Hyperdoc object.
  *
  * Hyperdoc manages different objects (i.e. User, Node, ModelDefinition, etc.). All those objects have common
  * characteristics, as an unique identifier within the system. Therefore, any hyperdoc object which is managed
  * within Hyperdoc, must extend this trait.
  *
  * @author Ezequiel Foncubierta
  */
trait HyperdocObject {
  /** Hyperdoc object unique reference.
    *
    * @return Hyperdoc reference
    */
  def ref: HyperdocRef
}
