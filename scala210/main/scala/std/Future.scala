package scalaz.contrib
package std

import scalaz.Monad

import scala.concurrent.{Future, ExecutionContext, CanAwait, Await}
import scala.concurrent.duration.Duration

trait FutureInstances {

  implicit def futureInstance(implicit ec: ExecutionContext, duration: Duration): Monad[Future] = new Monad[Future] {
    def point[A](a: => A): Future[A] = Future(a)
    def bind[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa flatMap f
    override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
  }

}

object future extends FutureInstances

// vim: expandtab:ts=2:sw=2
