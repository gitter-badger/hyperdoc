package hyperdoc.core

/** Authority */
sealed trait Authority extends HyperdocObject {
  // ref should be in the 'authority' store
  require(ref.store.equals(Store.AuthorityStoreRef))
  // and has no filter
  require(ref.filter.isEmpty)
}

/** User
  *
  * @param ref Node reference
  * @param username Username
  * @param profile User profile
  * @param accounts User accounts
  * @param groups User groups
  */
case class User(ref: HyperdocRef,
                username: String,
                profile: Profile,
                accounts: Map[String, Account],
                groups: List[Group]) extends Authority {
  // TODO add user regular expression
  require(ref.path.getOrElse("").matches("/user/\\w+"))
}

/** User profile
  *
  * @param email User email
  * @param firstName User first name
  * @param lastName User last name
  * @param website User web site
  */
case class Profile(email: String,
                   firstName: String,
                   lastName: String,
                   website: Option[String])

/** User accounts
  *
  * @param provider Account provider
  * @param properties Account properties
  */
case class Account(provider: AccountProvider,
                   properties: Map[String, Property])


/** Account provider */
trait AccountProvider {
  def name: String
}

/** Group
  *
  * @param ref Hyperdoc reference
  * @param name Group name
  * @param description group description
  */
case class Group(ref: HyperdocRef,
                 name: String,
                 description: Option[String]) extends Authority {
  // TODO add group regular expression
  require(ref.path.getOrElse("").matches("/group/\\w+"))
}

/** Users utility object */
object Authority {
  /** Generate an hyperdoc reference base on a group name
    *
    * @param name Group name
    * @return Hyperdoc reference
    */
  def generateGroupRef(name: String): HyperdocRef =
    HyperdocRef(Store.AuthorityStoreRef, Some(s"/group/$name"), None)

  /** Generate an hyperdoc reference base on a user name
    *
    * @param username Group name
    * @return Hyperdoc reference
    */
  def generateUserRef(username: String): HyperdocRef =
    HyperdocRef(Store.AuthorityStoreRef, Some(s"/user/$username"), None)

  private val emptyAccounts = Map[String, Account]()

  /** Monitor user */
  private val MonitorUserProfile = Profile("monitor@hyperdoc", "Monitor", "Hyperdoc", None)
  val MonitorUserRef = HyperdocRef(Store.AuthorityStoreRef, Some("/user/monitor"), None)
  val MonitorUser = User(MonitorUserRef, "monitor", MonitorUserProfile, emptyAccounts, Nil)

  /** System user */
  private val SystemUserProfile = Profile("system@hyperdoc", "System", "Hyperdoc", None)
  val SystemUserRef = HyperdocRef(Store.AuthorityStoreRef, Some("/user/system"), None)
  val SystemUser = User(SystemUserRef, "system", SystemUserProfile, emptyAccounts, Nil)
}