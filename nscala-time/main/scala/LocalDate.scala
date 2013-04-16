package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.LocalDate

trait LocalDateInstances{
  implicit val localDateInstance = OrderFromInt[LocalDate](_ compareTo _)
}
