package scalaz.contrib
package validator

import scalaz._
import java.text.{SimpleDateFormat, ParseException, DateFormat}
import java.util.{UUID, Date}

/**
 * String to various other data types.
 */
trait StringConverters {

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

  def uuid[F](f: => F): Converter[F, String, UUID] = s =>
    try {
      Success(UUID.fromString(s))
    }
    catch {
      case ex: IllegalArgumentException => Failure(f)
    }

  def int[F](f: => F): Converter[F, String, Int] =
    fromNFE(_.toInt, f)

  def float[F](f: => F): Converter[F, String, Float] =
    fromNFE(_.toFloat, f)

  def double[F](f: => F): Converter[F, String, Double] =
    fromNFE(_.toDouble, f)

  private def fromNFE[N, F](fx: String => N, f: => F): Converter[F, String, N] = s =>
    try {
      Success(fx(s))
    }
    catch {
      case e: NumberFormatException => Failure(f)
    }

}
