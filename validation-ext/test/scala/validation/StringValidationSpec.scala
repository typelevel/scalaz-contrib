package validation

import org.specs2.mutable.Specification
import scalaz.contrib.validation.StringValidation
import scalaz._
import Scalaz._
import java.util.UUID

/**
 */
class StringValidationSpec extends Specification {

  val errorMessage = "Generic Error Message"
  val failNel = new Failure(NonEmptyList(errorMessage))

  val stringValidation = new StringValidation {}
  import stringValidation._

  "match pattern" should {

    val digitOnly = matchRegex("""^\d*$""".r, errorMessage)

    "succeed when the pattern is matched" in {
      digitOnly("123456") must beEqualTo(new Success("123456"))
    }

    "fail when the pattern is not matched" in {
      digitOnly("123a") must beEqualTo(failNel)
    }

  }

  "not blank validation" should {

    val failNotBlank = notBlank(errorMessage)

    "fail when the string is blank" in {
      failNotBlank("") must beEqualTo(failNel) and
        (failNotBlank("     ") must beEqualTo(failNel))
    }

    "succeed when the string is not blank" in {
      List("1", "              1     ")
        .map(s => failNotBlank(s) must beEqualTo(new Success(s)))
        .reduceLeft(_ and _)

    }
  }

  "uuid validation" should {
    val toUuid = uuid(errorMessage)
    "succeed when string is a uuid" in {
      (1 to 100).map(_ => UUID.randomUUID).map(u => toUuid(u.toString) must beEqualTo(new Success(u)))
    }

    "fail when string is not a uuid" in {
      toUuid("1234") must beEqualTo(failNel)
    }
  }

}
