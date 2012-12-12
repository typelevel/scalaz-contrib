package scalaz.contrib
package validator

import java.util.UUID

import util.matching.Regex

import scalaz._

trait StringValidators {

  def matchRegex[F](r: Regex, f: => F): Validator[F,String] =
    validator(r.pattern.matcher(_).matches(), f)

  def notBlank[F](f: => F): Validator[F, String] =
    validator(_.trim.size > 0, f)

  def uuid[F](f: => F): String => Validation[F, UUID] = s => {
    try {
      Success(UUID.fromString(s))
    }
    catch {
      case ex: IllegalArgumentException => Failure(f)
    }
  }

}

// vim: expandtab:ts=2:sw=2
