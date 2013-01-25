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

  // This is a bit inconvenient as we have to pass all necessary instances
  // (the instance under test, `Eq` and `Arbitrary`) to the same method.
  // If we want to specify the instance under test explicitly, we have to
  // pass the two others also explicitly.
  // TODO investigate whether we should change scalaz-scalacheck-binding
  // to mimic the structure of spire-scalacheck-binding

  checkAll("Int", monoid.laws[Int](algebra.Ring[Int].additive.asScalaz, implicitly, implicitly))

  // At this point, I expected an ambiguity error, but it fails to compile
  // nonetheless. (expected: Monoid, found: Tuple2)
  // checkAll("Int @@ Multiplication", monoid.laws[Int @@ Multiplication](algebra.Ring[Int].asScalaz, implicitly, implicitly))

  checkAll("Int @@ Multiplication", monoid.laws[Int @@ Multiplication]((algebra.Ring[Int]: algebra.MultiplicativeMonoid[Int]).asScalaz, implicitly, implicitly))

}

// vim: expandtab:ts=2:sw=2
