package scalaz.contrib

import org.specs2.scalaz.Spec
import scalaz.scalacheck.ScalazProperties._
import scalaz.contrib.NScalaTimeArbitrary._
import org.joda.time._
import scalaz.contrib.nscala_time._

class MonthsTest extends Spec {

  checkAll(monoid.laws[Months])
  checkAll(order.laws[Months])

}
