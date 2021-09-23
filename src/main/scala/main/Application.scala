package main

import cats.effect.IO
import error.AppError
import main.config.Config

class Application(
  userFilename: String,
  ticketFilename: String,
  orgFilename: String
) {
  private val fieldsOfdOrganization =
    List("_id", "url", "external_id", "name", "domain_names", "created_at", "details", "shared_tickets", "tags")
  private val fieldsOfUser = List(
    "_id",
    "url",
    "external_id",
    "name",
    "alias",
    "created_at",
    "active",
    "verified",
    "shared",
    "locale",
    "timezone",
    "last_login_at",
    "email",
    "phone",
    "signature",
    "organization_id",
    "tags",
    "suspended",
    "role"
  )
  private val fieldsOfTicket = List(
    "_id",
    "url",
    "external_id",
    "created_at",
    "type",
    "subject",
    "description",
    "priority",
    "status",
    "submitter_id",
    "assignee_id",
    "organization_id",
    "tags",
    "has_incidents",
    "due_at",
    "via"
  )

  private val userIndexes = IndexBuilder.createIndexes(fieldsOfUser, userFilename)
  private val orgIndexes = IndexBuilder.createIndexes(fieldsOfdOrganization, orgFilename)
  private val ticketIndexes = IndexBuilder.createIndexes(fieldsOfTicket, ticketFilename)

  private val consoleIO: Console[IO] = new Console.ConsoleIo()
  private val searchCategory = consoleIO.write("Select 1 Users, 2 org and 3 tickets").flatMap(_ => consoleIO.read())
  private val searchField = consoleIO.write("Enter search term").flatMap(_ => consoleIO.read())
  private val searchValue = consoleIO.write("Enter search value").flatMap(_ => consoleIO.read())

  def run(): IO[Unit] = {
    val indexes: IO[Option[(DocumentIndex, DocumentType)]] = searchCategory.map {
      case "1" => Some(userIndexes, User)
      case "2" => Some(orgIndexes, Org)
      case "3" => Some(ticketIndexes, Ticket)
      case _ => None
    }
    indexes.flatMap {
      case None => consoleIO.write("Invalid search category")
      case Some((index, documentType)) =>
        searchField.flatMap { searchField =>
          index.index.get(searchField) match {
            case Some(index) =>
              searchValue.flatMap { searchValue =>
                val resultOfSearch = index.search(searchValue)
                consoleIO.write(ResultEnricher.apply(resultOfSearch, documentType, orgIndexes, userIndexes, ticketIndexes).toString)
              }
            case None => consoleIO.write(AppError.invalidFieldError(searchField).getMessage)
          }
        }
    }
  }
}
object Application {
  def apply(config: Config): Application = {
    new Application(
      userFilename = config.userFilename,
      ticketFilename = config.ticketFilename,
      orgFilename = config.orgFilename
    )
  }
}
