name := "MySearchEngine"

version := "0.1"

scalaVersion := "2.13.6"

val scalaLoggingVersion = "3.9.4"
val circeVersion = "0.14.0"
val catsVersion = "2.6.1"
val catsEffectVersion = "3.1.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic"% circeVersion,
  "io.circe" %% "circe-parser"% circeVersion,
  "org.typelevel"%% "cats-core"% catsVersion,
  "org.typelevel"%% "cats-effect"% catsEffectVersion,
  "org.scalatest" %% "scalatest" % "3.2.9" % "test",
  "com.typesafe.scala-logging"  %% "scala-logging"% scalaLoggingVersion,

)