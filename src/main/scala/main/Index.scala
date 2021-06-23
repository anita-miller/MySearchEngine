package main

import io.circe.Json
import io.circe.Json.JArray

class Index private (val rawIndex: Map[String, List[Json]]) {
  def search(value: String): List[Json] = rawIndex.getOrElse(value, List.empty)
}

object Index {
  def createIndex(documents: List[Json], field: String): Index = {
    val pairs: List[(String, Json)] = documents.flatMap { document =>
      indexDocument(document, field) match {
        case Some(jsonval) => jsonToListofString(jsonval).map(stringval => (stringval, document))
        // List[String] -> List[(String, Json)]
        // List[X] -> List[Y]  // map(f : X => Y)

        // List[X] -> Y // fold(z: Y)(f: (X, Y) -> Y)
        case None => ???
      }
    }

    // List[A] - A => F[B] - F[List[B]]

    // List[Json] .map(f: Json => (String, Json))  // List[(String, Json)]
    // after
    // List[Json] .map(f: Json => List[(String, Json)]) // List[List[(String, Json)]]

    // List[A].map(f: A => B) // List[B]
    // List[A].XXXX(f: A => List[B]) // List[B]

    new Index(pairs.groupMap(_._1)(_._2))
  }
  //[[("ohio" , json)], [("pen", json)]]
  // Json => ( -> document)
  //  Json.fromString(List( "Ohio",
  //        "Pennsylvania",
  //       "American Samoa",
  //        "Northern Mariana Islands"
  //     )
  //[["Ohio"," pen", "]

  // Json => List[String]
  private def jsonToListofString (jsonval: Json) :List[String] = {
    jsonval.fold(
      List.empty,
      boolVal => List(boolVal.toString),
      numval => List(numval.toString),
      stringval => List(stringval),
      arrayVal => arrayVal.toList.flatMap(jsondoc => jsonToListofString(jsondoc)),
      objectVal => List(objectVal.toString)
    )
  }
  // Json.obj("_id" := "123", "name" := "hello", "nested" -> Json.obj("foo" := "bar"))
  // Option(Json.frmoString("123"))

  //Json.obj("tags":= List( "Ohio",
  //      "Pennsylvania",
  //      "American Samoa",
  //      "Northern Mariana Islands"
  //    ))
  //Option(Json.fromString(List( "Ohio",
  //        "Pennsylvania",
  //       "American Samoa",
  //        "Northern Mariana Islands"
  //     ))
  private def indexDocument(document: Json, field: String): Option[Json] = {
    document.asObject match {
      case Some(jsonObject) => jsonObject.apply(field)
      case None => None
    }
  }

  // "_id" => Json("123")
  // String => Json
}