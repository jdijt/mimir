package eu.derfniw.mimir.server.resources

import cats.effect.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ApiRoutesService[F[_]: Async] extends Http4sDsl[F]{
    def routes = HttpRoutes.of[F] {
        case GET -> Root => NotImplemented()
    }
}
