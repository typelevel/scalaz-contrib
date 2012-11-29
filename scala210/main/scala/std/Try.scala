package scalaz.contrib
package std

import scalaz._
import scalaz.syntax.id._
import scalaz.Isomorphism._

import scala.util.Try

trait TryInstances1 {

  type EitherThrowable[A] = Throwable \/ A

  private[scalaz] def tryIsoFunctor = new IsoFunctorTemplate[Try, EitherThrowable] {

    def to[A](fa: Try[A]): Throwable \/ A = fa match {
      case util.Failure(e) => e.left
      case util.Success(a) => a.right
    }

    def from[A](ga: Throwable \/ A): Try[A] = ga.fold(util.Failure(_), util.Success(_))

  }

  implicit def TryInstances2: Traverse[Try] with Monad[Try] with Plus[Try] with Cozip[Try] =
    new IsomorphismTraverse[Try, EitherThrowable]
    with IsomorphismMonad[Try, EitherThrowable] with Monad[Try] /* TODO fix in scalaz-core */
    with IsomorphismPlus[Try, EitherThrowable]
    with Cozip[Try] {
    
    def G = \/.DisjunctionInstances2[Throwable]

    // TODO move to scalaz-core
    def cozip[A, B](x: Try[A \/ B]) = G.cozip(iso.to[A \/ B](x)).bimap(iso.from[A], iso.from[B])

    def iso = tryIsoFunctor

  }


}

trait TryInstances extends TryInstances1 {

  private[scalaz] def tryIsoSet[A] = new IsoSet[Try[A], Throwable \/ A] {
    def to = tryIsoFunctor.to[A]
    def from = tryIsoFunctor.from[A]
  }

  implicit def TryEqual[A : Equal]: Equal[Try[A]] = new IsomorphismEqual[Try[A], Throwable \/ A] {
    def G = \/.DisjunctionEqual(Equal.equalA, implicitly)
    def iso = tryIsoSet
  }

  implicit def TryShow[A : Show]: Show[Try[A]] = new IsomorphismShow[Try[A], Throwable \/ A] {
    def G = \/.DisjunctionShow(Show.showFromToString, implicitly)
    def iso = tryIsoSet
  }

}

object utilTry extends TryInstances

// vim: expandtab:ts=2:sw=2
