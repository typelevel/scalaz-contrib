package scalaz.contrib
package validator

import java.util.Date

import scalaz._
import Scalaz._

import org.specs2.mutable.Specification

class BasicValidationSpec extends Specification {

  import basic._

  val errorMessage = "Generic Error Message"

  "atMost validator" should {
    val atMost10 = atMost(10, errorMessage)

    "work for Int with implicit Ordering" in {
      1 to 10   foreach { x => atMost10(x) must beNone }
      11 to 100 foreach { x => atMost10(x) must beSome(errorMessage) }
    }
  }

  "atLeast validator" should {
    val atLeast10 = atLeast(10, errorMessage)

    "pass for values greater than or equal to min" in {
      (10 to 100).map(x => atLeast10(x) must beNone)
    }
    
    "fail for values less than min" in {
      (-110 to 9).map(x => atLeast10(x) must beSome(errorMessage))
    }
  }

  def equalTo5Test(f: Validator[String, Int]) = {
    "pass for equal values" in {
      f(5) must beNone
    }

    "fail for values less than the value" in {
      (0 to 4).map(x => f(x) must beSome(errorMessage))
    }

    "fail for values more than the value" in {
      (6 to 100).map(x => f(x) must beSome(errorMessage))
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
      (0 to 4).map(range5to10(_) must beSome(errorMessage))
    }

    "pass if in range" in {
      (5 to 10).map(n => range5to10(n) must beNone)
    }

    "fail if greater than range" in {
      (11 to 100).map(range5to10(_) must beSome(errorMessage))
    }
  }

  "max size" should {
    val max2 = maxLength(2, errorMessage)

    "pass when size is <= maxSize" in {
      max2(List(1)) must beNone
    }

    "fail when size is > maxSize" in {
      max2(List(1, 2, 3)) must beSome(errorMessage)
    }
  }

  "not empty" should {
    "pass when object is empty" in {
      notEmpty(errorMessage)(List(1)) must beNone
    }
    "fail when object is empty" in {
      notEmpty(errorMessage)(List()) must beSome(errorMessage)
    }
  }

  "length is" should {
    val check = lengthIs(_ === 2, errorMessage)
    "pass when length is as specified" in {
      check(List(1,2)) must beNone
    }
    "fail when length is not as specified" in {
      check(List(1,2,3)) must beSome(errorMessage)
    }
  }

}

// vim: expandtab:ts=2:sw=2
