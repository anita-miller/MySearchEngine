package main

import cats.effect.IO
import main.config.Config
import scala.io.StdIn.readLine

class Application(
                   userFilename: String,
                   ticketFilename: String,
                   orgFilename: String
                 )  {
  private val fieldsOfdOrganization = List("_id", "url", "external_id", "name", "domain_names", "created_at", "details","shared_tickets","tags")
  private val fieldsOfUser = List("_id", "url", "external_id", "name", "alias", "created_at", "active", "verified", "shared", "locale", "timezone", "last_login_at", "email", "phone", "signature", "organization_id", "tags", "suspended", "role")
  private val fieldsOfTicket = List("_id", "url", "external_id", "created_at", "type", "subject", "description", "priority", "status", "submitter_id", "assignee_id", "organization_id", "tags", "has_incidents", "due_at", "via")

  private val userIndexes = IndexBuilder.createIndexes(fieldsOfUser, userFilename)
  private val orgIndexes: Map[String, Index] = IndexBuilder.createIndexes(fieldsOfdOrganization,orgFilename)
  private val ticketIndexes = IndexBuilder.createIndexes(fieldsOfTicket, ticketFilename)

  private val searchCategory = readLine("Select 1 Users, 2 org and 3 tickets")
  private val searchField = readLine("Enter search term")
  private val searchValue = readLine("Enter search value")


  def run():IO[Unit] = {
    val Indexes: Option[Map[String, Index]] = searchCategory match {
      case "1" => Some(userIndexes)
      case "2" => Some(orgIndexes)
      case "3" => Some(ticketIndexes)
      case _ => None
    }

    Indexes match {
      case Some(index) => index.get(searchField) match {
        case Some(index) => IO(println(index.search(searchValue)))
        case None => IO(println("field didn't exist"))
      }
      case None => IO(println("try again"))
    }
  }
}
object Application{

  def apply(config: Config): Application = {
    new Application(
      userFilename = config.userFilename,
      ticketFilename = config.ticketFilename,
      orgFilename = config.orgFilename
    )
  }
}