package hyperdoc.core

import scala.util.Try

/** Utility object.
  *
  * @author Ezequiel Foncubierta
  */
package object utils {
  /**
   * From: Paypal's cascade
   *
   * Implicit wrapper for List[Try[A]] objects.
   *
   * @param listTry the list to be wrapped
   * @tparam A the success type of the Try
   */
  implicit class ListTrySequencer[A](listTry: List[Try[A]]) {
    /**
     * Transform a List[Try[A]] to Try[List[A]]
     *
     * @return List transformed
     */
    def sequence: Try[List[A]] = {
      def addTry(builder: Try[Vector[A]], next: Try[A]): Try[Vector[A]] = builder.flatMap(t => next.map(t :+ _))
      listTry.foldLeft(Try(Vector[A]()))(addTry).map(_.toList)
    }
  }
}
