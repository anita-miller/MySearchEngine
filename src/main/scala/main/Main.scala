package main

import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]): Unit = {
    val listFieldsdOrg = List("_id", "url", "external_id", "name", "domain_names", "created_at", "details","shared_tickets","tags")

    val searchCategory = readLine("Select 1 Users, 2 org and 3 tickets")
    val searchField = readLine("Enter search term")
    val searchValue = readLine("Enter search value")

    val orgIndexes: Map[String, Index] = IndexBuilder.createIndexes(listFieldsdOrg)
    orgIndexes.get(searchField) match {
      case Some(index) => println(index.search(searchValue))
      case None => println("field didn't exist")
    }
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