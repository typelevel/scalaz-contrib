package scalaz.contrib

import scalaz._
import scalaz.syntax.std.boolean._
import scalaz.syntax.std.option._

import converter.Converter

package object validator {

  type Validator[E, T] = T => Option[E]

  def validator[E, T](p: T => Boolean, f: => E): Validator[E, T] = t => !p(t) option f

  def toConverter[E, T](v: Validator[E, T]): Converter[E, T, T] = t => v(t) toFailure t

  object basic extends BasicValidators
  object string extends StringValidators

  object all extends BasicValidators with StringValidators

}

// vim: expandtab:ts=2:sw=2
