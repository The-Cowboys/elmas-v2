import cats.effect.IO
import io.circe.Json
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.*
import org.http4s.client.middleware.Logger
import org.http4s.implicits.*
import org.typelevel.ci.CIString

final class EmailClient(internal: Client[IO], token: String) {

    val url = uri"https://api.resend.com/emails"

    private val client = Logger[IO](
        logHeaders = false,
        logBody = true,
        logAction = Some((msg: String) => IO.println(msg))
    )(internal)

    def sendEmail(cowboys: List[Cowboy], tonto: Cowboy): IO[Unit] =
        val receivers = cowboys.map(_.email)
        val emailPayload = Json.obj(
            "from"    -> "The Cowboys <tonto@thecowboys.one>".asJson,
            "to"      -> receivers.asJson,
            "subject" -> "El más tonto del día es...".asJson,
            "html"    -> s"<p>Felicidades, <strong>${tonto.name}</strong> es el más tonto :D</p>".asJson
        )

        IO.println("Sending emails") *> client.expect[Unit](
            Request[IO](Method.POST, url)
                .withEntity(emailPayload)
                .withHeaders(
                    Header.Raw(CIString("Content-Type"), "application/json"),
                    Header.Raw(CIString("Authorization"), s"Bearer $token")
                )
        ) <* IO.println("Sent email to cowboys")

}
