package services
import play.api.libs.json.{JsArray, JsObject}
import repository.CassandraQueryExecutor

import scala.concurrent.{ExecutionContext, Future}

object AppStoreDBService extends AppStoreService {

  override def ifAppExists(id: Int): Boolean = {
    CassandraQueryExecutor.ifAppExists(id)
  }

  override def getAppsAsync()(implicit ex: ExecutionContext): Future[JsArray] = {
    Future {
      CassandraQueryExecutor.getAllApps
    }
  }

  override def getAppAsync(id: Int)(implicit ex: ExecutionContext): Future[JsObject] = {
    Future {
      CassandraQueryExecutor.getAppById(id)
    }
  }

  override def addOrUpdateApp(id: Int, name: String)(implicit ex: ExecutionContext): Future[JsObject] = {
    Future {
      CassandraQueryExecutor.updateApp(id, name)
    }
  }

  override def deleteApp(id: Int)(implicit ex: ExecutionContext): Future[JsArray] = {
    Future {
      CassandraQueryExecutor.deleteApp(id)
    }
  }
}
