name := "scalaplotlib"
organization := "io.github.jeremyrsmith"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

crossScalaVersions := Seq(
  "2.11.8", "2.11.11", "2.12.4"
)

libraryDependencies ++= Seq(
  "org.jfree" % "jfreechart" % "1.0.19",
  "org.jfree" % "jfreesvg" % "3.2",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.apache.spark" %% "spark-sql" % "2.0.0" % "provided"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-P:splain:color:false"
)

addCompilerPlugin("io.tryp" %% "splain" % "0.2.6")


        