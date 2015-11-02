package hyperdoc.journal

import hyperdoc.core.DummyBackendHyperdoc
import org.scalatest.{Matchers, FlatSpec}

import scala.util.Random

class ResumableProjectionSpec extends FlatSpec with Matchers {
  implicit val hyperdoc = DummyBackendHyperdoc

  "A resumable projection" should "be 0 if resume file does not exist" in {
    val resume = new ResumableProjection(Random.alphanumeric.take(10).mkString(""))
    resume.latestOffset shouldEqual 0L
    resume.latestOffset
  }
  it should "persist offset value" in {
    val id = Random.alphanumeric.take(10).mkString("")
    val resume = new ResumableProjection(id)
    resume.saveProgress(10)
    resume.latestOffset shouldEqual 10

    val resume2 = new ResumableProjection(id)
    resume2.latestOffset shouldEqual resume.latestOffset
  }
}
