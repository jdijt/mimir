package eu.derfniw.mimir.server

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{Ipv4Address, Port, ipv4, port}
import eu.derfniw.mimir.server.resources.StaticResourcesService
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

object Main extends IOApp.Simple:

  private val server = for
    config <- Config.serviceConfig[IO]
    _ <- Resource.Pure(IO.println(s"Starting server in ${config.mode} mode"))
    httpApp = Router
      .define()(StaticResourcesService[IO](config).routes)
      .orNotFound
    server <- EmberServerBuilder
      .default[IO]
      .withHost(Ipv4Address.fromString(config.host).getOrElse(ipv4"0.0.0.0"))
      .withPort(Port.fromInt(config.port).getOrElse(port"8080"))
      .withHttpApp(httpApp)
      .build
  yield server

  def run: IO[Unit] = server.useForever
end Main
