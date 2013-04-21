import sbt._
import Keys._

import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._

import com.typesafe.sbt.pgp.PgpKeys._

object ScalazContribBuild extends Build {

  val scalazVersion = "7.0.0"

  val specs2 = "org.specs2" %% "specs2" % "1.12.3" % "test"
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
  val scalazSpecs2 = "org.typelevel" %% "scalaz-specs2" % "0.1.4" % "test"
  val scalazScalacheck = "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"


  lazy val publishSignedArtifacts = ReleaseStep(
    action = st => {
      val extracted = st.extract
      val ref = extracted.get(thisProjectRef)
      extracted.runAggregated(publishSigned in Global in ref, st)
    },
    check = st => {
      // getPublishTo fails if no publish repository is set up.
      val ex = st.extract
      val ref = ex.get(thisProjectRef)
      Classpaths.getPublishTo(ex.get(publishTo in Global in ref))
      st
    },
    enableCrossBuild = true
  )

  lazy val standardSettings = Defaults.defaultSettings ++ releaseSettings ++ Seq(
    organization := "org.typelevel",

    licenses := Seq("MIT" → url("http://www.opensource.org/licenses/mit-license.php")),
    homepage := Some(url("http://typelevel.org/")),

    scalaVersion := "2.10.1",
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-feature", "-language:implicitConversions", "-language:higherKinds"
    ),

    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % scalazVersion
    ),

    resolvers += Resolver.sonatypeRepo("releases"),

    // https://github.com/sbt/sbt/issues/603
    conflictWarning ~= { cw =>
      cw.copy(filter = (id: ModuleID) => id.organization != "org.scala-lang", group = (id: ModuleID) => id.organization + ":" + id.name, level = Level.Error, failOnConflict = true)
    },

    sourceDirectory <<= baseDirectory(identity),

    publishTo <<= (version).apply { v =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("Snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("Releases" at nexus + "service/local/staging/deploy/maven2")
    },
    credentials += Credentials(
      Option(System.getProperty("build.publish.credentials")) map (new File(_)) getOrElse (Path.userHome / ".ivy2" / ".credentials")
    ),

    // adapted from sbt-release defaults
    // * does not perform `pushChanges`
    // * performs `publish-signed` instead of `publish`
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishSignedArtifacts,
      setNextVersion,
      commitNextVersion
    ),

    pomIncludeRepository := Function.const(false),
    pomExtra := (
      <scm>
          <url>https://github.com/typelevel/scalaz-contrib</url>
          <connection>scm:git:git://github.com/typelevel/scalaz-contrib.git</connection>
      </scm>
      <developers>
        <developer>
          <id>larsrh</id>
          <name>Lars Hupel</name>
          <url>https://github.com/larsrh</url>
        </developer>
        <developer>
          <id>OleTraveler</id>
          <name>Travis Stevens</name>
          <url>https://github.com/OleTraveler</url>
        </developer>
        <developer>
          <id>folone</id>
          <name>George Leontiev</name>
          <url>https://github.com/folone</url>
        </developer>
      </developers>
    )
  )

  lazy val scalazContrib = Project(
    id = "scalaz-contrib",
    base = file("."),
    settings = standardSettings ++ Seq(
      publishArtifact := false
    ),
    aggregate = Seq(scala210, dispatch, lift, spire, validationExtension, undo, nscalatime)
  )

  lazy val scala210 = Project(
    id = "scala210",
    base = file("scala210"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-210",
      libraryDependencies ++= Seq(
        specs2,
        scalazSpecs2,
        scalacheck,
        scalazScalacheck
      )
    )
  )

  lazy val dispatch = Project(
    id = "dispatch",
    base = file("dispatch"),
    settings = standardSettings ++ Seq(
      name := "scalaz-dispatch",
      libraryDependencies +=
        "net.databinder.dispatch" %% "dispatch-core" % "0.9.5"
    )
  )

  lazy val spire = Project(
    id = "spire",
    base = file("spire"),
    settings = standardSettings ++ Seq(
      name := "scalaz-spire",
      libraryDependencies ++= Seq(
        "org.spire-math" %% "spire" % "0.4.0-M3",
        "org.spire-math" %% "spire-scalacheck-binding" % "0.4.0-M3" % "test",
        scalazSpecs2,
        scalazScalacheck
      )
    )
  )

  lazy val lift = Project(
    id = "lift",
    base = file("lift"),
    settings = standardSettings ++ Seq(
      name := "scalaz-lift",
      libraryDependencies ++= Seq(
        "net.liftweb" %% "lift-common" % "2.5-RC2",
        scalazSpecs2,
        scalazScalacheck
      )
    )
  )

  lazy val nscalatime = Project(
    id = "nscala_time",
    base = file("nscala-time"),
    settings = standardSettings ++ Seq(
      name := "scalaz-nscala-time",
      libraryDependencies ++= Seq(
        "com.github.nscala-time" %% "nscala-time" % "0.2.0",
        scalazSpecs2,
        scalazScalacheck
      )
    )
  )

  lazy val validationExtension = Project(
    id = "validation-ext",
    base = file("validation-ext"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-validation",
      libraryDependencies ++= Seq(
        specs2,
        scalazSpecs2,
        scalacheck,
        scalazScalacheck
      )
    )
  )

  lazy val undo = Project(
    id = "undo",
    base = file("undo"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-undo",
      libraryDependencies += specs2
    )
  )

}

// vim: expandtab:ts=2:sw=2
