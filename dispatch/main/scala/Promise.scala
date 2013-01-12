/*
 * #%L
 * Instancez
 * %%
 * Copyright (C) 2012 Instancez
 * %%
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
 * #L%
 */
package scalaz.contrib
package dispatch

import scalaz._

import _root_.dispatch.{Promise, Http, HttpExecutor}

trait PromiseInstances {

  def promiseInstance(http: HttpExecutor) = new Traverse[Promise] with Monad[Promise] {
    def traverseImpl[G[_]: Applicative, A, B](fa: Promise[A])(f: (A) => G[B]): G[Promise[B]] =
      Applicative[G].map(f(fa()))(http.promise(_: B))
    def point[A](a: => A) = http.promise(a)
    def bind[A, B](fa: Promise[A])(f: (A) => Promise[B]) = fa flatMap f
  }

  implicit def defaultPromiseInstance: Traverse[Promise] with Monad[Promise] = promiseInstance(Http)

}

object promise extends PromiseInstances
