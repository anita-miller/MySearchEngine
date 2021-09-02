package main

import io.circe.Json
import io.circe.syntax.KeyOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class ResultEncricherSpec  extends AnyFunSpec{

  val userIndex = IndexBuilder.createIndexes(List("_id", "name"), List(Json.obj("_id" := 456, "name":= "Anita")))

  describe("ResultEnricher") {
    describe("enriches tickets") {
      it("enriches ticket documents") {
        val ticket = Json.obj("id" := "123", "submitter_id" := 456)
        val enriched = ResultEnricher(List(ticket), userIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "submitter_id" := 456, "submitter" := "Anita"))
      }

      it("handles missing joins") {
        val ticket = Json.obj("id" := "123", "submitter_id" := 789)

        val enriched = ResultEnricher(List(ticket), userIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "submitter_id" := 789, "submitter" := "Could not find"))
      }
    }

    describe("enriches users") {
      it("enriches user documents") {
        val user = Json.obj("id" := "123", "organization_id" := 1111)
        val enriched = ResultEnricher(List(user), userIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "organization_id" := 1111, "organization" := "Zendesk"))
      }

      it("handles missing joins") {
        val user = Json.obj("id" := "123", "organization_id" := 2222)
        val enriched = ResultEnricher(List(user), userIndex, userIndex, userIndex)

        enriched shouldBe List(Json.obj("id" := "123", "organization_id" := 2222, "organization" := "Could not find"))
      }
    }
  }
}
