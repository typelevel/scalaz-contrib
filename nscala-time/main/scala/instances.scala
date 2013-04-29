package scalaz.contrib
package nscala_time

import scalaz._

import org.joda.time._

trait Instances {

  private def orderFromInt[A](f: (A, A) => Int): Order[A] = new Order[A] {
    def order(x: A, y: A) = Ordering.fromInt(f(x, y))
  }

  implicit val durationInstance = new Monoid[Duration] with Order[Duration] {
    override def zero = org.joda.time.Duration.ZERO
    override def append(f1: Duration, f2: ⇒ Duration) = f1.withDurationAdded(f2, 1)
    override def order(a1: Duration, a2: Duration) = Ordering.fromInt(a1 compareTo a2)
  }

  implicit val periodInstance = new Monoid[Period] with Equal[Period] {
    override val zero = Period.ZERO
    override def append(f1: Period, f2: ⇒ Period) = new com.github.nscala_time.time.RichPeriod(f1) + f2
    override def equal(a1: Period, a2: Period) = a1 == a2
  }

  implicit val yearsInstance = new Monoid[Years] with Order[Years] {
    def order(x: Years, y: Years) = Ordering.fromInt(x compareTo y)
    def append(x: Years, y: => Years) = x plus y
    val zero = Years.ZERO
  }

  implicit val monthsInstance = new Monoid[Months] with Order[Months] {
    def order(x: Months, y: Months) = Ordering.fromInt(x compareTo y)
    def append(x: Months, y: => Months) = x plus y
    val zero = Months.ZERO
  }

  implicit val weeksInstance = new Monoid[Weeks] with Order[Weeks] {
    def order(x: Weeks, y: Weeks) = Ordering.fromInt(x compareTo y)
    def append(x: Weeks, y: => Weeks) = x plus y
    val zero = Weeks.ZERO
  }

  implicit val daysInstance = new Monoid[Days] with Order[Days] {
    def order(x: Days, y: Days) = Ordering.fromInt(x compareTo y)
    def append(x: Days, y: => Days) = x plus y
    val zero = Days.ZERO
  }

  implicit val hoursInstance = new Monoid[Hours] with Order[Hours] {
    def order(x: Hours, y: Hours) = Ordering.fromInt(x compareTo y)
    def append(x: Hours, y: => Hours) = x plus y
    val zero = Hours.ZERO
  }

  implicit val minutesInstance = new Monoid[Minutes] with Order[Minutes] {
    def order(x: Minutes, y: Minutes) = Ordering.fromInt(x compareTo y)
    def append(x: Minutes, y: => Minutes) = x plus y
    val zero = Minutes.ZERO
  }

  implicit val secondsInstance = new Monoid[Seconds] with Order[Seconds] {
    def order(x: Seconds, y: Seconds) = Ordering.fromInt(x compareTo y)
    def append(x: Seconds, y: => Seconds) = x plus y
    val zero = Seconds.ZERO
  }

  implicit val intervalInstance = Equal.equalA[Interval]

  implicit val yearMonthInstance     = orderFromInt[YearMonth](_ compareTo _)
  implicit val monthDayInstance      = orderFromInt[MonthDay](_ compareTo _)
  implicit val instantInstance       = orderFromInt[Instant](_ compareTo _)
  implicit val dateTimeInstance      = orderFromInt[DateTime](_ compareTo _)
  implicit val localTimeInstance     = orderFromInt[LocalTime](_ compareTo _)
  implicit val localDateInstance     = orderFromInt[LocalDate](_ compareTo _)
  implicit val localDateTimeInstance = orderFromInt[LocalDateTime](_ compareTo _)

}

// vim: expandtab:ts=2:sw=2
