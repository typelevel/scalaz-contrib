package scalaz.contrib

import org.specs2.scalaz.Spec
import scalaz.scalacheck.ScalazProperties._
import scalaz.contrib.NScalaTimeArbitrary._
import org.joda.time._
import scalaz.contrib.nscala_time._

class PeriodTest extends Spec {

  checkAll(monoid.laws[Period])
  checkAll(equal.laws[Period])

}
