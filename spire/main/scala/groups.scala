package scalaz.contrib
package spire

import _root_.spire.algebra

object Groups {

  // Semigroups

  trait SemigroupOps[F] {
    def asSpire: algebra.Semigroup[F]
    def asScalaz: scalaz.Semigroup[F]

    def asSpireAdditive: algebra.AdditiveSemigroup[F] =
      algebra.Additive(asSpire)

    def asSpireMultiplicative: algebra.MultiplicativeSemigroup[F] =
      algebra.Multiplicative(asSpire)
  }

  trait SpireSemigroupOps[F] extends SemigroupOps[F] {
    override def asScalaz: scalaz.Semigroup[F] = new ScalazSemigroup {}

    private[scalaz] trait ScalazSemigroup extends scalaz.Semigroup[F] {
      def append(f1: F, f2: => F) = asSpire.op(f1, f2)
    }
  }

  trait ScalazSemigroupOps[F] extends SemigroupOps[F] {
    override def asSpire: algebra.Semigroup[F] = new SpireSemigroup {}

    private[scalaz] trait SpireSemigroup extends algebra.Semigroup[F] {
      def op(x: F, y: F) = asScalaz.append(x, y)
    }
  }

  // Monoids

  trait MonoidOps[F] extends SemigroupOps[F] {
    def asSpire: algebra.Monoid[F]
    def asScalaz: scalaz.Monoid[F]

    override def asSpireAdditive: algebra.AdditiveMonoid[F] =
      algebra.Additive(asSpire)

    override def asSpireMultiplicative: algebra.MultiplicativeMonoid[F] =
      algebra.Multiplicative(asSpire)
  }

  trait SpireMonoidOps[F] extends MonoidOps[F] with SpireSemigroupOps[F] {
    override def asScalaz: scalaz.Monoid[F] = new ScalazMonoid {}

    private[scalaz] trait ScalazMonoid extends scalaz.Monoid[F] with ScalazSemigroup {
      def zero = asSpire.id
    }
  }

  trait ScalazMonoidOps[F] extends MonoidOps[F] with ScalazSemigroupOps[F] {
    override def asSpire: algebra.Monoid[F] = new SpireMonoid {}

    private[scalaz] trait SpireMonoid extends algebra.Monoid[F] with SpireSemigroup {
      def id = asScalaz.zero
    }
  }

}

private[scalaz] trait GroupOps0 {
  import Groups._
  implicit class SpireSemigroup2Scalaz[F](val asSpire: algebra.Semigroup[F]) extends SpireSemigroupOps[F]
  implicit class ScalazSemigroup2Spire[F](val asScalaz: scalaz.Semigroup[F]) extends ScalazSemigroupOps[F]
}

private[scalaz] trait GroupOps extends GroupOps0 {
  import Groups._
  implicit class SpireMonoid2Scalaz[F](val asSpire: algebra.Monoid[F]) extends SpireMonoidOps[F]
  implicit class ScalazMonoid2Spire[F](val asScalaz: scalaz.Monoid[F]) extends ScalazMonoidOps[F]
}

object groups extends GroupOps

// vim: expandtab:ts=2:sw=2
