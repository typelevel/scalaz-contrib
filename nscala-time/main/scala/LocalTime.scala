package scalaz.contrib
package nscala_time

import org.joda.time.LocalTime

trait LocalTimeInstances{
  implicit val localTimeInstance = OrderFromInt[LocalTime](_ compareTo _)
}
