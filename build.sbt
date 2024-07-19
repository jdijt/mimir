ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "eu.derfniw.mimir"

val dependencies = new {

  val versions = new {
    val cats       = "2.12.0"
    val catsEffect = "3.5.4"
    val circe      = "0.14.9"
    val http4s     = "0.23.27"
    val http4sDom  = "0.2.11"
    val laminar    = "16.0.0"
    val scribe     = "3.14.0"
    val pureConfig = "0.17.7"
  }

  val commonDeps = Def.settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core"   % versions.cats,
      "org.typelevel" %%% "cats-effect" % versions.catsEffect
    )
  )

  val shared = Def.settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core"    % versions.circe,
      "io.circe" %%% "circe-generic" % versions.circe
    )
  )

  val client = Def.settings(
    libraryDependencies ++= Seq(
      "com.raquo"  %%% "laminar"       % versions.laminar,
      "org.http4s" %%% "http4s-dom"    % versions.http4sDom,
      "org.http4s" %%% "http4s-client" % versions.http4s,
      "org.http4s" %%% "http4s-circe"  % versions.http4s
    )
  )

  val server = Def.settings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig-core"     % versions.pureConfig,
      "com.outr"              %% "scribe-cats"         % versions.scribe,
      "com.outr"              %% "scribe-slf4j"        % versions.scribe,
      "org.http4s"            %% "http4s-ember-server" % versions.http4s,
      "org.http4s"            %% "http4s-dsl"          % versions.http4s,
      "org.http4s"            %% "http4s-circe"        % versions.http4s,
      "org.typelevel"         %% "cats-effect"         % versions.catsEffect,
      "org.typelevel"         %% "cats-core"           % versions.cats
    )
  )
}

lazy val mimir = project
  .in(file("."))
  .aggregate(shared.js, shared.jvm, client, server)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared"))
  .settings(dependencies.commonDeps, dependencies.shared)

lazy val client = project
  .in(file("modules/client"))
  .dependsOn(shared.js)
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(dependencies.commonDeps, dependencies.client)
  .settings(
    scalaJSUseMainModuleInitializer := true
  )

lazy val server = project
  .in(file("modules/server"))
  .dependsOn(shared.jvm)
  .enablePlugins(JavaAppPackaging, DockerPlugin, SbtWeb)
  .settings(dependencies.commonDeps, dependencies.server)
  .settings(
    scalaJSProjects                := Seq(client),
    Assets / pipelineStages        := Seq(scalaJSPipeline),
    Assets / WebKeys.packagePrefix := "public/",
    Runtime / managedClasspath += (Assets / packageBin).value,
    Compile / compile := (Compile / compile).dependsOn(scalaJSPipeline).value,
  )

addCommandAlias("runDev", ";server/reStart --- -Dservice.mode=dev")
addCommandAlias("stopDev", ";server/reStop")
