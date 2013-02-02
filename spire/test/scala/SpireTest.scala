package scalaz.contrib
package spire

import org.specs2.scalaz.Spec

import _root_.spire.algebra.Laws

class SpireTest extends Spec {

  import scalaz.@@
  import scalaz.Tags.Multiplication
  import scalaz.std.anyVal._
  import scalaz.std.list._
  import scalaz.std.map._

  val M = scalaz.Monoid[Int]
  val MMult = scalaz.Monoid[Int @@ Multiplication]

  checkAll("Int", Laws[Int].monoid(M.asSpire))
  checkAll("Int @@ Multiplication", Laws[Int].multiplicativeMonoid(MMult.asSpire))
  checkAll("Int Additive", Laws[Int].additiveMonoid(M.asSpireAdditive))
  checkAll("Int Multiplicative", Laws[Int].multiplicativeMonoid(M.asSpireMultiplicative))
  checkAll("(Int, Int @@ Multiplication)", Laws[Int].rig((M, MMult).asSpire))

  // some more instances
  checkAll("Map[String, Int]", Laws[Map[String, Int]].monoid(scalaz.Monoid[Map[String, Int]].asSpire))
  checkAll("List[Int]", Laws[List[Int]].monoid(scalaz.Monoid[List[Int]].asSpire))

}

// vim: expandtab:ts=2:sw=2
