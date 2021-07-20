package main

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.Logger
import main.config.Config

object Main extends IOApp  {
  private lazy val logger = Logger[Main.type]

  override def run(args: List[String]): IO[ExitCode] =
    Config.fromEnvironment() match {
      case Right(config) => start(config)
      case Left(error) => IO(logger.error("Failed to run")).map(_ => ExitCode.Error)
    }

  private def start(config: Config): IO[ExitCode] = {
    Application(config).run()
      .map { _ => logger.info("starting with config") }
      .map(_ => ExitCode.Success)
  }
}

// Option 1 - Search works on specific case classes
// Pros
// First thing we thought of
// Cons
// Need model case classes
// Need to write circe decoders for all fields
// Call per field to create index

// Option 2 - Search just works on Json data structure
// Pros
// it works for all of our inputs (org, user, ....)
// No modelling / decoding
// Search decoupled from our models
// Cons
// Decode the json for each search (not once on startup)
// Trade off
// Can index users which are invalid, discovered on search rather than startup