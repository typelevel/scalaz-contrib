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
   * @param f The return object if there is a failure.
   */
  def atMost[T: Ordering, F](max: T, f: => F): Validator[F, T] =
    validator(_ <= max, f)

  def atLeast[T: Ordering, F](min: T, f: => F): Validator[F, T] =
    validator(_ >= min, f)

  /** Inclusive range. */
  def range[T: Ordering, F](min: T, max:T, f: => F): Validator[F, T] =
    validator(t => t >= min && t <= max, f)

  def equalA[T, F](t: T, f: => F): Validator[F, T] =
    validator(_ == t, f)

  def equal[T: Equal, F](t: T, f: => F) : Validator[F, T] =
    validator(_ === t, f)

  def minSize[F](min: Int, f: => F) = lengthIs(_ >= min, f)

  def maxSize[F](max: Int, f: => F) = lengthIs(_ <= max, f)

  def notEmpty[F](f: => F) = lengthIs(_ > 0, f)

  def lengthIs[F](lf: (Int) => Boolean, f: => F) = new {
    def apply[T](t: T)(implicit L: Unapply[Length, T]): Validation[F, T] =
      fromBoolean(lf(L.TC.length(L(t))), f, t)
  }




}

// vim: expandtab:ts=2:sw=2
