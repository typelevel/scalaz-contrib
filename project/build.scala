import sbt._
import Keys._

object ScalazContribBuild extends Build {

  lazy val standardSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.typelevel",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.10.0-RC3",
    crossVersion := CrossVersion.full,
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-feature", "-language:implicitConversions", "-language:higherKinds"
    ),
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % "7.0.0-M5" cross CrossVersion.full
    ),
    sourceDirectory <<= baseDirectory(identity)
  )

  lazy val scalazContrib = Project(
    id = "scalaz-contrib",
    base = file("."),
    settings = standardSettings,
    aggregate = Seq(scala210)
  )

  lazy val scala210 = Project(
    id = "scala210",
    base = file("scala210"),
    settings = standardSettings ++ Seq(
      name := "scalaz-contrib-210",
      libraryDependencies ++= Seq(
        "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" cross CrossVersion.full,
        "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.0-M5" % "test" cross CrossVersion.full,
        "org.specs2" %% "specs2" % "1.12.3" % "test" cross CrossVersion.full
      )
    )
  )

}

// vim: expandtab:ts=2:sw=2
