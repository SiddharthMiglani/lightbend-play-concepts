package services

import javafx.beans.value.WritableStringValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import play.api.libs.json._

object CovidStatsService {

  def getStatsAsTuple(country: String): (String, Int, Int, Int, String) = {
    val jsonValue = getStatsFromAPI(country)
    val jsonArray = (jsonValue \ "data" \"covid19Stats").as[JsArray]
    (
      (jsonValue \ "data" \"covid19Stats" \0 \"country").as[String],
      jsonArray.value.map(result => (result \"confirmed").as[Int]).sum,
      jsonArray.value.map(result => (result \"recovered").as[Int]).sum,
      jsonArray.value.map(result => (result \"deaths").as[Int]).sum,
      (jsonValue \ "data" \"covid19Stats" \0 \"lastUpdate").as[String]
    )
  }

  private def getStatsFromAPI(country: String) = {

    // Set the URL
    val url = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats?country="+country
    val httpGet = new HttpGet(url)

    // set the desired header values
    httpGet.setHeader("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com")
    httpGet.setHeader("x-rapidapi-key", "56a3327ce3mshe322b16064b4e93p101047jsn5ff5fb570214")

    // execute the request
    val client = new DefaultHttpClient
    val httpResponse = client.execute(httpGet)

    // process the response
    val resHTTPEntity = httpResponse.getEntity
    var resString = ""
    if (resHTTPEntity != null) {
      val resStream = resHTTPEntity.getContent
      resString = scala.io.Source.fromInputStream(resStream).getLines.mkString
      resStream.close
    }

    // shut down connection
    client.getConnectionManager.shutdown

    // return parsed json
    Json.parse(resString)
  }
}
