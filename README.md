scalaz-contrib
==============

Interoperability libraries &amp; additional data structures and instances for Scalaz

[![Build Status](https://travis-ci.org/larsrh/scalaz-contrib.png?branch=master)](http://travis-ci.org/larsrh/scalaz-contrib)


Usage
-----

This library is currently available for Scala 2.10 only.

To use the latest snapshot, include the following in your `build.sbt`:

```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.typelevel" % "scalaz-contrib-210" % "0.1-SNAPSHOT",
  "org.typelevel" % "scalaz-contrib-validation" % "0.1-SNAPSHOT",
  "org.typelevel" % "scalaz-dispatch" % "0.1-SNAPSHOT"
)
```

Examples
--------

### Scala 2.10

You can now use type class instances for new data types in the standard library:

```scala
scala> import scalaz._
import scalaz._

scala> import scalaz.contrib.std.utilTry._
import scalaz.contrib.std.utilTry._

scala> Monad[scala.util.Try]
res1: scalaz.Monad[scala.util.Try] = scalaz.contrib.std.TryInstances1$$anon$1@19ae3dd5
```

### Validation DSL

There are a couple of useful validators and converters, as well as a DSL for checking and transforming values.

```scala
import scalaz.contrib.Checker
import scalaz.contrib.validator.all._, scalaz.contrib.converter.all._
import scalaz.std.list._

val c = Checker.check("2012-12-20".toList)

scala> c.checkThat(notEmpty("must be non-empty")(_)).
     |   map(_.mkString("")).
     |   convertTo(date("yyyy-MM-dd", "must be a valid date")).
     |   toValidation
res0: Validation[NonEmptyList[String],Date] = Success(Thu Dec 20 00:00:00 CET 2012)

scala> c.checkThat(notEmpty("must be non-empty")(_)).
     |   map(_.mkString("")).
     |   convertTo(uuid("must be a valid UUID")).
     |   toValidation
res0: Validation[NonEmptyList[String],Date] = Failure(NonEmptyList(must be a valid UUID))
```

### Library bindings

This project provides bindings (instances) for the following libraries:

* Dispatch Reboot 0.9.5

There are more to come, so stay tuned!
