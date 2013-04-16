package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Interval

trait IntervalInstances{
  implicit val intervalInstance = Equal.equalA[Interval]
}
