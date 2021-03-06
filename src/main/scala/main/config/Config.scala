package main.config

import error.AppError

object Config {
  def fromEnvironment(): Either[AppError, Config] = {
    for {
      appName <- EnvVar.searchEnvVar("APP_NAME")
      userFilename <- EnvVar.searchEnvVar("USER_FILENAME")
      ticketFilename <- EnvVar.searchEnvVar("TICKET_FILENAME")
      orgFilename <- EnvVar.searchEnvVar("ORG_FILENAME")
    } yield Config(
      appName = appName,
      userFilename = userFilename,
      ticketFilename = ticketFilename,
      orgFilename = orgFilename
    )
  }
}

final case class Config(appName: String, userFilename: String, ticketFilename: String, orgFilename: String)
