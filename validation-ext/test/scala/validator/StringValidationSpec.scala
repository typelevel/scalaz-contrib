package scalaz.contrib
package validator

import java.text.SimpleDateFormat
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

  "luhn check" should {
    val check = luhn(errorMessage)

    "success when string checks out" in {
      List(
        "5105105105105100", "5454545454545454", "5555555555554444", "4222222222222", "4111111111111111",
        "4012888888881881", "378282246310005", "371449635398431", "378734493671000", "38520000023237", "30569309025904",
        "6011111111111117", "6011000990139424", "3530111333300000", "3566002020360505"
      ) foreach { num =>
        check(num) must beEqualTo(Success(num))
      }
    }

    "fail for invalid strings" in {
      List(
        "4105105105105100", "5554545454545454", "5545555555554444", "4222322222222", "4111116111111111",
        "4012888878881881", "378282246300005", "371449635398432", "378734493671030", "38520000023231", "30569309125904",
        "6011111111114117", "6011000990132424", "3530111333303000", "3566002020260505"
      ) foreach { num =>
        check(num) must beEqualTo(fail)
      }
    }
  }


}

// vim: expandtab:ts=2:sw=2
