package scalaz.contrib
package validator

import java.util.UUID

import scalaz._
import Scalaz._

import org.specs2.mutable.Specification

class StringValidationSpec extends Specification {

  import string._

  val errorMessage = "Generic Error Message"
  val fail = Failure(errorMessage)

  "match pattern" should {
    val digitOnly = matchRegex("""^\d*$""".r, errorMessage)

    "succeed when the pattern is matched" in {
      digitOnly("123456") must beEqualTo(Success("123456"))
    }

    "fail when the pattern is not matched" in {
      digitOnly("123a") must beEqualTo(fail)
    }
  }

  "not blank validator" should {
    val failNotBlank = notBlank(errorMessage)

    "fail when the string is blank" in {
      failNotBlank("") must beEqualTo(fail)
      failNotBlank("     ") must beEqualTo(fail)
    }

    "succeed when the string is not blank" in {
      List("1", "              1     ").foreach(s => failNotBlank(s) must beEqualTo(Success(s)))
    }
  }

  "UUID validator" should {
    val toUUID = uuid(errorMessage)

    "succeed when string is a UUID" in {
      List.fill(100)(UUID.randomUUID) foreach { u => toUUID(u.toString) must beEqualTo(Success(u)) }
    }

    "fail when string is not a uuid" in {
      toUUID("1234") must beEqualTo(fail)
    }
  }

}
