package scalaz.contrib

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._

import org.specs2.scalaz.Spec

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._
import scalaz.std.AllInstances._

class DurationTest extends Spec {

  import scalaz.contrib.nscala_time._
  import com.github.nscala_time.time.Imports._

  implicit def DurationArbitrary: Arbitrary[Duration] =
    implicitly[Arbitrary[Int]] map { Duration.standardSeconds(_) }

  checkAll(monoid.laws[Duration])
  checkAll(equal.laws[Duration])
}
