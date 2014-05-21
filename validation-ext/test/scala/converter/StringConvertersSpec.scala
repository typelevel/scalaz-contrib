package scalaz.contrib
package converter

import java.text.SimpleDateFormat
import java.util.UUID

import scalaz.{Failure, Success}

import org.specs2.mutable.Specification
import org.specs2.scalaz.ValidationMatchers

class StringConvertersSpec extends Specification with ValidationMatchers {

  val errorMessage = "Generic Error Message"

  import string._

  "valid date" should {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    sdf.setLenient(false)
    val check = date(sdf, errorMessage)

    "be successful when date is valid" in {
      skipped("Fails nondeterministically")
      check("2012-12-21") must beSuccessful(sdf.parse("2012-12-21"))
    }

    "have failure when invalid date" in {
      check("2012-13-133") must beFailing(errorMessage)
    }
  }

  "successful number conversions" in {
    int(errorMessage)("5") must beSuccessful(5)
    float(errorMessage)("5.5") must beSuccessful(5.5f)
    double(errorMessage)("5") must beSuccessful(5l)
  }

  "failure number conversions" in {
    int(errorMessage)("fifty") must beFailing(errorMessage)
    float(errorMessage)("five point five") must beFailing(errorMessage)
    double(errorMessage)("seven") must beFailing(errorMessage)
  }

  "UUID conversion" should {
    val toUUID = uuid(errorMessage)

    "succeed when string is a UUID" in {
      forall(List.fill(100)(UUID.randomUUID)) { u => toUUID(u.toString) must beSuccessful(u) }
    }

    "fail when string is not a uuid" in {
      toUUID("1234") must beFailing(errorMessage)
    }
  }

}

// vim: expandtab:ts=2:sw=2
