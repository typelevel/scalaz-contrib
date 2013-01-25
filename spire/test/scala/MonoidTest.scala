package scalaz.contrib
package spire

import _root_.spire.algebra.{Laws, LawChecker}

class Test extends LawChecker {

  import scalaz.@@
  import scalaz.Tags.Multiplication
  import scalaz.std.anyVal._
  import scalaz.std.list._
  import scalaz.std.map._

  checkAll("Int", Laws[Int].monoid(scalaz.Monoid[Int].asSpire))
  checkAll("List[Int]", Laws[List[Int]].monoid(scalaz.Monoid[List[Int]].asSpire))
  checkAll("Map[String, Int]", Laws[Map[String, Int]].monoid(scalaz.Monoid[Map[String, Int]].asSpire))

  checkAll("Int @@ Multiplication", Laws[Int].multiplicativeMonoid(scalaz.Monoid[Int @@ Multiplication].asSpire))
}

// vim: expandtab:ts=2:sw=2
