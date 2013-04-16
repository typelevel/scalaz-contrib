package scalaz.contrib

import org.specs2.scalaz.Spec
import scalaz.scalacheck.ScalazProperties._
import scalaz.contrib.NScalaTimeArbitrary._
import org.joda.time._
import scalaz.contrib.nscala_time._

class MinutesTest extends Spec {

  checkAll(monoid.laws[Minutes])
  checkAll(order.laws[Minutes])

}
