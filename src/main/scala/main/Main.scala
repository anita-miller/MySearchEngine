package main

import io.circe.Json

class Main {

  trait BareBonesBinarySearchTree {
    def insert(key:Int)
    def search(key:Int):Boolean
  }

  def foo() = {
    val json =
      """[
        |  {
        |    "_id": "436bf9b0-1147-4c0a-8439-6f79833bff5b",
        |    "url": "http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json",
        |    "external_id": "9210cdc9-4bee-485f-a078-35396cd74063",
        |    "created_at": "2016-04-28T11:19:34 -10:00",
        |    "type": "incident",
        |    "subject": "A Catastrophe in Korea (North)",
        |    "description": "Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum.",
        |    "priority": "high",
        |    "status": "pending",
        |    "submitter_id": 38,
        |    "assignee_id": 24,
        |    "organization_id": 116,
        |    "tags": [
        |      "Ohio",
        |      "Pennsylvania",
        |      "American Samoa",
        |      "Northern Mariana Islands"
        |    ],
        |    "codes": [
        |     1,2,3
        |    ],
        |    "has_incidents": false,
        |    "due_at": "2016-07-31T02:37:50 -10:00",
        |    "via": "web"
        |  }
        |]
        |""".stripMargin
    //
    //    val items = io.circe.parser.parse(json).right.get
    //    val userByIdIndex = createIndex(List(items), field = "_id")
    //    println(userByIdIndex.search(userByIdIndex, "436bf9b0-1147-4c0a-8439-6f79833bff5b"))
  }

}

// Option 1 - Search works on specific case classes
// Pros
// First thing we thought of
// Cons
// Need model case classes
// Need to write circe decoders for all fields
// Call per field to create index

// Option 2 - Search just works on Json data structure
// Pros
// it works for all of our inputs (org, user, ....)
// No modelling / decoding
// Search decoupled from our models
// Cons
// Decode the json for each search (not once on startup)
// Trade off
// Can index users which are invalid, discovered on search rather than startup