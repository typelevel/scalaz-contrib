package scalaz.contrib
package spire

import scalaz.@@
import scalaz.Tags.Multiplication

import _root_.spire.algebra

object Rings {

  trait Ops[F, +SP[_], +SZA[_], +SZM[_]] {
    def asSpire: SP[F]
    def asScalaz: (SZA[F], SZM[F @@ Multiplication])

    @inline final def addAsScalaz: SZA[F] =
      asScalaz._1

    @inline final def mulAsScalaz: SZM[F @@ Multiplication] =
      asScalaz._2
  }

  // Semirings

  trait SemiringOps[F] extends Ops[F, algebra.Semiring, scalaz.Semigroup, scalaz.Semigroup]

  trait SpireSemiringOps[F] extends SemiringOps[F] {
    override def asScalaz = (
      asSpire.additive.asScalaz,
      // I'm not sure why this type annotation is necessary
      SpireMulSemigroup2Ops(asSpire).asScalaz: scalaz.Semigroup[F @@ Multiplication]
    )
  }

  trait ScalazSemiringOps[F] extends SemiringOps[F] {
    override def asSpire = new SpireSemiring {}

    trait SpireSemiring extends algebra.Semiring[F] {
      def times(x: F, y: F) = mulAsScalaz.append(Multiplication(x), Multiplication(y))
      def plus(x: F, y: F) = addAsScalaz.append(x, y)
    }
  }

  // Rigs

  trait RigOps[F] extends SemiringOps[F] with Ops[F, algebra.Rig, scalaz.Monoid, scalaz.Monoid]

  trait SpireRigOps[F] extends RigOps[F] with SpireSemiringOps[F] {
    override def asScalaz = (
      asSpire.additive.asScalaz,
      SpireMulMonoid2Ops(asSpire).asScalaz
    )
  }

  trait ScalazRigOps[F] extends RigOps[F] with ScalazSemiringOps[F] {
    override def asSpire = new SpireRig {}

    trait SpireRig extends algebra.Rig[F] with SpireSemiring {
      def one = mulAsScalaz.zero
      def zero = addAsScalaz.zero
    }
  }

}

private[scalaz] trait RingOps0 {
  import Rings._
  implicit class SpireSemiring2Ops[F](val asSpire: algebra.Semiring[F]) extends SpireSemiringOps[F]
  implicit class ScalazSemiring2Ops[F](val asScalaz: (scalaz.Semigroup[F], scalaz.Semigroup[F @@ Multiplication])) extends ScalazSemiringOps[F]
}

private[scalaz] trait RingOps extends RingOps0 {
  import Rings._
  implicit class SpireRig2Ops[F](val asSpire: algebra.Rig[F]) extends SpireRigOps[F]
  implicit class ScalazRig2Ops[F](val asScalaz: (scalaz.Monoid[F], scalaz.Monoid[F @@ Multiplication])) extends ScalazRigOps[F]
}

object rings extends RingOps

// vim: expandtab:ts=2:sw=2
