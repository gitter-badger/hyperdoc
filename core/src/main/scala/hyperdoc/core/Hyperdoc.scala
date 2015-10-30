package hyperdoc.core

import com.typesafe.config.Config
import hyperdoc.core.stores.{AuthorityStore, ContentObjectStore, ModelStore, NodeStore}

trait Hyperdoc {
  def config: Config

  def authorityStore: AuthorityStore

  def modelStore: ModelStore

  def nodeStore: NodeStore

  def contentObjectStore: ContentObjectStore
}