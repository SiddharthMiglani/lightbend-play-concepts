package models

import play.api.libs.json._
case class Application(id: Int, name: String)

object Application {
  implicit val applicationJsonFormat: Format[Application] = Json.format[Application]
}
