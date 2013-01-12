package scalaz.contrib

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._

import org.specs2.scalaz.Spec

import scalaz._
import scalaz.syntax.functor._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._
import scalaz.scalacheck.ScalaCheckBinding._

class CheckerTest extends Spec {

  implicit def CheckerArbitrary[A](implicit a: Arbitrary[A]): Arbitrary[Checker[String, A]] =
    implicitly[Arbitrary[(A \/ String, List[String])]] map { case (l, r) => l.fold(YesChecker(_, r.toVector), head => NoChecker(NonEmptyList(head, r: _*))) }

  // The following instances are intentionally not included in the definition of `Checker`,
  // since its only use is in a DSL -- there is no point in passing instances of `Checker`
  // around. The instances are here to check whether `map`, `andWith` also satisfy desired
  // laws.

  implicit def CheckerApplicative[F] = new Applicative[({ type λ[α] = Checker[F, α] })#λ] {
    override def map[A, B](fa: Checker[F, A])(f: A => B) = fa map f
    def point[A](a: => A) = YesChecker(a, Vector.empty)
    def ap[A, B](fa: => Checker[F, A])(f: => Checker[F, A => B]) = f.andWith(fa, _(_: A))
  }

  implicit def CheckerEqual = Equal.equalA[Checker[String, Int]]

  checkAll(applicative.laws[({ type λ[α] = Checker[String, α] })#λ])

}

// vim: expandtab:ts=2:sw=2
