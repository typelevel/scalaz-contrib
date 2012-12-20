package scalaz.contrib
package validator

import annotation.tailrec
import util.matching.Regex

import scalaz._
import scalaz.syntax.equal._
import scalaz.std.anyVal._

trait StringValidators {

  def maxStrLength[F](max: Int, f: => F): Validator[F,String] = strLengthIs(_ <= max, f)

  def minStrLength[F](min: Int, f: => F): Validator[F,String] = strLengthIs(_ >= min, f)

  def strLength[F](x: Int, f: => F): Validator[F, String] = strLengthIs(_ == x, f)

  def strLengthIs[F](fx: (Int) => Boolean, f: => F): Validator[F,String] = s =>
    fromBoolean(fx(s.length), f, s)

  def matchRegex[F](r: Regex, f: => F): Validator[F,String] =
    validator(r.pattern.matcher(_).matches(), f)

  def notBlank[F](f: => F): Validator[F, String] =
    validator(_.trim.size > 0, f)

  /** The Luhn check in a validation. */
  def luhn[F](f: => F): Validator[F, String] = {

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

    validator(luhnCheck(10, _), f)
  }


}

// vim: expandtab:ts=2:sw=2
