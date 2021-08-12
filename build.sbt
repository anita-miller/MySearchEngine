name := "MySearchEngine"

version := "0.1"

scalaVersion := "2.13.6"

val scalaLoggingVersion = "3.9.4"
val circeVersion = "0.14.0"
val catsVersion = "2.6.1"
val catsEffectVersion = "3.1.1"
val logbackVersion = "1.2.3"
val scalaTestVersion = "3.2.9"

libraryDependencies ++= Seq(
  "io.circe"                    %%      "circe-core"         % circeVersion,
  "io.circe"                    %%      "circe-generic"      % circeVersion,
  "io.circe"                    %%      "circe-parser"       % circeVersion,
  "org.typelevel"               %%      "cats-core"          % catsVersion,
  "org.typelevel"               %%      "cats-effect"        % catsEffectVersion,
  "com.typesafe.scala-logging"  %%      "scala-logging"      % scalaLoggingVersion,
  "ch.qos.logback"              %      "logback-classic"    % logbackVersion,
  "org.scalatest"               %%      "scalatest"          % scalaTestVersion      %   "test",
)
addCommandAlias("verify", "; scalafmtCheckAll; strict test")