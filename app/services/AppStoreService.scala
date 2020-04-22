package services

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

trait AppStoreService {
  def ifAppExists(id: Int): Boolean
  def getAppsAsync()(implicit ex: ExecutionContext): Future[JsArray]
  def getAppAsync(id: Int)(implicit ex: ExecutionContext): Future[JsObject]
  def addOrUpdateApp(id: Int, name: String)(implicit ex: ExecutionContext): Future[JsObject]
  def deleteApp(id: Int)(implicit ex: ExecutionContext): Future[JsArray]
}
