package scalaz.contrib
package spire

import org.specs2.scalaz.Spec

import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalazProperties._

class ScalazTest extends Spec {

  import scalaz.std.anyVal._
  import scalaz.@@
  import scalaz.Tags.Multiplication
  import _root_.spire.algebra
  import _root_.spire.implicits._

  val R = algebra.Rig[Int]
  val AddM: algebra.AdditiveMonoid[Int] = R
  val MulM: algebra.MultiplicativeMonoid[Int] = R

  // This is a bit inconvenient as we have to pass all necessary instances
  // (the instance under test, `Eq` and `Arbitrary`) to the same method.
  // If we want to specify the instance under test explicitly, we have to
  // pass the two others also explicitly.
  // TODO investigate whether we should change scalaz-scalacheck-binding
  // to mimic the structure of spire-scalacheck-binding

  checkAll("Int", monoid.laws[Int](AddM.asScalaz, implicitly, implicitly))
  checkAll("Int @@ Multiplication", monoid.laws[Int @@ Multiplication](MulM.asScalaz, implicitly, implicitly))

  // The following (correctly) failes to compile
  // (expected: Monoid, found: Tuple2)
  // checkAll("Int @@ Multiplication", monoid.laws[Int @@ Multiplication](R.asScalaz, implicitly, implicitly))

  // this should compile
  val (ma, mb) = R.asScalaz


}

// vim: expandtab:ts=2:sw=2
