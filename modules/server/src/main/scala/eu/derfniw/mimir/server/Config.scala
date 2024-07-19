package eu.derfniw.mimir.server

import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.{Ipv4Address, Port, ipv4}
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.syntax.*
import pureconfig.*
import pureconfig.generic.derivation.EnumConfigReader
import pureconfig.generic.derivation.default.*

import scala.util.Try

enum AppMode derives EnumConfigReader:
  case Dev, Prod

given ConfigReader[Ipv4Address] =
  ConfigReader.fromStringOpt(in => Ipv4Address.fromString(in))
given ConfigReader[Port] =
    ConfigReader.fromStringOpt(in => Port.fromString(in))

case class ServiceConfig(host: Ipv4Address, port: Port, mode: AppMode)
    derives ConfigReader

case class Config(service: ServiceConfig) derives ConfigReader

object Config:
  def apply[F[_]: Sync](): Resource[F, Config] = Resource.eval {
      for {
        cfg <- Sync[F].delay(ConfigSource.default.loadOrThrow[Config])
        _ <- Logger[F].info(Loaded config: $cfg)
      } yield cfg
    }
end Config
