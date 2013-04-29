package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Weeks

trait WeeksInstances{
  implicit val weeksInstance = new Monoid[Weeks] with Order[Weeks] {
    def order(x: Weeks, y: Weeks) = Ordering.fromInt(x compareTo y)
    def append(x: Weeks, y: => Weeks) = x plus y
    val zero = Weeks.ZERO
  }
}
