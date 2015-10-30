package hyperdoc.core

/** An authority object represent users, groups and any other organisational entity within a corporation.
  * Authorities live in the 'authority' hyperdoc store, like hyperdoc://authority/user/system or
  * hyperdoc://authority/group/administrators.
  *
  * @author Ezequiel Foncubierta
  */
sealed trait Authority extends HyperdocObject {
  // ref should be in the 'authority' store
  require(ref.store.equals(Store.AuthorityStoreRef))
  // and has no filter
  require(ref.filter.isEmpty)
}

/** An hyperdoc user is uniquely identify by its hyperdoc reference and username. It also contains information
  * about the user profile, accounts and groups within Hyperdoc.
  *
  * @param ref Node reference
  * @param username Username
  * @param profile User profile
  * @param accounts User accounts
  * @param groups User groups
  *
  * @author Ezequiel Foncubierta
  */
case class User(ref: HyperdocRef,
                username: String,
                profile: Profile,
                accounts: Map[String, Account],
                groups: List[Group]) extends Authority {
  // TODO add user name regular expression
  require(ref.path.getOrElse("").matches("/user/\\w+"))
}

/** An user has a profile that describes the user to other peers.
  *
  * @param email User email
  * @param firstName User first name
  * @param lastName User last name
  * @param website User web site
  *
  * @author Ezequiel Foncubierta
  */
case class Profile(email: String,
                   firstName: String,
                   lastName: String,
                   website: Option[String])

/** An user can have multiple accounts. One per account provider.
  *
  * @param provider Account provider
  * @param properties Account properties
  *
  * @see [[hyperdoc.core.AccountProvider]]
  *
  * @author Ezequiel Foncubierta
  */
case class Account(provider: AccountProvider,
                   properties: Map[String, PropertyValue])


/** Hyperdoc could manage multiple account providers (i.e. Database, Twitter, Facebook, etc.). Any
  * account provider must extend this trait.
  */
trait AccountProvider {
  /**
   * Account provider name
   *
   * @return Account provider name
   */
  def name: String
}

/** A group is used, mainly, to grant permissions to a group of people. However, there are many other uses like
  * sending emails to a group of people, etc.
  *
  * @param ref Hyperdoc reference
  * @param name Group name
  * @param description group description
  *
  * @author Ezequiel Foncubierta
  */
case class Group(ref: HyperdocRef,
                 name: String,
                 description: Option[String]) extends Authority {
  // TODO add group name regular expression
  require(ref.path.getOrElse("").matches("/group/\\w+"))
}

/** Authorities utility object */
object Authority {
  /** Generate an hyperdoc reference base on a group name.
    *
    * @param name Group name
    * @return Hyperdoc reference
    */
  def generateGroupRef(name: String): HyperdocRef =
    HyperdocRef(Store.AuthorityStoreRef, Some(s"/group/$name"), None)

  /** Generate an hyperdoc reference base on a user name.
    *
    * @param username Group name
    * @return Hyperdoc reference
    */
  def generateUserRef(username: String): HyperdocRef =
    HyperdocRef(Store.AuthorityStoreRef, Some(s"/user/$username"), None)

  /** Monitor user */
  private val MonitorUserProfile = Profile("monitor@hyperdoc", "Monitor", "Hyperdoc", None)
  val MonitorUserRef = HyperdocRef(Store.AuthorityStoreRef, Some("/user/monitor"), None)
  val MonitorUser = User(MonitorUserRef, "monitor", MonitorUserProfile, Map(), Nil)

  /** System user */
  private val SystemUserProfile = Profile("system@hyperdoc", "System", "Hyperdoc", None)
  val SystemUserRef = HyperdocRef(Store.AuthorityStoreRef, Some("/user/system"), None)
  val SystemUser = User(SystemUserRef, "system", SystemUserProfile, Map(), Nil)
}