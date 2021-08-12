package error

sealed trait Errors extends Exception

object Errors {
  final case class someError(value: String) extends Errors

}
