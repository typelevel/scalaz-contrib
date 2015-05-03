package scalaz.contrib
package nscala_time

import org.joda.time._

import scalaz.Show

/**
 * Created by tstevens on 5/3/15.
 */
trait ShowInstances {

  implicit val showDuration: Show[Duration] = Show.showFromToString

  implicit val showPeriod: Show[Period] = Show.showFromToString

  implicit val showYears: Show[Years] = Show.showFromToString

  implicit val showMonths: Show[Months] = Show.showFromToString

  implicit val showDays: Show[Days] = Show.showFromToString

  implicit val showHours: Show[Hours] = Show.showFromToString

  implicit val showMinutes: Show[Minutes] = Show.showFromToString

  implicit val showSeconds: Show[Seconds] = Show.showFromToString

  implicit val showInterval: Show[Interval] = Show.showFromToString

  implicit val showDateTime: Show[DateTime] = Show.showFromToString

  implicit val showLocalDate: Show[LocalDate] = Show.showFromToString

  implicit val showLocalDateTime: Show[LocalDateTime] = Show.showFromToString
}
