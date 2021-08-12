package main

import io.circe.Json

object ResultEnricher {
  val fieldToEnrich = List("organisation_id", "submitter_id", "assignee_id")
  def apply(result: List[Json], orgIndex: DocumentIndex, userIndex: DocumentIndex, ticketIndex: DocumentIndex) : List[Json] = ???
}