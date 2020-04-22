name := """lightbend-play-concepts"""
organization := "icx.training"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.12"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "4.0.0" pomOnly()
libraryDependencies += "com.datastax.oss" % "java-driver-query-builder" % "4.5.1"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "icx.training.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "icx.training.binders._"
