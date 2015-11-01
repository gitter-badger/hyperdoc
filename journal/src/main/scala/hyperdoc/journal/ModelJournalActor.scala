package hyperdoc.journal
//
///** Companion object for the model journal.
//  *
//  * @author Ezequiel Foncubierta
//  */
//object ModelJournalActor {
//
//  import HyperdocJournalActor._
//
//  /** Model command */
//  trait ModelCommand extends HyperdocCommand
//
//  /** Command for creating a model.
//    *
//    * @param model Model entity
//    * @tparam A Model entity type
//    */
//  case class CreateModel[A <: ModelEntityDefinition](model: A) extends ModelCommand
//
//  /** Model event */
//  trait ModelEvent extends HyperdocEvent
//
//  /** Event produced when a model has been created.
//    *
//    * @param model Model entity definition
//    * @tparam A Model entity type
//    */
//  case class ModelCreated[A <: ModelEntityDefinition](model: A) extends ModelEvent
//
//  /** Event produced when a model has been removed. */
//  case object ModelRemoved extends ModelEvent
//
//  /** Model journal actor properties */
//  def props(ref: HyperdocRef): Props = Props(new ModelJournalActor(ref))
//}
//
///** Journal actor for managing models.
//  *
//  * @param href Model reference
//  * @author Ezequiel Foncubierta
//  */
//class ModelJournalActor(href: HyperdocRef) extends HyperdocJournalActor {
//
//  import HyperdocJournalActor._
//  import ModelJournalActor._
//
//  override def ref: HyperdocRef = href
//
//  override def update(event: HyperdocEvent): Unit = event match {
//    case ModelCreated(model) =>
//      context.become(enabled)
//      currentObject = model
//    case ModelRemoved =>
//      context.become(removed)
//      currentObject = RemovedObject(ref)
//  }
//
//  def initial: Receive = {
//    case CreateModel(model) =>
//      persist(ModelCreated(model))(afterEventPersisted)
//    case GetCurrentObject =>
//      respond()
//    case KillObjectJournal =>
//      context.stop(self)
//  }
//
//  def enabled: Receive = {
//    case RemoveObject =>
//      persist(ModelRemoved)(afterEventPersisted)
//    case GetCurrentObject =>
//      respond()
//    case KillObjectJournal =>
//      context.stop(self)
//    // FIXME sending CreateModel when in 'enabled' state will fail
//  }
//
//  def removed: Receive = {
//    case GetCurrentObject =>
//      respond()
//    case KillObjectJournal =>
//      context.stop(self)
//  }
//}