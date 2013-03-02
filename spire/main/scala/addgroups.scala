package scalaz.contrib
package spire

import _root_.spire.algebra

object AdditiveGroups {
  trait Ops[F, +SP[_], +SZ[_]] {
    def asSpire: SP[F]
    def asScalaz: SZ[F]
  }

  trait SemigroupOps[F] extends Ops[F, algebra.AdditiveSemigroup, scalaz.Semigroup]
  trait MonoidOps[F] extends Ops[F, algebra.AdditiveMonoid, scalaz.Monoid]
}

private[scalaz] trait AddGroupOps0 {
  import AdditiveGroups._

  implicit class SpireAddSemigroup2Ops[F](val asSpire: algebra.AdditiveSemigroup[F]) extends SemigroupOps[F] {
    override def asScalaz = asSpire.additive.asScalaz
  }
}

private[scalaz] trait AddGroupOps extends AddGroupOps0 {
  import AdditiveGroups._

  implicit class SpireAddMonoid2Ops[F](val asSpire: algebra.AdditiveMonoid[F]) extends MonoidOps[F] {
    override def asScalaz = asSpire.additive.asScalaz
  }
}

// vim: expandtab:ts=2:sw=2
