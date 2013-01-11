package scalaz.contrib
package std

import scalaz._

import scala.concurrent.{Future, ExecutionContext, CanAwait, Await}
import scala.concurrent.duration.Duration

trait FutureInstances1 {

  private[scalaz] class FutureMonoid[A](implicit m: Monoid[A], executionContext: ExecutionContext) extends Monoid[Future[A]] {

    def zero: Future[A] = Future(m.zero)

    def append(f1: Future[A], f2: => Future[A]): Future[A] = for {
      first <- f1
      second <- f2
    } yield m.append(first, second)

  }

  implicit def FutureMonoid[A](implicit m: Monoid[A], executionContext: ExecutionContext): Monoid[Future[A]] = new FutureMonoid[A]

}

trait FutureInstances extends FutureInstances1 {

  private[scalaz] class FutureInstance(implicit ec: ExecutionContext) extends Monad[Future] with Cobind[Future] with Cojoin[Future] with Each[Future] {

    def point[A](a: => A): Future[A] = Future(a)
    def bind[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa flatMap f
    override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
    def cobind[A, B](fa: Future[A])(f: Future[A] => B): Future[B] = Future(f(fa))
    def cojoin[A](a: Future[A]): Future[Future[A]] = Future(a)
    def each[A](fa: Future[A])(f: A => Unit) = fa foreach f

  }

  implicit def futureInstance(implicit executionContext: ExecutionContext): Monad[Future] with Cobind[Future] with Cojoin[Future] with Each[Future] = 
    new FutureInstance


  /**
   * Requires explicit usage as the use of `Await.result`. Can throw an exception, which is inherently bad.
   */
  def futureComonad(duration: Duration)(implicit executionContext: ExecutionContext): Comonad[Future] = new FutureInstance with Comonad[Future] {
    def copoint[A](f: Future[A]): A = Await.result(f, duration)
  }


  implicit def FutureGroup[A](implicit g: Group[A], executionContext: ExecutionContext): Group[Future[A]] = new FutureMonoid[A] with Group[Future[A]] {
    def inverse(f: Future[A]): Future[A] = f.map(value => g.inverse(value))
  }

}

object scalaFuture extends FutureInstances

// vim: expandtab:ts=2:sw=2
