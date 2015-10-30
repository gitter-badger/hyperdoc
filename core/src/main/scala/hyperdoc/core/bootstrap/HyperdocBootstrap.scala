package hyperdoc.core.bootstrap

import scala.util.Try

/** Hyperdoc bootstrap */
trait HyperdocBootstrap {
  def bootstrap: Try[Unit]
}
