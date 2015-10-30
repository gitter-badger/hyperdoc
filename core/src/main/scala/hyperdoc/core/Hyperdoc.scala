package hyperdoc.core

import com.typesafe.config.{Config, ConfigFactory}
import hyperdoc.core.stores.{AuthorityStore, ModelStore, NodeStore, ObjectStore}

trait Hyperdoc {
  def config: Config = ConfigFactory.load().getConfig("hyperdoc")

  def authorityStore: AuthorityStore

  def modelStore: ModelStore

  def nodeStore: NodeStore

  def objectStore: ObjectStore
}