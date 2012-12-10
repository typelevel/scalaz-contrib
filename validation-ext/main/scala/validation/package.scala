package scalaz.contrib
import scalaz._
import Scalaz._

package object validation {
  type Valid[F, T] = T => ValidationNEL[F, T]

  def valid[T, F](p: T => Boolean, f: => F): Valid[F, T] = t => if(p(t)) t.success else f.failNel

}
