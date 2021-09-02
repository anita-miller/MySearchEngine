package main

import io.circe.{Json, JsonObject}

object ResultEnricher {
  val fieldToEnrich = List("organisation_id", "submitter_id", "assignee_id")
  def apply(result: List[Json], orgIndex: DocumentIndex, userIndex: DocumentIndex, ticketIndex: DocumentIndex) : List[Json] = {
    // go through result => base on which field => choose the index
    result.map {jsonObject =>
      val listOfValues: Seq[String] = indexDocument(jsonObject, "submitter_id") match {
        case Some(jsonObj) => jsonToListofString(jsonObj)
        case None => List("")
      }
      val correspondingUserJson = listOfValues.flatMap { id =>
        userIndex.index.get("_id") match {
          case Some(index) => index.search(id)
          case None => List.empty
        }
      }
      val maybeUserName: Option[String] = correspondingUserJson.headOption.flatMap { jsonObjectAgain =>
        jsonObjectAgain.hcursor.get[String]("name").toOption
      }
      val maybejson = maybeUserName match {
        case Some(userName) => jsonObject.asObject.map(json => json.add("submitter", Json.fromString(userName)))
        case None =>jsonObject.asObject.map(json => json.add("submitter", Json.fromString("Could not find")))
      }
      maybejson match {
        case Some(json) => Json.fromJsonObject(json)
        case None =>Json.fromJsonObject(JsonObject.empty)
      }
    }
  }

  private def jsonToListofString(jsonval: Json): List[String] = {
    jsonval.fold(
      List.empty,
      boolVal => List(boolVal.toString),
      numval => List(numval.toString),
      stringval => List(stringval),
      arrayVal => arrayVal.toList.flatMap(jsondoc => jsonToListofString(jsondoc)),
      objectVal => List(objectVal.toString)
    )
  }

  private def indexDocument(document: Json, field: String): Option[Json] = {
    document.asObject match {
      case Some(jsonObject) => jsonObject.apply(field)
      case None => None
    }
  }
}