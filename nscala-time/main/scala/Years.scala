package scalaz.contrib
package nscala_time

import scalaz._
import org.joda.time.Years

trait YearsInstances{
  implicit val yearsInstance = new Monoid[Years] with Order[Years] {
    def order(x: Years, y: Years) = Ordering.fromInt(x compareTo y)
    def append(x: Years, y: => Years) = x plus y
    val zero = Years.ZERO
  }
}
