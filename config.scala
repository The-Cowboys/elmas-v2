import cats.effect.*
import pureconfig.*
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.derivation.default.*
import pureconfig.module.catseffect.syntax.*

case class AppConf(
    token: Option[String]
) derives ConfigReader

def loadConfig: IO[AppConf] =
    ConfigSource.file("./app.conf").loadF[IO, AppConf]()
