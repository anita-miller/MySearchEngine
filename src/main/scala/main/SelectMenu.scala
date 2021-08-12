//package main
//
//
//trait SearchMenuProgram[F[_]] {
//
//  def fieldNameDescription: List[String]
//
//  def getFieldsNamesFor(tableType: TableType): List[String]
//
//  def selectFieldNameString: String //
//
//  def selectFieldValueString: List[String]
//
//  def searchCriteriaString(tableType: TableType, fieldName: JsonFieldName, fieldValue: FieldValue): List[String]
//
//  def run(tableType: TableType): F[Continuation]
//
//  def createSearchQuery(tableType: TableType, jsonFieldName: JsonFieldName, fieldValue: FieldValue): SearchQuery
//}
//
//object SelectMenu {
//  override def fieldNameDescription: String = "You can search on the following fields: User, Tickets, Organization"
//
//  override def selectFieldValueString: List[String] =
//    List(
//      "",
//      "Please enter the field value you want to match on"
//    )
//
//  override def run = {
//    for {
//      _ <- fieldNameDescription
//    }
//  }
//}
