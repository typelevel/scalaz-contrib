package scalaz.contrib
package nscala_time

import org.joda.time.LocalDateTime

trait LocalDateTimeInstances{
  implicit val localDateTimeInstance = OrderFromInt[LocalDateTime](_ compareTo _)
}
