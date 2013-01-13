scalaz-contrib
==============

Interoperability libraries &amp; additional data structures and instances for Scalaz

[![Build Status](https://travis-ci.org/larsrh/scalaz-contrib.png?branch=master)](http://travis-ci.org/larsrh/scalaz-contrib)


Usage
-----

This library is currently available for Scala 2.10 only.

To use the latest version, include the following in your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.typelevel" %% "scalaz-contrib-210" % "0.1",
  "org.typelevel" %% "scalaz-contrib-validation" % "0.1",
  "org.typelevel" %% "scalaz-contrib-undo" % "0.1",
  "org.typelevel" %% "scalaz-dispatch" % "0.1"
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
res1: Validation[NonEmptyList[String],Date] = Failure(NonEmptyList(must be a valid UUID))
```

### Undo

Originally by Gerolf Seitz (@gseitz).

```scala
import scalaz.contrib.undo.UndoT
import UndoT._
import scalaz.std.option._

val result = for {
  one           <- hput[Option, Int](1)
  two           <- hput[Option, Int](2)
  three         <- hput[Option, Int](3)
  twoAgain      <- undo[Option, Int]
  four          <- hput[Option, Int](4)
  twoAgainAgain <- undo[Option, Int]
  fourAgain     <- redo[Option, Int]
} yield ()

scala> result.exec(1)
res0: Option[Int] = Some(4)
```

### Library bindings

This project provides bindings (instances) for the following libraries:

* Dispatch Reboot 0.9.5

There are more to come, so stay tuned!
