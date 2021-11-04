package main

import cats.effect.IO
import scala.io.StdIn.readLine

trait Console[F[_]] {
  def write(message: String): F[Unit]
  def read(): F[String]
}

object Console {
  object ConsoleIo extends Console[IO] {
    def write(message: String): IO[Unit] = IO(println(message))

    def read(): IO[String] = IO(readLine)
  }
}
