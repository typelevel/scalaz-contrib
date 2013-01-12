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

}

sealed trait Checker[+F, T] {

  def checkThat[F1 >: F](vs: Validator[F1, T]*): Checker[F1, T]

  def convertTo[F1 >: F, U](conv: Converter[F1, T, U]): Checker[F1, U]

  def andAlso[F1 >: F, U](c: Checker[F1, U]): Checker[F1, (T, U)]

  @inline final def andWith[F1 >: F, U, R](that: Checker[F1, U], f: (T, U) => R) =
    this andAlso that map f.tupled

  def toValidation: Validation[NonEmptyList[F], T]

  def map[U](f: T => U): Checker[F, U]

  def toOption =
    toValidation.toOption

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

  def andAlso[F1 >: F, U](c: Checker[F1, U]) =
    c match {
      case YesChecker(v, fs) => YesChecker((value, v), failures ++ fs)
      case NoChecker(fs) => NoChecker(failures.toList <::: fs)
    }

  def toValidation =
    failures match {
      case f +: fs => Failure(NonEmptyList(f, fs: _*))
      case _ => Success(value)
    }

  def map[U](f: T => U): Checker[F, U] =
    YesChecker(f(value), failures)

}

private[scalaz] case class NoChecker[+F, T](failures: NonEmptyList[F]) extends Checker[F, T] {

  def checkThat[F1 >: F](vs: Validator[F1, T]*) =
    this

  def convertTo[F1 >: F, U](conv: Converter[F1, T, U]) =
    NoChecker(failures)

  def andAlso[F1 >: F, U](c: Checker[F1, U]) =
    c match {
      case YesChecker(v, fs) => NoChecker(failures :::> fs.toList)
      case NoChecker(fs) => NoChecker(failures append fs)
    }

  def toValidation: Validation[NonEmptyList[F], T] =
    Failure(failures)

  def map[U](f: T => U): Checker[F, U] =
    NoChecker(failures)

  override def toOption =
    None

}

// vim: expandtab:ts=2:sw=2
