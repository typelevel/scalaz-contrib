package scalaz.contrib
package nscala_time

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary._

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._
import scalaz.std.AllInstances._

import org.joda.time._

object NScalaTimeArbitrary {

  private def arb[A: Arbitrary] = implicitly[Arbitrary[A]]

  private val smallIntArb = Arbitrary(Gen.choose(1, 100000))

  implicit val DurationArbitrary: Arbitrary[Duration] =
    arb[Int] map { Duration.standardSeconds(_) }

  implicit val PeriodArbitrary: Arbitrary[Period] =
    smallIntArb map { Period.millis(_) }

  implicit val DateTimeArbitrary: Arbitrary[DateTime] =
    arb[Int] map { new DateTime(_) }

  implicit val LocalDateArbitrary: Arbitrary[LocalDate] =
    arb[Int] map { new LocalDate(_) }

  implicit val LocalTimeArbitrary: Arbitrary[LocalTime] =
    arb[Int] map { new LocalTime(_) }

  implicit val LocalDateTimeArbitrary: Arbitrary[LocalDateTime] =
    arb[Int] map { new LocalDateTime(_) }

  implicit val DaysArbitrary: Arbitrary[Days] =
    smallIntArb map Days.days

  implicit val HoursArbitrary: Arbitrary[Hours] =
    smallIntArb map Hours.hours

  implicit val MonthsArbitrary: Arbitrary[Months] =
    smallIntArb map Months.months

  implicit val SecondsArbitrary: Arbitrary[Seconds] =
    smallIntArb map Seconds.seconds

  implicit val YearsArbitrary: Arbitrary[Years] =
    smallIntArb map Years.years

  implicit val WeeksArbitrary: Arbitrary[Weeks] =
    smallIntArb map Weeks.weeks

  implicit val MinutesArbitrary: Arbitrary[Minutes] =
    smallIntArb map Minutes.minutes

  implicit val InstantArbitrary: Arbitrary[Instant] =
    arb[Long] map { new Instant(_) }

  implicit val YearMonthArbitrary: Arbitrary[YearMonth] =
    arb[Int] map { new YearMonth(_) }

  implicit val MonthDayArbitrary: Arbitrary[MonthDay] =
    arb[Int] map { new MonthDay(_) }

  implicit val IntervalArbitrary: Arbitrary[Interval] =
    Apply[Arbitrary].apply2(arb[Int], arb[Int])((a, b) =>
      if(a > b)
        new Interval(b, a)
      else
        new Interval(a, b)
    )

}
