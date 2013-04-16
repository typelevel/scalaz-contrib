package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Hours

trait HoursInstances{
  implicit val hoursInstance = new Monoid[Hours] with Order[Hours] {
    def order(x: Hours, y: Hours) = Ordering.fromInt(x compareTo y)
    def append(x: Hours, y: => Hours) = x plus y
    val zero = Hours.ZERO
  }
}
