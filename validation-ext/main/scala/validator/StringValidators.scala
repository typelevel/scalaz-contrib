package scalaz.contrib
package validator

import annotation.tailrec
import util.matching.Regex

import scalaz._
import scalaz.syntax.equal._
import scalaz.std.anyVal._

trait StringValidators {

  def maxStrLength[E](max: Int, e: => E): Validator[E,String] = strLengthIs(_ <= max, e)

  def minStrLength[E](min: Int, e: => E): Validator[E,String] = strLengthIs(_ >= min, e)

  def strLength[E](x: Int, e: => E): Validator[E, String] = strLengthIs(_ == x, e)

  def strLengthIs[E](fx: (Int) => Boolean, e: => E): Validator[E,String] = s =>
    fromBoolean(fx(s.length), e, s)

  def matchRegex[E](r: Regex, e: => E): Validator[E,String] =
    validator(r.pattern.matcher(_).matches(), e)

  def notBlank[E](e: => E): Validator[E, String] =
    validator(_.trim.size > 0, e)

  /** The Luhn check in a validation. */
  def luhn[E](e: => E): Validator[E, String] = {

    def digitToInt(x:Char) = x.toInt - '0'.toInt

    /** True if the string passes the Luhn algorithm using the specified mod variable. */
    def luhnCheck(mod: Int, str: String): Boolean = {
      str.reverse.toList match {
        case x :: xs => (luhnSum(xs, 0, 2) * 9) % mod === digitToInt(x)
        case _ => false
      }
    }

    /** Calculates the total sum of the characters using the Luhn algorithm. */
    @tailrec
    def luhnSum(str: List[Char], sum: Int, multiplier: Int) : Int = {
      def nextMulti(m: Int) = if (m == 1) 2 else 1
      def doubleSum(i: Int) = i % 10 + i / 10
      str match {
        case Nil => sum
        case x :: xs => luhnSum(xs, sum + doubleSum(digitToInt(x) * multiplier), nextMulti(multiplier))
      }
    }

    validator(luhnCheck(10, _), e)
  }


}

// vim: expandtab:ts=2:sw=2
