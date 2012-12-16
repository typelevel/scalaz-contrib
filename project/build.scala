import sbt._
import Keys._

object ScalazContribBuild extends Build {

  val scalazVersion = "7.0.0-M6"

  val specs2 = "org.specs2" %% "specs2" % "1.12.3" % "test" cross CrossVersion.full
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" cross CrossVersion.full

  lazy val standardSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.typelevel",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.10.0-RC5",
    crossVersion := CrossVersion.full,
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-feature", "-language:implicitConversions", "-language:higherKinds"
    ),
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % scalazVersion cross CrossVersion.full
    ),
    sourceDirectory <<= baseDirectory(identity)
  )

  lazy val scalazContrib = Project(
    id = "scalaz-contrib",
    base = file("."),
    settings = standardSettings,
    aggregate = Seq(scala210, validationExtension)
  )

  lazy val scala210 = Project(
    id = "scala210",
    base = file("scala210"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-210",
      libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test" cross CrossVersion.full,
        specs2,
        scalacheck
      )
    )
  )

  lazy val validationExtension = Project(
    id = "validation-ext",
    base = file("validation-ext"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-validation",
      libraryDependencies += specs2
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
