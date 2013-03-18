package scalaz.contrib

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

import org.specs2.scalaz.Spec

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._
import scalaz.std.AllInstances._

class PeriodTest extends Spec {

  import scalaz.contrib.nscala_time._
  import com.github.nscala_time.time.Imports._

  implicit def PeriodArbitrary: Arbitrary[Period] =
    Arbitrary(Gen.choose(1, 100000)) map { Period.millis(_) }

  checkAll(monoid.laws[Period])
  checkAll(equal.laws[Period])
}
