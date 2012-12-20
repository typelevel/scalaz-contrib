package scalaz.contrib

import scalaz._

package object validator {

  type Validator[E, T] = T => Validation[E, T]

  def fromBoolean[T, E](b: Boolean, e: => E, t: => T) = if (b) Success(t) else Failure(e)

  def validator[T, E](p: T => Boolean, e: => E): Validator[E, T] = t => fromBoolean(p(t), e, t)

  object basic extends BasicValidators
  object string extends StringValidators

  object all extends BasicValidators with StringValidators

}

// vim: expandtab:ts=2:sw=2
