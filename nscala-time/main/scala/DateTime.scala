package scalaz.contrib
package nscala_time

import org.joda.time.DateTime

trait DateTimeInstances{
  implicit val dateTimeInstance = OrderFromInt[DateTime](_ compareTo _)
}
