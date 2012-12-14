package scalaz.contrib
package validator

import java.text.{SimpleDateFormat, ParseException, DateFormat}
import java.util.{Date, UUID}

import annotation.tailrec
import util.matching.Regex

import scalaz._
import scalaz.syntax.equal._
import scalaz.std.anyVal._

trait StringValidators {

  def matchRegex[F](r: Regex, f: => F): Validator[F,String] =
    validator(r.pattern.matcher(_).matches(), f)

  def notBlank[F](f: => F): Validator[F, String] =
    validator(_.trim.size > 0, f)

  def uuid[F](f: => F): Converter[F, String, UUID] = s =>
    try {
      Success(UUID.fromString(s))
    }
    catch {
      case ex: IllegalArgumentException => Failure(f)
    }

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

  def date[F](fmt: DateFormat, f: => F): Converter[F, String, Date] = s =>
    try {
      Success(fmt.parse(s))
    }
    catch {
      case e: ParseException => Failure(f)
    }

  def date[F](str: String, f: => F): Converter[F, String, Date] = {
    val fmt = new SimpleDateFormat(str)
    fmt.setLenient(false)
    date(fmt, f)
  }

}

// vim: expandtab:ts=2:sw=2
