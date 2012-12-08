package scalaz.contrib
package validation

import scalaz._
import Scalaz._

/**
 * User: travis.stevens@gaiam.com
 * Date: 12/7/12
 */
trait Validation {

  def max[T, F](maxT: T, failure: F)(implicit ord: scala.Ordering[T]): (T) => ValidationNEL[F, T] = t => {
    if (t <= maxT) t.success
    else failure.failNel
  }

  def min[T,F](minT: T, failure: F)(implicit ord: scala.Ordering[T]): (T) => ValidationNEL[F,T] = t => {
    if (t >= minT) t.success
    else failure.failNel
  }

  def equalO[T,F](equalT: T, failure: F)(implicit ord: scala.Ordering[T]): (T) => ValidationNEL[F,T] = t => {
    if (t >= equalT) t.success
    else failure.failNel
  }

  def equalE[T,F](equalT: T, failure: F)(implicit eq: Equal[T]) : (T) => ValidationNEL[F,T] = t => {
    if (t === equalT) t.success
    else failure.failNel
  }

  def maxSize[T, F](max: Int, failure: F)(implicit l: Length[T]): (T) => ValidationNEL[F,T] = t => {
    if (l.length(t) <= max) t.success
    else failure.failNel
  }

  def minSize[T, F](max: Int, failure: F)(implicit l: Length[T]): (T) => ValidationNEL[F,T] = t => {
    if (l.length(t) >= max) t.success
    else failure.failNel
  }

  def notEmpty[T,F](failure: F)(implicit l: Length[T]): (T) => ValidationNEL[F,T] = t => {
    if (l.length(t) === 0) t.success
    else failure.failNel
  }


}


trait Length[T] {
  def length(t: T): Int
}

object Length {
  implicit def seqLength = new Length[Seq[_]] {def length(seq: Seq[_]) = seq.length}
  implicit def optionLength = new Length[Option[_]] {def length(o: Option[_]) = if (o.isDefined) 1 else 0}
}





