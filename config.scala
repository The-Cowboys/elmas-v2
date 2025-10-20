import AppConf.EmailSender
import cats.effect.*
import org.http4s.*

case class AppConf(
    apiAuthToken: String,
    emailAuthToken: String,
    senderEmail: EmailSender,
    apiUrl: Uri
)

object AppConf {
    opaque type EmailSender = String

    object EmailSender {
        def apply(value: String): EmailSender = value
    }

    extension (emailSender: EmailSender) {
        def value: String = emailSender
    }
}

def loadConfig: IO[AppConf] =
    for {
        apiToken   <- IO.fromOption(sys.env.get("TOKEN_API"))(RuntimeException("TOKEN_API not found"))
        emailToken <- IO.fromOption(sys.env.get("TOKEN_EMAIL"))(RuntimeException("TOKEN_EMAIL not found"))
        apiUri     <- IO.fromOption(sys.env.get("API_URL").map(Uri.unsafeFromString))(RuntimeException("API_URL not found"))
        emailSender <- IO.fromOption(sys.env.get("EMAIL_SENDER").map(EmailSender.apply))(RuntimeException("EMAIL_SENDER not found"))
    } yield AppConf(apiToken, emailToken, emailSender, apiUri)
