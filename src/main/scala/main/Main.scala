package main

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.Logger
import main.config.Config

object Main extends IOApp {
  private lazy val logger = Logger[Main.type]

  override def run(args: List[String]): IO[ExitCode] =
    Config.fromEnvironment() match {
      case Right(config) => start(config)
      case Left(error) => IO(logger.info(s"Failed to start server ${error.getMessage}")).map(_ => ExitCode.Error)
    }

  private def start(config: Config): IO[ExitCode] = {
    for {
      _ <- IO(logger.info(s"starting with config $config"))
      _ <- Application(config).run()
    } yield ExitCode.Success
  }
}
