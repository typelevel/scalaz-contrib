import sbt._
import Keys._

object ScalazContribBuild extends Build {

  val scalazVersion = "7.0.0-M7"

  val specs2 = "org.specs2" %% "specs2" % "1.12.3" % "test"
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
  val scalazSpecs2 = "org.typelevel" %% "scalaz-specs2" % "0.1-SNAPSHOT" % "test"
  val scalazScalacheck = "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"

  lazy val standardSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.typelevel",
    version := "0.1-SNAPSHOT",

    scalaVersion := "2.10.0",
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-feature", "-language:implicitConversions", "-language:higherKinds"
    ),

    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % scalazVersion
    ),
    resolvers += Resolver.sonatypeRepo("snapshots"),

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
    pomIncludeRepository := Function.const(false),
    pomExtra :=
      <url>http://typelevel.org/scalaz</url>
        <licenses>
          <license>
            <name>MIT</name>
            <url>http://www.opensource.org/licenses/mit-license.php</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
            <url>https://github.com/larsrh/scalaz-contrib</url>
            <connection>scm:git:git://github.com/larsrh/scalaz-contrib.git</connection>
            <developerConnection>scm:git:git@github.com:larsrh/scalaz-contrib.git</developerConnection>
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
        </developers>
  )

  lazy val scalazContrib = Project(
    id = "scalaz-contrib",
    base = file("."),
    settings = standardSettings ++ Seq(
      publishArtifact := false
    ),
    aggregate = Seq(scala210, validationExtension)
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
