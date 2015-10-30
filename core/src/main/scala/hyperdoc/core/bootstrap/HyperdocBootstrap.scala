package hyperdoc.core.bootstrap

import scala.util.Try

/** An hyperdoc bootstrap is executed when the server is starting up.
  *
  * @author Ezequiel Foncubierta
  */
trait HyperdocBootstrap {
  /** Bootstrapping.
    *
    * @return Unit
    */
  def bootstrap: Try[Unit]
}
