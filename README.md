scalaz-contrib
==============

Interoperability libraries &amp; additional data structures and instances for Scalaz

[![Build Status](https://travis-ci.org/larsrh/scalaz-contrib.png?branch=master)](http://travis-ci.org/larsrh/scalaz-contrib)


Usage
-----

This library is currently available for Scala 2.10 only.

To use the latest snapshot, include the following in your `build.sbt`:

```scala
scalaVersion := "2.10.0-RC5"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.typelevel" % "scalaz-contrib-210" % "0.1-SNAPSHOT" cross CrossVersion.full,
  "org.typelevel" % "scalaz-contrib-validation" % "0.1-SNAPSHOT" cross CrossVersion.full
)
```

For example, you can now use type class instances for new data types in Scala 2.10:

```scala
scala> import scalaz._
import scalaz._

scala> import scalaz.contrib.std.utilTry._
import scalaz.contrib.std.utilTry._

scala> Monad[scala.util.Try]
res1: scalaz.Monad[scala.util.Try] = scalaz.contrib.std.TryInstances1$$anon$1@19ae3dd5
```
