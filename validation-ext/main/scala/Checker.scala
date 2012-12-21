package scalaz.contrib

import scalaz._

import converter.Converter
import validator.Validator

object Checker {

  private[scalaz] def validatorsToSeq[F, T](t: T, vs: Seq[Validator[F, T]]) =
    vs.flatMap(_(t).toSeq).toVector

  def check[T](t: T): Checker[Nothing, T] =
    YesChecker(t, Vector.empty)

  def checkThat[F, T](t: T, vs: Validator[F, T]*): Checker[F, T] =
    YesChecker(t, validatorsToSeq(t, vs))

  def CheckerInstance[F]: Functor[({ type λ[α] = Checker[F, α] })#λ] = 
    new Functor[({ type λ[α] = Checker[F, α] })#λ] {
      def map[A, B](fa: Checker[F, A])(f: A => B) = fa map f
    }

}

sealed trait Checker[+F, T] {

  def checkThat[F1 >: F](vs: Validator[F1, T]*): Checker[F1, T]

  def convertTo[F1 >: F, U](conv: Converter[F1, T, U]): Checker[F1, U]

  def toValidation: Validation[NonEmptyList[F], T]

  def toOption = toValidation.toOption

  def map[U](f: T => U): Checker[F, U] = this match {
    case YesChecker(value, failures) => YesChecker(f(value), failures)
    case NoChecker(failures) => NoChecker(failures)
  }

}

private[scalaz] case class YesChecker[+F, T](value: T, failures: Vector[F]) extends Checker[F, T] {

  import Checker._

  def checkThat[F1 >: F](vs: Validator[F1, T]*) =
    YesChecker(value, failures ++ validatorsToSeq(value, vs))

  def convertTo[F1 >: F, U](conv: Converter[F1, T, U]) =
    failures match {
      case f +: fs => NoChecker(NonEmptyList(f, fs: _*))
      case _ => conv(value).fold(f => NoChecker(NonEmptyList(f)), YesChecker(_, Vector.empty))
    }

  def toValidation =
    failures match {
      case f +: fs => Failure(NonEmptyList(f, fs: _*))
      case _ => Success(value)
    }

}

private[scalaz] case class NoChecker[+F, T](failures: NonEmptyList[F]) extends Checker[F, T] {

  def checkThat[F1 >: F](vs: Validator[F1, T]*) =
    this

  def convertTo[F1 >: F, U](conv: Converter[F1, T, U]) =
    NoChecker(failures)

  def toValidation: Validation[NonEmptyList[F], T] =
    Failure(failures)

  override def toOption =
    None

}

// vim: expandtab:ts=2:sw=2
