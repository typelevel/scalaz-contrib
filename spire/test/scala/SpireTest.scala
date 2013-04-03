package scalaz.contrib
package spire

import org.specs2.scalaz.Spec

import _root_.spire.algebra
import _root_.spire.algebra.{GroupLaws, RingLaws}

class SpireTest extends Spec {

  import scalaz.@@
  import scalaz.Tags.Multiplication
  import scalaz.std.anyVal._
  import scalaz.std.list._
  import scalaz.std.map._
  import _root_.spire.std.int._
  import _root_.spire.std.map._
  import _root_.spire.std.seq._

  val M = scalaz.Monoid[Int]
  val MMult = scalaz.Monoid[Int @@ Multiplication]

  checkAll("Int", GroupLaws[Int].monoid(M.asSpire))
  checkAll("Int @@ Multiplication", RingLaws[Int].multiplicativeMonoid(MMult.asSpire))
  checkAll("Int Additive", GroupLaws[Int].additiveMonoid(M.asSpireAdditive))
  checkAll("Int Multiplicative", RingLaws[Int].multiplicativeMonoid(M.asSpireMultiplicative))
  checkAll("(Int, Int @@ Multiplication)", RingLaws[Int].rig((M, MMult).asSpire))

  // some more instances
  checkAll("Map[String, Int]", GroupLaws[Map[String, Int]].monoid(scalaz.Monoid[Map[String, Int]].asSpire))
  checkAll("List[Int]", GroupLaws[List[Int]].monoid(scalaz.Monoid[List[Int]].asSpire))

  // test compilation for auto-conversions
  {
    import conversions.toSpire._

    trait Foo

    {
      implicit val S: scalaz.Monoid[Foo] = null

      implicitly[algebra.Monoid[Foo]]
      implicitly[algebra.AdditiveMonoid[Foo]]
      implicitly[algebra.Semigroup[Foo]]
      implicitly[algebra.AdditiveSemigroup[Foo]]

      // should fail:
      //implicitly[algebra.MultiplicativeMonoid[Foo]]
    }
    {
      implicit val S: scalaz.Monoid[Foo @@ Multiplication] = null

      implicitly[algebra.MultiplicativeMonoid[Foo]]
      implicitly[algebra.MultiplicativeSemigroup[Foo]]

      // should fail:
      //implicitly[algebra.Monoid[Foo]]
      //implicitly[algebra.AdditiveMonoid[Foo]]
    }
  }

}

// vim: expandtab:ts=2:sw=2
