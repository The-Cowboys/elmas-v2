import cats.effect.*
import org.http4s.*

case class AppConf(
    apiAuthToken: String,
    emailAuthToken: String,
    apiUrl: Uri
)

def loadConfig: IO[AppConf] =
    for {
        apiToken   <- IO.fromOption(sys.env.get("TOKEN_API"))(RuntimeException("TOKEN_API not found"))
        emailToken <- IO.fromOption(sys.env.get("TOKEN_EMAIL"))(RuntimeException("TOKEN_EMAIL not found"))
        apiUri     <- IO.fromOption(sys.env.get("API_URL").map(Uri.unsafeFromString))(RuntimeException("API_URL not found"))
    } yield AppConf(apiToken, emailToken, apiUri)
