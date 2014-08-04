package scalaz.contrib
package spire

import scalaz.@@
import scalaz.Tag
import scalaz.Tags.Multiplication

import _root_.spire.algebra

object MultiplicativeGroups {

  trait Ops[F, +SP[_], +SZ[_]] {
    def asSpire: SP[F]
    def asScalaz: SZ[F @@ Multiplication]
  }

  // Semigroups

  trait SemigroupOps[F] extends Ops[F, algebra.MultiplicativeSemigroup, scalaz.Semigroup]

  trait SpireSemigroupOps[F] extends SemigroupOps[F] {
    override def asScalaz = new ScalazSemigroup {}

    trait ScalazSemigroup extends scalaz.Semigroup[F @@ Multiplication] {
      def append(f1: F @@ Multiplication, f2: => F @@ Multiplication) = Multiplication(asSpire.times(Tag.unwrap(f1), Tag.unwrap(f2)))
    }
  }

  trait ScalazSemigroupOps[F] extends SemigroupOps[F] {
    override def asSpire = new SpireSemigroup {}

    trait SpireSemigroup extends algebra.MultiplicativeSemigroup[F] {
      def times(x: F, y: F) = Tag.unwrap(asScalaz.append(Multiplication(x), Multiplication(y)))
    }
  }

  // Monoids

  trait MonoidOps[F] extends SemigroupOps[F] with Ops[F, algebra.MultiplicativeMonoid, scalaz.Monoid]

  trait SpireMonoidOps[F] extends MonoidOps[F] with SpireSemigroupOps[F] {
    override def asScalaz = new ScalazMonoid {}

    trait ScalazMonoid extends scalaz.Monoid[F @@ Multiplication] with ScalazSemigroup {
      def zero = Multiplication(asSpire.one)
    }
  }

  trait ScalazMonoidOps[F] extends MonoidOps[F] with ScalazSemigroupOps[F] {
    override def asSpire = new SpireMonoid {}

    trait SpireMonoid extends algebra.MultiplicativeMonoid[F] with SpireSemigroup {
      def one = Tag.unwrap(asScalaz.zero)
    }
  }

}

private[scalaz] trait MulGroupOps0 {
  import MultiplicativeGroups._
  implicit class SpireMulSemigroup2Ops[F](val asSpire: algebra.MultiplicativeSemigroup[F]) extends SpireSemigroupOps[F]
  implicit class ScalazMulSemigroup2Ops[F](val asScalaz: scalaz.Semigroup[F @@ Multiplication]) extends ScalazSemigroupOps[F]
}

private[scalaz] trait MulGroupOps extends MulGroupOps0 {
  import MultiplicativeGroups._
  implicit class SpireMulMonoid2Ops[F](val asSpire: algebra.MultiplicativeMonoid[F]) extends SpireMonoidOps[F]
  implicit class ScalazMulMonoid2Ops[F](val asScalaz: scalaz.Monoid[F @@ Multiplication]) extends ScalazMonoidOps[F]
}

// vim: expandtab:ts=2:sw=2
