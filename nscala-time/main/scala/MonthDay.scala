package scalaz.contrib
package nscala_time

import org.joda.time.MonthDay

trait MonthDayInstances{
  implicit val MonthDayInstance = OrderFromInt[MonthDay](_ compareTo _)
}
