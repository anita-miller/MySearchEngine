package main

import io.circe.{Json, JsonObject}

object ResultEnricher {
  case class JointIndex(documentIndex: DocumentIndex, sourceFieldId: String, targetFieldId: String, targetJoinName: String, label: String)

  def apply(result: List[Json], typeOfDocument: DocumentType, orgIndex: DocumentIndex, userIndex: DocumentIndex, ticketIndex: DocumentIndex) : List[Json] = {
    val joins = typeOfDocument match {
      case Ticket =>  List(
          JointIndex(orgIndex, "organization_id", "_id", "name", "organization"),
          JointIndex(userIndex, "submitter_id", "_id", "name", "submitter"),
          JointIndex(userIndex, "assignee_id", "_id", "name", "assignee")
        )
      case User => List(JointIndex(orgIndex, "organization_id", "_id", "name", "organization"))
      case Org => List.empty
    }
    result.map(enrichDocument(joins))
  }

  private def enrichDocument(joins: List[JointIndex])(result: Json): Json = {
    joins.foldLeft(result){ (jsonObject, jointIndex) =>
      val listOfValues: List[String] = indexDocument(jsonObject, jointIndex.sourceFieldId) match { // source field
        case Some(jsonObj) => jsonToListofString(jsonObj)
        case None => List("")
      }
      val correspondingUserJson = listOfValues.flatMap { id =>
        jointIndex.documentIndex.index.get("_id") match {
          case Some(index) => index.search(id)
          case None => List.empty
        }
      }
      val maybeName: Option[String] = correspondingUserJson.headOption.flatMap { jsonObjectAgain =>
        jsonObjectAgain.hcursor.get[String](jointIndex.targetJoinName).toOption // descriptive field to fetch on target index
      }
      val maybejson = maybeName match {
        case Some(userName) => jsonObject.asObject.map(json => json.add(jointIndex.label, Json.fromString(userName))) // label for joined relationship
        case None => jsonObject.asObject.map(json => json.add(jointIndex.label, Json.fromString("Could not find")))
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
