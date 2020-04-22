package services

import play.api.libs.json.{JsArray, Json}

import scala.collection.mutable.Map
import scala.concurrent.{ExecutionContext, Future}

object AppStoreMapService extends AppStoreService {

  var appStore: Map[Int, String] = Map(1 -> "DefaultApp")

  def ifAppExists(id: Int) = AppStoreMapService.appStore.contains(id)

  def getAppsAsync()(implicit ex: ExecutionContext) = {
    Future{
      convertToJsonArray(AppStoreMapService.appStore)
    }
  }

  def getAppAsync(id: Int)(implicit ex: ExecutionContext) = {
    Future{
      Json.obj(id.toString -> AppStoreMapService.appStore.get(id).get)
    }
  }

  def addOrUpdateApp(id: Int, name: String)(implicit ex: ExecutionContext) = {
    Future{
      AppStoreMapService.appStore.addOne(id, name)
      Json.obj(id.toString -> AppStoreMapService.appStore.get(id).get)
    }
  }

  def deleteApp(id: Int)(implicit ex: ExecutionContext): Future[JsArray] = {
    Future{
      AppStoreMapService.appStore.remove(id)
      convertToJsonArray(AppStoreMapService.appStore)
    }
  }

  def convertToJsonArray(apps: Map[Int, String]) = {
    apps.toList.map(app => Json.obj(app._1.toString -> app._2)).foldRight(Json.arr())((obj, arr) => arr :+ obj)
  }
}
