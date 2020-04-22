package controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.mvc._
import play.api.Configuration

import scala.concurrent.ExecutionContext
import services.{AppStoreDBService, AppStoreMapService, AppStoreService}

class AppStoreController @Inject()(config: Configuration, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  // choose which app store to use
  val serviceType = config.get[String]("appStoreService")
  val appStoreService = if(serviceType.equals("DB")){ AppStoreDBService } else { AppStoreMapService }

  def apps = Action.async {
    appStoreService.getAppsAsync().map(result => Ok(result))
  }

  def app(id: Int) = Action.async {
    appStoreService.getAppAsync(id).map(result => Ok(result))
  }

  def publish(id: Int, name: String) = Action.async {
    if (appStoreService.ifAppExists(id)) {
      Future {
        NotAcceptable("App by id " + id + " already exists. If you are looking to update an application. Try: " + controllers.routes.AppStoreController.update(id, name).url)
      }
    } else {
      appStoreService.addOrUpdateApp(id, name).map(result => Created(result))
    }
  }

  def update(id: Int, name: String) = Action.async {
    if (!appStoreService.ifAppExists(id)) {
      Future {
        NotAcceptable("App by id " + id + " does not exist. If you are looking to publish a new application. Try: " + controllers.routes.AppStoreController.publish(id, name).url)
      }
    } else {
      appStoreService.addOrUpdateApp(id, name).map(result => Created(result))
    }
  }

  def delete(id: Int) = Action.async{
    if (!appStoreService.ifAppExists(id)) {
      Future {
        NotAcceptable("App by id " + id + " doe not exist.")
      }
    } else {
      appStoreService.deleteApp(id).map(result => Created(result))
    }
  }
}
