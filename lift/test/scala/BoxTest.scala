package scalaz.contrib

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._

import org.specs2.scalaz.Spec

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._
import scalaz.std.AllInstances._

import net.liftweb.common.{Box, Failure}

class TryTest extends Spec {

  import scalaz.contrib.lift._

  implicit def BoxArbitrary[A](implicit a: Arbitrary[A]): Arbitrary[Box[A]] =
    implicitly[Arbitrary[Option[String \/ A]]] map { _.fold(empty[A])(_.fold(Failure(_, empty, empty), full)) }

  checkAll(monad.laws[Box])
  checkAll(equal.laws[Box[Int]])

}

// vim: expandtab:ts=2:sw=2
