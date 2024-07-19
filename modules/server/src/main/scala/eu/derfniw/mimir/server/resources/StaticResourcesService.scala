package eu.derfniw.mimir.server.resources

import cats.effect.Async
import cats.syntax.all.*
import eu.derfniw.mimir.server.{AppMode, ServiceConfig}
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.staticcontent.resourceServiceBuilder

class StaticResourcesService[F[_]: Async](config: ServiceConfig) extends Http4sDsl[F]:

  private val baseRoutes = resourceServiceBuilder[F]("/public")
    .withPreferGzipped(true)
    .toRoutes

  private val rewrites = HttpRoutes.of[F] {
    case req @ GET -> Root => baseRoutes.apply(req.withUri(req.uri / "index.html")).getOrElseF(NotFound())
    case req @ GET -> "client" /: path =>
      if config.mode == AppMode.Development then
        baseRoutes.apply(req.withUri(req.uri / "client-fastopt" / path.renderString)).getOrElseF(NotFound())
      else
        baseRoutes.apply(req.withUri(req.uri / "client-fullopt" / path.renderString)).getOrElseF(NotFound())
  }
  val routes = rewrites <+> baseRoutes

end StaticResourcesService
