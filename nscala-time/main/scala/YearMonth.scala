package scalaz.contrib
package nscala_time

import org.joda.time.YearMonth

trait YearMonthInstances{
  implicit val yearMonthInstance = OrderFromInt[YearMonth](_ compareTo _)
}
