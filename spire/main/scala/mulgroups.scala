package scalaz.contrib
package spire

import scalaz.@@
import scalaz.Tags.Multiplication

import _root_.spire.algebra

object MultiplicativeGroups {

  // Semigroups

  trait SemigroupOps[F] {
    def asSpire: algebra.MultiplicativeSemigroup[F]
    def asScalaz: scalaz.Semigroup[F @@ Multiplication]
  }

  trait SpireSemigroupOps[F] extends SemigroupOps[F] {
    override def asScalaz: scalaz.Semigroup[F @@ Multiplication] = new ScalazSemigroup {}

    private[scalaz] trait ScalazSemigroup extends scalaz.Semigroup[F @@ Multiplication] {
      def append(f1: F @@ Multiplication, f2: => F @@ Multiplication) = Multiplication(asSpire.times(f1, f2))
    }
  }

  trait ScalazSemigroupOps[F] extends SemigroupOps[F] {
    override def asSpire: algebra.MultiplicativeSemigroup[F] = new SpireSemigroup {}

    private[scalaz] trait SpireSemigroup extends algebra.MultiplicativeSemigroup[F] {
      def times(x: F, y: F) = asScalaz.append(Multiplication(x), Multiplication(y))
    }
  }

  // Monoids

  trait MonoidOps[F] extends SemigroupOps[F] {
    def asSpire: algebra.MultiplicativeMonoid[F]
    def asScalaz: scalaz.Monoid[F @@ Multiplication]
  }

  trait SpireMonoidOps[F] extends MonoidOps[F] with SpireSemigroupOps[F] {
    override def asScalaz: scalaz.Monoid[F @@ Multiplication] = new ScalazMonoid {}

    private[scalaz] trait ScalazMonoid extends scalaz.Monoid[F @@ Multiplication] with ScalazSemigroup {
      def zero = Multiplication(asSpire.one)
    }
  }

  trait ScalazMonoidOps[F] extends MonoidOps[F] with ScalazSemigroupOps[F] {
    override def asSpire: algebra.MultiplicativeMonoid[F] = new SpireMonoid {}

    private[scalaz] trait SpireMonoid extends algebra.MultiplicativeMonoid[F] with SpireSemigroup {
      def one = asScalaz.zero
    }
  }

}

private[scalaz] trait MulGroupOps0 {
  import MultiplicativeGroups._
  implicit class SpireMulSemigroup2Scalaz[F](val asSpire: algebra.MultiplicativeSemigroup[F]) extends SpireSemigroupOps[F]
  implicit class ScalazMulSemigroup2Spire[F](val asScalaz: scalaz.Semigroup[F @@ Multiplication]) extends ScalazSemigroupOps[F]
}

private[scalaz] trait MulGroupOps extends MulGroupOps0 {
  import MultiplicativeGroups._
  implicit class SpireMulMonoid2Scalaz[F](val asSpire: algebra.MultiplicativeMonoid[F]) extends SpireMonoidOps[F]
  implicit class ScalazMulMonoid2Spire[F](val asScalaz: scalaz.Monoid[F @@ Multiplication]) extends ScalazMonoidOps[F]
}

object multiplicativeGroups extends MulGroupOps

// vim: expandtab:ts=2:sw=2
