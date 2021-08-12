package error

sealed trait AppError {
  def getMessage: String
}

object AppError {
  case class invalidFieldError(field: String) extends AppError {
    override def getMessage: String = s"Requested field $field not found "
  }
  case class configError(configEntry: String) extends AppError {
    override def getMessage: String = s"Invalid app config: $configEntry"
  }
}
