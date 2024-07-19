package eu.derfniw.mimir.server

import cats.effect.*
import pureconfig.*
import pureconfig.generic.derivation.EnumConfigReader
import pureconfig.generic.derivation.default.*

enum AppMode derives EnumConfigReader:
  case Development, Production

case class ServiceConfig(host: String, port: Int, mode: AppMode)
    derives ConfigReader

object Config:
  def serviceConfig[F[_]: Sync]: Resource[F, ServiceConfig] =
    Resource.eval(
      Sync[F].delay(ConfigSource.default.loadOrThrow[ServiceConfig])
    )
end Config
