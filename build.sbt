name := "MySearchEngine"

version := "0.1"

scalaVersion := "2.13.6"

val circeVersion = "0.14.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic"% circeVersion,
  "io.circe" %% "circe-parser"% circeVersion,
  "org.scalatest" %% "scalatest" % "3.2.9" % "test"
)