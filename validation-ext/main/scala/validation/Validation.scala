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

  type Valid[F, T] = T => ValidationNEL[F, T]

  def max[T: Ordering, F](max: T, failure: F): Valid[F, T] = 
    valid(_ <= max, failure)

  def min[T: Ordering, F](minT: T, failure: F): Valid[F, T] =
    valid(_ >= minT, failure)

  def equalO[T: Ordering, F](equalT: T, failure: F): Valid[F, T] = 
    valid(_ >= equalT, failure)

  def equalE[T: Equal, F](equalT: T, failure: F) : Valid[F, T] =     
    valid(_ === equalT, failure)

  def maxSize[T: Length, F](max: Int, failure: F): Valid[F, T] =
    valid(implicitly[Length[T]].length(_) <= max, failure) 

  def minSize[T: Length, F](max: Int, failure: F): Valid[F, T] =
    valid(implicitly[Length[T]].length(_) >= max, failure)

  def notEmpty[T: Length,F](failure: F): Valid[F, T] = 
    valid(implicitly[Length[T]].length(_) === 0, failure)

  private def valid[T, F](p: T => Boolean, f: F): Valid[F, T] = t => if(p(t)) t.success else f.failNel
}






