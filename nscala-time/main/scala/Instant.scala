package scalaz.contrib
package nscala_time

import org.joda.time.Instant

trait InstantInstances{
  implicit val InstantInstance = OrderFromInt[Instant](_ compareTo _)
}
