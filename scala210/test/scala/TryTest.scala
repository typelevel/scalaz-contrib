package scalaz.contrib

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._
import scalaz.std.AllInstances._

import scala.util.Try

class TryTest extends Spec {

  import scalaz.contrib.std.utilTry._

  implicit def TryArbitrary[A](implicit a: Arbitrary[A]): Arbitrary[Try[A]] =
    implicitly[Arbitrary[Int \/ A]] map { _.fold(n => util.Failure(new RuntimeException(n.toString)), util.Success(_)) }

  checkAll(monad.laws[Try])
  checkAll(traverse.laws[Try])
  checkAll(plus.laws[Try])
  checkAll(equal.laws[Try[Int]])

}

// vim: expandtab:ts=2:sw=2
