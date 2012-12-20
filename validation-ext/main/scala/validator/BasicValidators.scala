package scalaz.contrib
package validator

import scala.Ordering.Implicits.infixOrderingOps

import scalaz.{Ordering => _, _}
import scalaz.std.anyVal._
import scalaz.syntax.equal._

/**
 * Basic validation functions.
 */
trait BasicValidators {

  /**
   * @param max The max, can be any type that has an Ordering
   * @param e The return object if there is a failure.
   */
  def atMost[T: Ordering, E](max: T, e: => E): Validator[E, T] =
    validator(_ <= max, e)

  def atLeast[T: Ordering, E](min: T, e: => E): Validator[E, T] =
    validator(_ >= min, e)

  /** Inclusive range. */
  def range[T: Ordering, E](min: T, max:T, e: => E): Validator[E, T] =
    validator(t => t >= min && t <= max, e)

  def equalA[T, E](t: T, e: => E): Validator[E, T] =
    validator(_ == t, e)

  def equal[T: Equal, E](t: T, e: => E) : Validator[E, T] =
    validator(_ === t, e)

  def minLength[E](min: Int, e: => E) = lengthIs(_ >= min, e)

  def maxLength[E](max: Int, e: => E) = lengthIs(_ <= max, e)

  def notEmpty[E](e: => E) = lengthIs(_ > 0, e)

  def lengthIs[E](lf: (Int) => Boolean, e: => E) = new {
    def apply[T](t: T)(implicit L: Unapply[Length, T]): Validation[E, T] =
      fromBoolean(lf(L.TC.length(L(t))), e, t)
  }

}

// vim: expandtab:ts=2:sw=2
