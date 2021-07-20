package main

import io.circe.Json
import main.Index.createIndex

import java.io.InputStream
import scala.io.Source

object IndexBuilder {
  def createIndexes(fields: List[String], fileName: String): Map[String, Index] = {
    val fileStream: InputStream = getClass.getResourceAsStream(fileName)
    val rawJson: String = Source.fromInputStream(fileStream).getLines.mkString.stripMargin
    val Items: Option[Vector[Json]] = io.circe.parser.parse(rawJson).right.get.asArray
    Items match {
      case Some(value) =>
        val fieldIndexes: Map[String, Index] = fields.map(field => (field, createIndex(value.toList, field = field))).toMap
        fieldIndexes
      case None => Map.empty //TODO let them know what happened, add message log or sth
    }
  }
}
