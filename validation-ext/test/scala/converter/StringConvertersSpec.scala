package scalaz.contrib
package converter

import java.text.SimpleDateFormat
import java.util.UUID

import scalaz.{Failure, Success}

import org.specs2.mutable.Specification

class StringConvertersSpec extends Specification {

  val errorMessage = "Generic Error Message"
  val fail = Failure(errorMessage)

  import string._

  "valid date" should {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    sdf.setLenient(false)
    val check = date(sdf, errorMessage)

    "be successful when date is valid" in {
      check("2012-12-21") must beEqualTo(Success(sdf.parse("2012-12-21")))
    }

    "have failure when invalid date" in {
      check("2012-13-133") must beEqualTo(fail)
    }
  }

  "successful number conversions" in {
    int(errorMessage)("5") must beEqualTo(Success(5))
    float(errorMessage)("5.5") must beEqualTo(Success(5.5f))
    double(errorMessage)("5") must beEqualTo(Success(5l))
  }

  "failure number conversions" in {
    int(errorMessage)("fifty") must beEqualTo(fail)
    float(errorMessage)("five point five") must beEqualTo(fail)
    double(errorMessage)("seven") must beEqualTo(fail)
  }

  "UUID conversion" should {
    val toUUID = uuid(errorMessage)

    "succeed when string is a UUID" in {
      List.fill(100)(UUID.randomUUID) foreach { u => toUUID(u.toString) must beEqualTo(Success(u)) }
    }

    "fail when string is not a uuid" in {
      toUUID("1234") must beEqualTo(fail)
    }
  }

}

// vim: expandtab:ts=2:sw=2
