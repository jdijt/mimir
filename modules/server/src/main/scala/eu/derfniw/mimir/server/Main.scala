package eu.derfniw.mimir.server

import cats.effect.{IO, IOApp}
import eu.derfniw.mimir.server.resources.StaticResourcesService
import fs2.io.net.Network
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.Logger

object Main extends IOApp.Simple:
  
  private def config = Config[IO]()

  private def routes(config: ServiceConfig) =
    Router
      .define( /*todo: api routes */ )(
        StaticResourcesService[IO](config).routes
      )
      .orNotFound

  private def server(config: ServiceConfig, app: HttpApp[IO]) =
    EmberServerBuilder
      .default[IO]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(app)
      .build

  def run: IO[Unit] =
    val serverResource = for
      cfg <- config
      app = routes(cfg.service)
      srv <- server(cfg.service, app)
    yield srv
    serverResource.useForever
end Main
