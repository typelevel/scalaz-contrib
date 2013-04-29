package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Months

trait MonthsInstances{
  implicit val monthsInstance = new Monoid[Months] with Order[Months] {
    def order(x: Months, y: Months) = Ordering.fromInt(x compareTo y)
    def append(x: Months, y: => Months) = x plus y
    val zero = Months.ZERO
  }
}
