package main

import io.circe.Json
import io.circe.syntax.KeyOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class ResultEncricherSpec  extends AnyFunSpec{

  val userIndex = DocumentIndex (Map("_id" -> 456, "name" -> "Anita"), List("_id", "name"))
  describe("ResultEnricher") {
    it("enriches ticket documents") {
      val ticket = Json.obj("id" := "123", "submitter_id":= 456)

      val enriched = ResultEnricher(List(ticket))

      enriched shouldBe List(Json.obj("id" := "123", "submitter_id":= 456,"submitter":= "Anita"))
    }
  }
}
