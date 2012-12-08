package scalaz.contrib
package validation

import scalaz._
import Scalaz._
import scala.Ordering

/**
 * User: OleTraveler
 * Date: 12/7/12
 */
trait Length[T] {
  def length(t: T): Int
}

object Length {
  implicit def seqLength = new Length[Seq[_]] {def length(seq: Seq[_]) = seq.length}
  implicit def optionLength = new Length[Option[_]] {def length(o: Option[_]) = if (o.isDefined) 1 else 0}
}



trait Validation {

  type Valid[T, F] = T => ValidationNEL[F, T]

  def max[T:Ordering, F](maxT: T, failure: F): Valid[T, F] = t => {
    if (t <= maxT) t.success
    else failure.failNel
  }

  def min[T:Ordering,F](minT: T, failure: F): Valid[T, F] = t => {
    if (t >= minT) t.success
    else failure.failNel
  }

  def equalO[T:Ordering,F](equalT: T, failure: F): Valid[T, F] = t => {
    if (t >= equalT) t.success
    else failure.failNel
  }

  def equalE[T:Equal,F](equalT: T, failure: F) : Valid[T, F] = t => {
    if (t === equalT) t.success
    else failure.failNel
  }

  def maxSize[T:Length, F](max: Int, failure: F): Valid[T, F] = t => {
    if (implicitly[Length[T]].length(t) <= max) t.success
    else failure.failNel
  }

  def minSize[T:Length, F](max: Int, failure: F): Valid[T, F] = t => {
    if (implicitly[Length[T]].length(t) >= max) t.success
    else failure.failNel
  }

  def notEmpty[T: Length,F](failure: F): Valid[T, F] = t => {
    if (implicitly[Length[T]].length(t) === 0) t.success
    else failure.failNel
  }


}






