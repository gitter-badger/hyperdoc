package hyperdoc.core.utils

import org.scalatest.{Matchers, FlatSpec}

import org.scalatest.TryValues._
import scala.util.{Failure, Success}

/**
 * Tests for [[hyperdoc.core.utils]]
 */
class UtilsSpec extends FlatSpec with Matchers {
  "A List[Try[A]]" should "be transformed into a Try[List[A]]" in {
    // test list of success
    val l1 = List(Success(1), Success(2), Success(3))
    l1.sequence shouldEqual Success(List(1, 2, 3))

    // test list with a failure
    val l2 = List(Success(1), Success(2), Failure(new Exception("test error")))
    l2.sequence.failure.exception should have message "test error"
  }
}