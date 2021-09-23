package main

import io.circe.Json
import io.circe.syntax.KeyOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class ResultEncricherSpec extends AnyFunSpec {

  val userIndex = IndexBuilder.createIndexes(List("_id", "name"), List(Json.obj("_id" := 456, "name" := "Anita")))
  val orgIndex = IndexBuilder.createIndexes(List("_id", "name"), List(Json.obj("_id" := 700, "name" := "Zendesk")))

  describe("ResultEnricher") {
    describe("enriches tickets") {
      it("enriches ticket documents") {
        val ticket = Json.obj("id" := "123", "submitter_id" := 456, "organization_id" := 700, "assignee_id" := 456)
        val enriched = ResultEnricher(List(ticket), Ticket, orgIndex, userIndex, userIndex)

        enriched shouldBe List(
          Json.obj(
            "id" := "123",
            "submitter_id" := 456,
            "submitter" := "Anita",
            "organization_id" := 700,
            "organization" := "Zendesk",
            "assignee_id" := 456,
            "assignee" := "Anita"
          )
        )
      }

      it("handles missing joins") {
        val ticket = Json.obj("id" := "123", "submitter_id" := 789, "organization_id" := 100, "assignee_id" := 12435)

        val enriched = ResultEnricher(List(ticket), Ticket, userIndex, userIndex, userIndex)

        enriched shouldBe List(
          Json.obj(
            "id" := "123",
            "submitter_id" := 789,
            "organization_id" := 100,
            "assignee_id" := 12435,
            "submitter" := "Could not find",
            "organization" := "Could not find",
            "assignee" := "Could not find"
          )
        )
      }
    }

    describe("enriches users") {
      it("enriches user documents") {
        val user = Json.obj("id" := "123", "organization_id" := 700)
        val enriched = ResultEnricher(List(user), User, orgIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "organization_id" := 700, "organization" := "Zendesk"))
      }

      it("handles missing joins") {
        val user = Json.obj("id" := "123", "organization_id" := 2222)
        val enriched = ResultEnricher(List(user), User, orgIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "organization_id" := 2222, "organization" := "Could not find"))
      }
    }
  }
}
