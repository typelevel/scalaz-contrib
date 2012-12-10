package validation

import org.specs2.mutable.Specification
import scalaz.contrib.validation.Validation
import scalaz._
import Scalaz._
import java.util.Date

/**
 * User: travis.stevens@gaiam.com
 * Date: 12/7/12
 */
class ValidationSpec extends Specification with Validation {

  val errorMessage = "Generic Error Message"
  val failNel = new Failure(NonEmptyList(errorMessage))

  "max validation" should {


    "pass for int data types with implicit Ordering" in {
      val max10 = max(10, errorMessage)

      max10(1) must beEqualTo(new Success(1)) and
        (max10(100) must beEqualTo(failNel)) and
        ((1 to 10).map(x => max10(x) must beEqualTo(new Success(x)))).reduceLeft(_ and _) and
        ((11 to 100).map(x => max10(x) must beEqualTo(failNel))).reduceLeft(_ and _)
    }

    "pass for floats with implicit ordering" in {

      val max10Float = max(10.0, errorMessage)
      max10Float(7.0) must beEqualTo(new Success(7.0)) and
        (max10Float(12) must beEqualTo(failNel))
    }

    "pass for chars with implicit ordering" in {
      val maxIsx = max("x", errorMessage)

      maxIsx("a") must beEqualTo(new Success("a")) and
        (maxIsx("z") must beEqualTo(failNel))
    }

    "pass for dates with implicit ordering" in {

      val earlier = new Date(1354924394000l)
      val later = new Date(1354924395000l)
      val maxDate = max(new Date(1354924394521l), errorMessage)
      maxDate(earlier) must beEqualTo(Success(earlier)) and
        (maxDate(later) must beEqualTo(failNel))
    }

  }

  "min validation" should {
    val min10 = min(10,errorMessage)
    "pass for values greater than or equal to min" in {
      (10 to 100).map(x => min10(x) must beEqualTo(new Success(x)))
    }
    
    "fail for values less than min" in {
      (-110 to 9).map(x => min10(x) must beEqualTo(failNel))
    }
  }

  def equalTo5Test(f: Valid[String,Int]) = {
    "pass for equal values" in {
      f(5) must beEqualTo(new Success(5))
    }

    "fail for values less than the value" in {
      (0 to 4).map(x => f(x) must beEqualTo(failNel))
    }

    "fail for values more than the value" in {
      (6 to 100).map(x => f(x) must beEqualTo(failNel))
    }

  }

  "equalO (Ordered)" should {
    equalTo5Test(equalO(5, errorMessage))
  }

  "equal (Equal)" should {
    equalTo5Test(equal(5,errorMessage))
  }

  "range validation" should {
    val range5to10 = range(5,10, errorMessage)

    "fail if less than range" in {
      (0 to 4).map(range5to10(_) must beEqualTo(failNel))
    }
    "pass if in range" in {
      (5 to 10).map(n => range5to10(n) must beEqualTo(new Success(n)))
    }
    "fail if greater than range" in {
      (11 to 100).map(range5to10(_) must beEqualTo(failNel))
    }
  }

  "max size" should {
    val max2 = maxSize[Int, List, String](2, errorMessage)

    "pass when size is <= maxSize" in {
      max2(List(1)) must beEqualTo(new Success(List(1)))
    }

    "fail when size is > maxSize" in {
      max2(List(1,2,3)) must beEqualTo(failNel)
    }

  }

}
