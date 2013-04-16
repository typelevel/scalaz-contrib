package scalaz.contrib
package nscala_time

import scalaz._

private object OrderFromInt{

  def apply[A](f: (A, A) => Int): Order[A] = new Order[A] {
    def order(x: A, y: A) = Ordering.fromInt(f(x, y))
  }

}
