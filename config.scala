import cats.effect.*

case class AppConf(
    apiAuthToken: String,
    emailAuthToken: String
)

def loadConfig: IO[AppConf] =
    for {
        apiToken   <- IO.fromOption(sys.env.get("TOKEN_API"))(RuntimeException("TOKEN_API not found"))
        emailToken <- IO.fromOption(sys.env.get("TOKEN_EMAIL"))(RuntimeException("TOKEN_EMAIL not found"))
    } yield AppConf(apiToken, emailToken)
