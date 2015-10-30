package hyperdoc.core.backend

import hyperdoc.core.ModelEntityDefinition

/** Manages model objects in the backend.
  *
  * @author Ezequiel Foncubierta
  */
trait ModelBackend extends CrudOperationsBackend[ModelEntityDefinition] {
}
