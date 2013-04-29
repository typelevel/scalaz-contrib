package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Minutes

trait MinutesInstances{
  implicit val minutesInstance = new Monoid[Minutes] with Order[Minutes] {
    def order(x: Minutes, y: Minutes) = Ordering.fromInt(x compareTo y)
    def append(x: Minutes, y: => Minutes) = x plus y
    val zero = Minutes.ZERO
  }
}
