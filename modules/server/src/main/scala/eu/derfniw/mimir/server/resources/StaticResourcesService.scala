package eu.derfniw.mimir.server.resources

import cats.effect.Async
import cats.syntax.all.*
import eu.derfniw.mimir.server.AppMode.{Dev, Prod}
import eu.derfniw.mimir.server.{AppMode, ServiceConfig}
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.staticcontent.resourceServiceBuilder

class StaticResourcesService[F[_]: Async](config: ServiceConfig)
    extends Http4sDsl[F]:

  private val log = scribe.cats[F]

  private val baseRoutes = resourceServiceBuilder[F]("/public")
    .withPreferGzipped(true)
    .toRoutes

  private val clientRewrites =
    def clientRouteRewrite(internalPrefix: String) = HttpRoutes.of[F] {
      case req @ GET -> "client" /: path =>
        val newPath = Path.Root / internalPrefix / path.renderString
        baseRoutes(req.withUri(req.uri.withPath(newPath)))
          .getOrElseF(NotFound())
    }

    config.mode match
      case Dev  => clientRouteRewrite("client-fastopt")
      case Prod => clientRouteRewrite("client-fullopt")
  end clientRewrites

  private val indexRewrite = HttpRoutes.of[F] { case req @ GET -> Root =>
    baseRoutes(req.withUri(req.uri / "index.html")).getOrElseF(NotFound())
  }

  val routes: HttpRoutes[F] = clientRewrites <+> indexRewrite <+> baseRoutes

end StaticResourcesService
