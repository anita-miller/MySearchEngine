package main.config

import error.Errors
import error.Errors.someError

object EnvVar {
  private val defaults = Map(
    "APP_NAME" -> "search-cli-dev",
    "USER_FILENAME" -> "/users.json",
    "TICKET_FILENAME" -> "/tickets.json",
    "ORG_FILENAME" -> "/organizations.json"
  )

  def searchEnvVar(name: String): Either[Errors, String] ={
    defaults.get(name) match {
      case Some(value) => Right[Errors, String](value)
      case None => Left[Errors, String](someError("Invalid Config"))
    }
  }
}
