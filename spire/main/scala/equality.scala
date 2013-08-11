package scalaz.contrib
package spire

import _root_.spire.algebra

object Equalities {

  // Equal

  trait EqualityOps[F] extends Ops[F, algebra.Eq, scalaz.Equal]

  trait SpireEqualityOps[F] extends EqualityOps[F] {
    override def asScalaz = new ScalazEquality {}

    trait ScalazEquality extends scalaz.Equal[F] {
      def equal(a1: F, a2: F) = asSpire.eqv(a1, a2)
    }
  }

  trait ScalazEqualityOps[F] extends EqualityOps[F] {
    override def asSpire = new SpireEquality {}

    trait SpireEquality extends algebra.Eq[F] {
      def eqv(x: F, y: F) = asScalaz.equal(x, y)
    }
  }

  // Order

  trait OrderOps[F] extends Ops[F, algebra.Order, scalaz.Order]

  trait SpireOrderOps[F] extends OrderOps[F] with SpireEqualityOps[F] {
    override def asScalaz = new ScalazOrder {}

    trait ScalazOrder extends scalaz.Order[F] with ScalazEquality {
      override def equal(x: F, y: F) = super[ScalazEquality].equal(x, y)
      def order(x: F, y: F) = scalaz.Ordering.fromInt(asSpire.compare(x, y))
    }
  }

  trait ScalazOrderOps[F] extends OrderOps[F] with ScalazEqualityOps[F] {
    override def asSpire = new SpireOrder {}

    trait SpireOrder extends algebra.Order[F] with SpireEquality {
      override def eqv(x: F, y: F) = super[SpireEquality].eqv(x, y)
      def compare(x: F, y: F) = asScalaz.order(x, y).toInt
    }
  }

}

private[scalaz] trait EqualityOps0 {
  import Equalities._
  implicit class SpireEquality2Ops[F](val asSpire: algebra.Eq[F]) extends SpireEqualityOps[F]
  implicit class ScalazEquality2Ops[F](val asScalaz: scalaz.Equal[F]) extends ScalazEqualityOps[F]
}

private[scalaz] trait EqualityOps extends EqualityOps0 {
  import Equalities._
  implicit class SpireOrder2Ops[F](val asSpire: algebra.Order[F]) extends SpireOrderOps[F]
  implicit class ScalazOrder2Ops[F](val asScalaz: scalaz.Order[F]) extends ScalazOrderOps[F]
}

// vim: expandtab:ts=2:sw=2
