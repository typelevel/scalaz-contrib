package validation

import org.specs2.mutable.Specification
import scalaz.contrib.validation.Validation
import scalaz.{NonEmptyList, Failure, Success}
import java.util.Date

/**
 * User: travis.stevens@gaiam.com
 * Date: 12/7/12
 */
class ValidationSpec extends Specification with Validation {

  val errorMessage = "Generic Error Message"

  "max validation" should {


    "pass for int data types with implicit Ordering" in {
      val max10 = max(10, errorMessage)

      max10(1) must beEqualTo(new Success(1)) and
        (max10(100) must beEqualTo(new Failure(NonEmptyList(errorMessage)))) and
        ((1 to 10).map(x => max10(x) must beEqualTo(new Success(x)))).reduceLeft(_ and _) and
        ((11 to 100).map(x => max10(x) must beEqualTo(new Failure(NonEmptyList(errorMessage))))).reduceLeft(_ and _)
    }

    "pass for floats with implicit ordering" in {

      val max10Float = max(10.0, errorMessage)
      max10Float(7.0) must beEqualTo(new Success(7.0)) and
        (max10Float(12) must beEqualTo(new Failure(NonEmptyList(errorMessage))))
    }

    "pass for chars with implicit ordering" in {
      val maxIsx = max("x", errorMessage)

      maxIsx("a") must beEqualTo(new Success("a")) and
        (maxIsx("z") must beEqualTo(new Failure(NonEmptyList(errorMessage))))
    }

    "pass for dates with implicit ordering" in {

      val earlier = new Date(1354924394000l)
      val later = new Date(1354924395000l)
      val maxDate = max(new Date(1354924394521l), errorMessage)
      maxDate(earlier) must beEqualTo(Success(earlier)) and
        (maxDate(later) must beEqualTo(new Failure(NonEmptyList(errorMessage))))
    }

  }

  "min validation" should {
    val min10 = min(10,errorMessage)
    "pass for values greater than or equal to min" in {
      (10 to 100).map(x => min10(x) must beEqualTo(new Success(x)))
      (-110 to 9).map(x => min10(x) must beEqualTo(new Failure(NonEmptyList(errorMessage))))
    }
  }

}
