package scalaz.contrib
package spire

trait Ops[F, +SP[_], +SZ[_]] {
  def asSpire: SP[F]
  def asScalaz: SZ[F]
}

// vim: expandtab:ts=2:sw=2
