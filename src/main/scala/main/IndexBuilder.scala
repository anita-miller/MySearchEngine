package main

import io.circe.Json
import main.Index.createIndex

import java.io.InputStream
import scala.io.Source

case class DocumentIndex(index: Map[String, Index], fields: List[String])

object IndexBuilder {
  def createIndexes(fields: List[String], fileName: String): DocumentIndex = {
    val fileStream: InputStream = getClass.getResourceAsStream(fileName)
    val rawJson: String = Source.fromInputStream(fileStream).getLines.mkString.stripMargin
    val Items: Option[Vector[Json]] = io.circe.parser.parse(rawJson).right.get.asArray
    Items match {
      case Some(value) =>
        createIndexes(fields, value.toList)
      case None => DocumentIndex(Map.empty, fields) //TODO let them know what happened, add message log or sth
    }
  }

  def createIndexes(fields: List[String], documents: List[Json]): DocumentIndex = {
    val index = fields.map(field => (field, createIndex(documents, field = field))).toMap
    DocumentIndex(index, fields)
  }
}
