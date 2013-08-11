package scalaz.contrib
package spire

import scalaz.@@
import scalaz.Tags.Multiplication

import _root_.spire.algebra

private[scalaz] trait ToSpireConversions0 {
  implicit def scalazSemigroup2Spire[F](implicit m: scalaz.Semigroup[F]): algebra.Semigroup[F] = m.asSpire
  implicit def scalazAddSemigroup2Spire[F](implicit m: scalaz.Semigroup[F]): algebra.AdditiveSemigroup[F] = m.asSpireAdditive
  implicit def scalazMulSemigroup2Spire[F](implicit m: scalaz.Semigroup[F @@ Multiplication]): algebra.MultiplicativeSemigroup[F] = m.asSpire
}

private[scalaz] trait ToSpireConversions extends ToSpireConversions0 {
  implicit def scalazMonoid2Spire[F](implicit m: scalaz.Monoid[F]): algebra.Monoid[F] = m.asSpire
  implicit def scalazAddMonoid2Spire[F](implicit m: scalaz.Monoid[F]): algebra.AdditiveMonoid[F] = m.asSpireAdditive
  implicit def scalazMulMonoid2Spire[F](implicit m: scalaz.Monoid[F @@ Multiplication]): algebra.MultiplicativeMonoid[F] = m.asSpire
}

private[scalaz] trait ToScalazConversions2 {
  implicit def spireAddSemigroup2Scalaz[F](implicit m: algebra.AdditiveSemigroup[F]): scalaz.Semigroup[F] = m.asScalaz
}

private[scalaz] trait ToScalazConversions1 extends ToScalazConversions2 {
  implicit def spireAddMonoid2Scalaz[F](implicit m: algebra.AdditiveMonoid[F]): scalaz.Monoid[F] = m.asScalaz
}

private[scalaz] trait ToScalazConversions0 extends ToScalazConversions1 {
  implicit def spireSemigroup2Scalaz[F](implicit m: algebra.Semigroup[F]): scalaz.Semigroup[F] = m.asScalaz
  implicit def spireMulSemigroup2Scalaz[F](implicit m: algebra.MultiplicativeSemigroup[F]): scalaz.Semigroup[F @@ Multiplication] = m.asScalaz
}

private[scalaz] trait ToScalazConversions extends ToScalazConversions0 {
  implicit def spireMonoid2Scalaz[F](implicit m: algebra.Monoid[F]): scalaz.Monoid[F] = m.asScalaz
  implicit def spireMulMonoid2Scalaz[F](implicit m: algebra.MultiplicativeMonoid[F]): scalaz.Monoid[F @@ Multiplication] = m.asScalaz
}

object conversions {
  object toSpire extends ToSpireConversions
  object toScalaz extends ToScalazConversions
}

// vim: expandtab:ts=2:sw=2
