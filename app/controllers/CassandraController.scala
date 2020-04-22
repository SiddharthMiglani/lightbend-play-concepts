package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import com.datastax.oss.driver.api.core._
import play.api.libs.json.Json

@Singleton
class CassandraController @Inject()(val controllerComponents: ControllerComponents) extends BaseController{

  def fetchVersion = Action { implicit request: Request[AnyContent] =>
    {
      val session = CqlSession.builder().build()
      val rs = session.execute("select release_version from system.local")
      val row = rs.one()
      Ok(Json.obj(
        "Fetched Value" -> row.getString("release_version")
      ))
    }
  }

  def fetchFromKeySpace() = Action { implicit request: Request[AnyContent] =>
  {
    val session = CqlSession.builder().withKeyspace(CqlIdentifier.fromCql("some_keyspace")).build()
    val rs = session.execute("select * from some_table where identifier = 'pk1'")
    val row = rs.one()
    Ok(Json.obj(
      "Fetched Value" -> row.getString("column_3")
    ))
  }
  }

}
