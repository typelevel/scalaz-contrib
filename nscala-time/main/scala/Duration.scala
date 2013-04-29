package scalaz.contrib
package nscala_time

import scalaz._
import com.github.nscala_time.time.TypeImports._

trait DurationInstances {
  implicit val durationInstance = new Monoid[Duration] with Order[Duration] {
    override def zero = org.joda.time.Duration.ZERO
    override def append(f1: Duration, f2: ⇒ Duration) = f1.withDurationAdded(f2, 1)
    override def order(a1: Duration, a2: Duration) = Ordering.fromInt(a1 compareTo a2)
  }
}
