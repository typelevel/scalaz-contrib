package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Days

trait DaysInstances{
  implicit val daysInstance = new Monoid[Days] with Order[Days] {
    def order(x: Days, y: Days) = Ordering.fromInt(x compareTo y)
    def append(x: Days, y: => Days) = x plus y
    val zero = Days.ZERO
  }
}
