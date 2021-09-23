package main

sealed trait DocumentType
case object Ticket extends DocumentType
case object Org extends DocumentType
case object User extends DocumentType
