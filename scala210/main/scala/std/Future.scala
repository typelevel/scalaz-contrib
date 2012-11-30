package scalaz.contrib
package std

import scalaz._

import scala.concurrent.{Future, ExecutionContext, CanAwait, Await}
import scala.concurrent.duration.Duration

trait FutureInstances {
  implicit def futureInstance(implicit executionContext: ExecutionContext): Monad[Future] with Cobind[Future] with Cojoin[Future] with Each[Future] = 
    new Monad[Future] with Cobind[Future] with Cojoin[Future] with Each[Future] {
    def point[A](a: => A): Future[A] = Future(a)
    def bind[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa flatMap f
    override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
    def cobind[A, B](fa: Future[A])(f: Future[A] => B): Future[B] = Future(f(fa))
    def cojoin[A](a: Future[A]): Future[Future[A]] = Future(a)
    def each[A](fa: Future[A])(f: A => Unit) = fa foreach f
  }

  /**
   * Requires explicit usage as the use of Await.result can throw an exception, which is inherently bad.
   */
  def futureCopointed(duration: Duration)(implicit executionContext: ExecutionContext): Copointed[Future] = new Copointed[Future] {
    def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
    def copoint[A](f: Future[A]): A = Await.result(f, duration)
  }

  implicit def futureComonad(implicit cb: Cobind[Future], cj: Cojoin[Future], cp: Copointed[Future]): Comonad[Future] = new Comonad[Future] {
    def map[A, B](fa: Future[A])(f: A => B): Future[B] = cp.map(fa)(f)
    def cobind[A, B](fa: Future[A])(f: Future[A] => B): Future[B] = cb.cobind(fa)(f)
    def cojoin[A](a: Future[A]): Future[Future[A]] = cj.cojoin(a)
    def copoint[A](f: Future[A]): A = cp.copoint(f)
  }

  implicit def futureMonoid[A](implicit m: Monoid[A], executionContext: ExecutionContext): Monoid[Future[A]] = new Monoid[Future[A]] {
    def zero: Future[A] = Future(m.zero)
    def append(f1: Future[A], f2: => Future[A]): Future[A] = for {
      first <- f1
      second <- f2
    } yield m.append(first, second)
  }

  implicit def futureGroup[A](implicit g: Group[A], m: Monoid[Future[A]], executionContext: ExecutionContext): Group[Future[A]] = new Group[Future[A]] {
    def zero: Future[A] = m.zero
    def inverse(f: Future[A]): Future[A] = f.map(value => g.inverse(value))
    def append(f1: Future[A], f2: => Future[A]): Future[A] = m.append(f1, f2)
  }
}

object ScalazFuture extends FutureInstances

// vim: expandtab:ts=2:sw=2
