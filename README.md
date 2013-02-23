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
  "org.typelevel" %% "scalaz-contrib-210" % "0.1.1",
  "org.typelevel" %% "scalaz-contrib-validation" % "0.1.1",
  "org.typelevel" %% "scalaz-contrib-undo" % "0.1.1",
  "org.typelevel" %% "scalaz-dispatch" % "0.1.1",
  "org.typelevel" %% "scalaz-spire" % "0.1.1"
)
```

For the in-progess features, use the following:

```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

and depend on version `0.2-SNAPSHOT` instead of `0.1.1`.

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
* spire 0.3.0
* Lift 2.5-M4

There are more to come, so stay tuned!

#### spire

Spire provides powerful abstractions for numeric programming in Scala, including a full-stack hierarchy of algebraic type classes such as `Semigroup`, `Monoid`, and `Ring`. Scalaz only has the former two, but instead lots of instances. This library provides mappings between type classes where it makes sense.

There are two modes of conversion, *manual* and *automatic*:

* Importing `scalaz.contrib.spire._` enables the *manual* mode. It adds the methods `asSpire` and `asScalaz` on type class instances.
* Importing either `scalaz.contrib.spire.conversions.toSpire._` or `scalaz.contrib.spire.conversions.toScalaz._` (importing both will lead to anarchy) enables the *automatic* mode. It provides implicit conversions for type class instances. This mode does not provide conversions for two-operator classes (i.e. for `Rig` and `Semiring`).

It is possible (but not recommended) to enable both modes.

To understand which conversions "make sense", consider the kinds of type classes offered by scalaz and spire:

* Scalaz provides only one-operator classes, namely `Semigroup` and `Monoid`. To make a distinction between additive and multiplicative operations, scalaz uses _tags_. Hence, a `Semigroup[A]` denotes an unspecified operation, and `Semigroup[A @@ Multiplication]` a multiplicative operation.
* Spire provides one- and two-operator classes. The one-operator classes come in three flavours, e.g. `Semigroup`, `AdditiveSemigroup` and `MultiplicativeSemigroup` (same for `Monoid`, ...). As in scalaz, a plain `Semigroup` conveys nothing about the type of operation, whereas the other two should be used for additive and multiplicative operations, respectively. Spire's two-operator classes inherit from the additive and multiplicative variants, e.g. `Semiring` extends `AdditiveSemigroup` and `MultiplicativeSemigroup`. Also, these classes should guarantee that these two distinct operations relate to each other.

Thus, in *manual* mode, the following conversions are available:

* from scalaz one-operator to spire one-operator:
  ```scala
  S.asSpire // Semigroup → Semigroup
  S.asSpireAdditive // ... → AdditiveSemigroup
  S.asSpireMultiplicative // ... → MultiplicativeSemigroup

  SMult.asSpire // Semigroup @@ Multiplication → MultiplicativeSemigroup
  ```
* from spire one-operator to scalaz one-operator:
  ```scala
  // Semigroup and AdditiveSemigroup → Semigroup
  // MultiplicativeSemigroup → Semigroup @@ Multiplication
  S.asScalaz
  ```

These operations are also available in *automatic* mode, without the need to call `asXY`.

* from scalaz one-operator to spire two-operator:
  ```scala
  // (Semigroup, Semigroup @@ Multiplication) → Semiring
  (S, SMult).asSpire
  ```

The other direction is for free since the two-operator classes extend the one-operator classes. This operation is not available in *automatic* mode, since there is no guarantee that two independent scalaz instances obey the necessary laws to form a two-operator class.

Of course, the above mentioned conversions also work for `Monoid` and `Rig`.
