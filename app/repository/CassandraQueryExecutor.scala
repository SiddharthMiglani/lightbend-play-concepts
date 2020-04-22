package repository

import com.datastax.oss.driver.api.core.cql.PreparedStatement
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession, DefaultConsistencyLevel}
import com.datastax.oss.driver.api.core.cql._
import models.Application
import play.api.libs.json.{JsObject, Json}

object CassandraQueryExecutor {
  val session = CqlSession.builder().withKeyspace(CqlIdentifier.fromCql("AppStore")).build()

  def ifAppExists(id: Int) = {
    val preparedStatement = session.prepare("select * from applications where partitionKey = 1 and id = ?")
    val row = session.execute(preparedStatement.bind(id)).one()
    row != null
  }

  def getAllApps = {
    val resultSet = session.execute("select * from applications where partitionKey = 1")
    val iterator = resultSet.iterator()
    var jsonArray = Json.arr()
    while(iterator.hasNext){
      val currentRow = iterator.next()
      jsonArray = jsonArray :+ Json.toJson(Application(currentRow.getInt("id"), currentRow.getString("name")))
    }
    // session.close()
    jsonArray
  }

  def getAppById(id: Int) = {
    val preparedStatement = session.prepare("select * from applications where partitionKey = 1 and id = ?")
    val row = session.execute(preparedStatement.bind(id)).one()
    Json.toJson(Application(row.getInt("id"), row.getString("name"))).as[JsObject]
  }

  def updateApp(id: Int, name: String) ={
    val simpleStatement = SimpleStatement.builder("update appstore.applications set name = ? where partitionKey = 1 and id = ?")
      .setConsistencyLevel(DefaultConsistencyLevel.ONE)
      .build()
    val updateStatement: PreparedStatement = session.prepare(simpleStatement)
    val boundStatement = updateStatement.bind(name, id)
    session.execute(boundStatement)
    getAppById(id)
  }

  def deleteApp(id: Int) = {
    val preparedStatement = session.prepare("delete name from appstore.applications where partitionKey = 1 and id = ?")
    session.execute(preparedStatement.bind(id))
    getAllApps
  }
}
