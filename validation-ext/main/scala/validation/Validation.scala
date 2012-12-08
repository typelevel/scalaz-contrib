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
  implicit val seqLength = 
    new Length[Seq[_]] {
      def length(seq: Seq[_]) = seq.length
    }

  implicit val optionLength = 
    new Length[Option[_]] {
      def length(o: Option[_]) = if (o.isDefined) 1 else 0
    }
}

trait Validation {

  type Valid[F, T] = T => ValidationNEL[F, T]

  def max[T: Ordering, F](max: T, f: F): Valid[F, T] = 
    valid(_ <= max, f)

  def min[T: Ordering, F](min: T, f: F): Valid[F, T] =
    valid(_ >= min, f)

  def equalO[T: Ordering, F](equal: T, f: F): Valid[F, T] = 
    valid(_ == equal, f)

  def equalE[T: Equal, F](equal: T, f: F) : Valid[F, T] =     
    valid(_ === equal, f)

  def maxSize[T: Length, F](max: Int, f: F): Valid[F, T] =
    valid(implicitly[Length[T]].length(_) <= max, f) 

  def minSize[T: Length, F](max: Int, f: F): Valid[F, T] =
    valid(implicitly[Length[T]].length(_) >= max, f)

  def notEmpty[T: Length,F](f: F): Valid[F, T] = 
    valid(implicitly[Length[T]].length(_) === 0, f)

  private def valid[T, F](p: T => Boolean, f: F): Valid[F, T] = t => if(p(t)) t.success else f.failNel
}






