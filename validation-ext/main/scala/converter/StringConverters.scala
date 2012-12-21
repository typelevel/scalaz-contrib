package scalaz.contrib
package converter

import scalaz.{Failure, Success}

import java.text.{SimpleDateFormat, ParseException, DateFormat}
import java.util.{UUID, Date}

/**
 * String conversion to other data types.
 */
trait StringConverters {

  def date[E](fmt: DateFormat, e: => E): Converter[E, String, Date] = s =>
    try {
      Success(fmt.parse(s))
    }
    catch {
      case ex: ParseException => Failure(e)
    }

  def date[E](str: String, e: => E): Converter[E, String, Date] = {
    val fmt = new SimpleDateFormat(str)
    fmt.setLenient(false)
    date(fmt, e)
  }

  def uuid[E](e: => E): Converter[E, String, UUID] = s =>
    try {
      Success(UUID.fromString(s))
    }
    catch {
      case ex: IllegalArgumentException => Failure(e)
    }

  def int[E](e: => E): Converter[E, String, Int] =
    fromNFE(_.toInt, e)

  def float[E](e: => E): Converter[E, String, Float] =
    fromNFE(_.toFloat, e)

  def double[E](e: => E): Converter[E, String, Double] =
    fromNFE(_.toDouble, e)

  private def fromNFE[N, E](fx: String => N, e: => E): Converter[E, String, N] = s =>
    try {
      Success(fx(s))
    }
    catch {
      case ex: NumberFormatException => Failure(e)
    }

}
