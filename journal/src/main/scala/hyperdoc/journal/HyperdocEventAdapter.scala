package hyperdoc.journal

import akka.persistence.journal.{Tagged, WriteEventAdapter}

class HyperdocEventAdapter extends WriteEventAdapter {

  override def toJournal(event: Any): Any = {
    event match {
      case _: AuthorityEvent => Tagged(event, Set("authority"))
      case _: ContentObjectEvent => Tagged(event, Set("object"))
      case _: NodeEvent => Tagged(event, Set("node"))
      case _ => event
    }
  }

  override def manifest(event: Any): String = ""
}
