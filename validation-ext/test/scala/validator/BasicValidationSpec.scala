package scalaz.contrib
package validator

import java.util.Date

import scalaz._
import Scalaz._

import org.specs2.mutable.Specification

class BasicValidationSpec extends Specification {

  import basic._

  val errorMessage = "Generic Error Message"
  val fail = Failure(errorMessage)

  "atMost validator" should {
    val atMost10 = atMost(10, errorMessage)

    "work for Int with implicit Ordering" in {
      1 to 10   foreach { x => atMost10(x) must beEqualTo(Success(x)) }
      11 to 100 foreach { x => atMost10(x) must beEqualTo(fail) }
    }
  }

  "atLeast validator" should {
    val atLeast10 = atLeast(10, errorMessage)

    "pass for values greater than or equal to min" in {
      (10 to 100).map(x => atLeast10(x) must beEqualTo(Success(x)))
    }
    
    "fail for values less than min" in {
      (-110 to 9).map(x => atLeast10(x) must beEqualTo(fail))
    }
  }

  def equalTo5Test(f: Validator[String, Int]) = {
    "pass for equal values" in {
      f(5) must beEqualTo(Success(5))
    }

    "fail for values less than the value" in {
      (0 to 4).map(x => f(x) must beEqualTo(fail))
    }

    "fail for values more than the value" in {
      (6 to 100).map(x => f(x) must beEqualTo(fail))
    }
  }

  "equalA (universal equality)" should {
    equalTo5Test(equalA(5, errorMessage))
  }

  "equal (scalaz.Equal)" should {
    equalTo5Test(equal(5,errorMessage))
  }

  "range validator" should {
    val range5to10 = range(5,10, errorMessage)

    "fail if less than range" in {
      (0 to 4).map(range5to10(_) must beEqualTo(fail))
    }
    "pass if in range" in {
      (5 to 10).map(n => range5to10(n) must beEqualTo(Success(n)))
    }
    "fail if greater than range" in {
      (11 to 100).map(range5to10(_) must beEqualTo(fail))
    }
  }

  "max size" should {
    val max2 = maxSize(2, errorMessage)

    "pass when size is <= maxSize" in {
      max2(List(1)) must beEqualTo(Success(List(1)))
    }

    "fail when size is > maxSize" in {
      max2(List(1, 2, 3)) must beEqualTo(fail)
    }
  }



}
