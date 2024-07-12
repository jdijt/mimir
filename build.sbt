ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "eu.derfniw.mimir"


lazy val parent = project.in(file("."))
  .aggregate(shared.js, shared.jvm, client, server)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared"))

lazy val client = project.in(file("modules/client"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared.js)

lazy val server = project.in(file("modules/server"))
  .dependsOn(shared.jvm)