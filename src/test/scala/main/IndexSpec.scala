package main

import io.circe.Json
import io.circe.syntax.KeyOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class IndexSpec extends AnyFunSpec {

  it("returns Map of String and Json given a list of Json "){

    val matchingDocument = Json.obj("_id" := "123", "name" := "hello")
    val nonMatchingDocument =  Json.obj("_id" := "456", "name" := "hello")

    val index = Index.createIndex(List(matchingDocument, nonMatchingDocument),  "_id")
    index.search("123") shouldBe List(matchingDocument)
  }

  it("returns Map of String and Json if Json includes int as value") {
    val inputDocument = Json.obj("submitter_id":= 38)
    val index = Index.createIndex(List(inputDocument),  "submitter_id")

    index.search("38") shouldBe List(inputDocument)
  }

  it("returns Map of String and Json if Json includes boolean as value") {
    val inputDocument = Json.obj("has_incidents":= false)
    val index = Index.createIndex(List(inputDocument),  "has_incidents")

    index.search("false") shouldBe List(inputDocument)
  }

  it("returns Map of String and Json if Json includes list as value") {
    val inputDocument = Json.obj("tags":= List( "Ohio",
      "Pennsylvania",
      "American Samoa",
      "Northern Mariana Islands"
    ))
    val index = Index.createIndex(List(inputDocument),  "tags")

    index.search("Ohio") shouldBe List(inputDocument)
  }

  it("handles multiple matches"){

    val matchingDocument1 = Json.obj("_id" := "123", "name" := "hello")
    val matchingDocument2 =  Json.obj("_id" := "456", "name" := "hello")

    val index = Index.createIndex(List(matchingDocument1, matchingDocument2),  "name")
    index.search("hello") shouldBe List(matchingDocument1, matchingDocument2)
  }

  it("handles missing fields"){
    val matchingDocument = Json.obj("_id" := "123")
    val notMatchingDocument =  Json.obj("_id" := "456", "url" := "http://foo.com")

    val index = Index.createIndex(List(matchingDocument, notMatchingDocument),  "url")
    index.search("") shouldBe List(matchingDocument)

  }
  //  it("does something with unexpected json") {
  //    Index.createIndex(List(Json.fromBoolean(false)),  "_id")
  //  }
}