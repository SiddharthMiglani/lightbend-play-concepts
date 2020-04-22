package controllers

import play.api.libs.json._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import services.CovidStatsService

@Singleton
class CovidController @Inject()(val controllerComponents: ControllerComponents) extends BaseController{

  def statsAsTxt(country: String) = Action { implicit request: Request[AnyContent] =>
    {
      val serviceResponse = CovidStatsService.getStatsAsTuple(country)
      Ok(Json.obj(
        "Country" -> serviceResponse._1,
        "Confirmed" -> serviceResponse._2,
        "Recovered" -> serviceResponse._3,
        "Deaths" -> serviceResponse._4,
        "Last updated at" -> serviceResponse._5
      ))
    }
  }

  def statsForUI(country: String) = Action { implicit request: Request[AnyContent] =>
    val serviceResponse = CovidStatsService.getStatsAsTuple(country)
    Ok(views.html.covid(serviceResponse._1, serviceResponse._2, serviceResponse._3, serviceResponse._4, serviceResponse._5))
  }
}
