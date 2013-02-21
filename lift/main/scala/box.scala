/*
 * Scalaz Instances for Lift
 *
 * Copyright (C) 2012 Instancez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scalaz.contrib
package lift

import scalaz._
import scalaz.syntax.equal._

import net.liftweb.common.{Box, Empty, Full, Failure}

trait BoxInstances {
  implicit def BoxMonad: Monad[Box] = new Monad[Box] {
    def point[A](a: => A) = Full(a)
    def bind[A, B](box: Box[A])(f: A => Box[B]) = box flatMap f
  }

  private def FailureEqual: Equal[Failure] = new Equal[Failure] {
    def equal(a1: Failure, a2: Failure) =
      a1.msg == a2.msg &&
      BoxEqual(Equal.equalA[Throwable]).equal(a1.exception, a2.exception) &&
      BoxEqual(FailureEqual).equal(a1.chain, a2.chain)
  }

  implicit def BoxEqual[A : Equal]: Equal[Box[A]] = new Equal[Box[A]] {
    def equal(a1: Box[A], a2: Box[A]) = (a1, a2) match {
      case (Full(v1), Full(v2)) => v1 === v2
      case (Empty, Empty) => true
      case (f1: Failure, f2: Failure) => FailureEqual.equal(f1, f2)
      case _ => false
    }
  }
}

trait BoxFunctions {
  final def full[A](a: A): Box[A] = Full(a)
  final def empty[A]: Box[A] = Empty
}
