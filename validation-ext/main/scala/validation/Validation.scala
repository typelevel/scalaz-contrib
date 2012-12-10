package scalaz.contrib
package validation

import scalaz._
import Scalaz._
import scala.Ordering

object Validation extends CoreValidation with StringValidation

/**
 * Some basic validation functions.
 */
trait CoreValidation {

  /**
   *
   * @param max The max, can be any type that has an Ordering
   * @param f The return object if there is a failure.
   * @return {(t: T) => if (notValid) {Failure(NonEmptyList(t)) else {(t: T) => Success(t)} .
   */
  def max[T: Ordering, F](max: T, f: => F): Valid[F, T] =
    valid(_ <= max, f)

  def min[T: Ordering, F](min: T, f: => F): Valid[F, T] =
    valid(_ >= min, f)

  /** Inclusive range. */
  def range[T: Ordering, F](min: T, max:T, f: => F): Valid[F, T] =
    valid(t => {t >= min && t <= max}, f)

  def equalO[T: Ordering, F](equal: T, f: => F): Valid[F, T] =
    valid(_ == equal, f)

  def equal[T: Equal, F](equal: T, f: => F) : Valid[F, T] =
    valid(_ === equal, f)

  def minSize[V, T[_]: Length, F](min: Int, f: => F): Valid[F, T[V]] =
    valid(implicitly[Length[T]].length(_) >= min, f)

  def maxSize[V, T[_]: Length, F](max: Int, f: => F): Valid[F, T[V]] =
    valid(implicitly[Length[T]].length(_) <= max, f)

  def notEmpty[V, T[_]: Length,F](f: => F): Valid[F, T[V]] =
    valid(implicitly[Length[T]].length(_) === 0, f)

}






