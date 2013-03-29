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
  import _root_.spire.std.int._

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
  //checkAll("Int @@ Multiplication", monoid.laws[Int @@ Multiplication](R.asScalaz, implicitly, implicitly))

  // this should compile
  val (ma, mb) = R.asScalaz

  // test compilation for auto-conversions


  {
    import conversions.toScalaz._

    trait Foo
    // this code will *never* be executed!
    val FooRig: algebra.Rig[Foo] = algebra.Rig[Int].asInstanceOf[algebra.Rig[Foo]]

    {
      implicit val S: algebra.Rig[Foo] = FooRig

      scalaz.Monoid[Foo]
      scalaz.Monoid[Foo @@ Multiplication]
      scalaz.Semigroup[Foo]
      scalaz.Semigroup[Foo @@ Multiplication]
    }
    {
      implicit val S: algebra.AdditiveMonoid[Foo] = FooRig

      scalaz.Monoid[Foo]
      scalaz.Semigroup[Foo]

      // should fail:
      //scalaz.Monoid[Foo @@ Multiplication]
    }
    {
      implicit val S: algebra.MultiplicativeMonoid[Foo] = FooRig

      scalaz.Monoid[Foo @@ Multiplication]
      scalaz.Semigroup[Foo @@ Multiplication]

      // should fail:
      //scalaz.Monoid[Foo]
    }
  }

}

// vim: expandtab:ts=2:sw=2
