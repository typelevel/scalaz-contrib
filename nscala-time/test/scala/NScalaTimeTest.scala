package scalaz.contrib
package nscala_time

import org.specs2.scalaz.Spec

import scalaz.scalacheck.ScalazProperties._

import org.joda.time._

class NScalaTimeTest extends Spec {

  import NScalaTimeArbitrary._

  checkAll("DateTime", enum.laws[DateTime])

  checkAll("Days", monoid.laws[Days])
  checkAll("Days", order.laws[Days])

  checkAll("Duration", monoid.laws[Duration])
  checkAll("Duration", order.laws[Duration])

  checkAll("Hours", monoid.laws[Hours])
  checkAll("Hours", order.laws[Hours])
  
  checkAll("Instant", order.laws[Instant])
  
  checkAll("Interval", equal.laws[Interval])

  checkAll("LocalDate", enum.laws[LocalDate])

  checkAll("LocalDateTime", enum.laws[LocalDateTime])
  
  checkAll("LocalTime", order.laws[LocalTime])

  checkAll("Minutes", monoid.laws[Minutes])
  checkAll("Minutes", order.laws[Minutes])

  checkAll("MonthDay", order.laws[MonthDay])

  checkAll("Months", monoid.laws[Months])
  checkAll("Months", order.laws[Months])

  checkAll("Period", monoid.laws[Period])
  checkAll("Period", equal.laws[Period])

  checkAll("Seconds", monoid.laws[Seconds])
  checkAll("Seconds", order.laws[Seconds])

  checkAll("Weeks", monoid.laws[Weeks])
  checkAll("Weeks", order.laws[Weeks])

  checkAll("YearMonth", order.laws[YearMonth])

  checkAll("Years", monoid.laws[Years])
  checkAll("Years", order.laws[Years])

}
