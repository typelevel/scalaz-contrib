package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Seconds

trait SecondsInstances{
  implicit val secondsInstance = new Monoid[Seconds] with Order[Seconds] {
    def order(x: Seconds, y: Seconds) = Ordering.fromInt(x compareTo y)
    def append(x: Seconds, y: => Seconds) = x plus y
    val zero = Seconds.ZERO
  }
}
