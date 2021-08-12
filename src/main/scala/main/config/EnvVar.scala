package main.config

import error.AppError
import error.AppError.configError

object EnvVar {
  private val defaults = Map(
    "APP_NAME" -> "search-cli-dev",
    "USER_FILENAME" -> "/users.json",
    "TICKET_FILENAME" -> "/tickets.json",
    "ORG_FILENAME" -> "/organizations.json"
  )

  def searchEnvVar(configEntry: String): Either[AppError, String] = {
    defaults.get(configEntry) match {
      case Some(config) => Right[AppError, String](config)
      case None => Left[AppError, String](configError(configEntry))
    }
  }
}
