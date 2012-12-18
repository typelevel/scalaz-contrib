package scalaz.contrib

import scalaz._
import converter.StringConverters

package object validator {

  type Validator[F, T] = T => Validation[F, T]

  def fromBoolean[T, F](b: Boolean, f: => F, t: => T) = if (b) Success(t) else Failure(f)

  def validator[T, F](p: T => Boolean, f: => F): Validator[F, T] = t => fromBoolean(p(t), f, t)

  object basic extends BasicValidators
  object string extends StringValidators

  object all extends BasicValidators with StringValidators

}

// vim: expandtab:ts=2:sw=2
