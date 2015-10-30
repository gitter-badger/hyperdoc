package hyperdoc.core.stores

import hyperdoc.core._
import hyperdoc.core.backend.AuthorityBackend

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success, Try}

/** Manager for [[hyperdoc.core.Authority]] entities
  *
  * @param authorityBackend Authority backend
  *
  * @author Ezequiel Foncubierta
  */
class AuthorityStore(implicit authorityBackend: AuthorityBackend) extends Store[Authority] {
  /** Save an authority.
    *
    * @param authority Authority
    * @tparam A Authority type
    * @return Saved authority
    */
  def save[A <: Authority : ClassTag](authority: A, replace: Boolean = false): Try[A] =
    try {
      if (!replace && Await.result(authorityBackend.get(authority.ref), 1.seconds).isDefined) {
        Failure(new Exception(s"Authority '${authority.ref}' already exists"))
      } else {
        Success(Await.result(authorityBackend.save(authority), 1.seconds))
      }
    } catch {
      case e: Throwable => Failure(e)
    }


  /** Remove an authority.
    *
    * @param ref Hyperdoc reference
    */
  def remove(ref: HyperdocRef): Unit =
    Await.result(authorityBackend.remove(ref), 1.seconds)

  /** Get an authority.
    *
    * @param ref Hyperdoc reference
    * @tparam A Authority type
    * @return Authority
    */
  def get[A <: Authority : ClassTag](ref: HyperdocRef): Option[A] =
    try {
      Await.result(authorityBackend.get[A](ref), 1.seconds) match {
        case Some(authority) if classTag[A] == classTag[authority.type] => Some(authority)
        case _ => None
      }
    } catch {
      case e: Throwable => None
    }

  /** Get a user.
    *
    * @param ref Hyperdoc reference
    * @return User
    */
  def getUser(ref: HyperdocRef): Option[User] = get[User](ref)

  /**
   * Get a group.
   *
   * @param ref Hyperdoc reference
   * @return Group
   */
  def getGroup(ref: HyperdocRef): Option[Group] = get[Group](ref)

  /**
   * Get an authority.
   *
   * @param ref Hyperdoc reference
   * @return Authority
   */
  override def apply(ref: HyperdocRef): Option[Authority] = get[Authority](ref)
}
