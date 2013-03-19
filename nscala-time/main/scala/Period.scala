package scalaz.contrib
package nscala_time

import scalaz._
import com.github.nscala_time.time.TypeImports._

trait PeriodInstances {
  implicit val periodInstance = new Monoid[Period] with Equal[Period] {
    override def zero = new org.joda.time.Period()
    override def append(f1: Period, f2: ⇒ Period) = new com.github.nscala_time.time.RichPeriod(f1) + f2
    override def equal(a1: Period, a2: Period) = a1 == a2
  }
}
