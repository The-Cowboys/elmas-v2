import cats.effect.{IO, Resource}
import org.http4s.client._
import org.http4s.ember.client._
import org.http4s._
import org.http4s.implicits._
import org.http4s.circe._
import io.circe.Json
import io.circe.syntax._
import org.http4s.client.middleware.Logger

final class EmailClient(internal: Client[IO], token: String) {

  val url = uri"https://api.resend.com/emails"

  val client = Logger[IO](
    logHeaders = false,
    logBody = true,
    logAction = Some((msg: String) => IO.println(msg))
  )(internal)

  def sendEmail(cowboys: List[Cowboy], tonto: Cowboy): IO[Unit] =
    val receivers = cowboys.map(_.email)
    val emailPayload = Json.obj(
      "from"    -> "The Cowboys <tonto@thecowboys.one>".asJson,
      "to"      -> receivers.asJson,
      "subject" -> "Hello World".asJson,
      "html"    -> "<p>Congrats on sending your <strong>first email</strong>!</p>".asJson
    )

    IO.println("Sending emails") *> client.expect[Unit](
      Request[IO](Method.POST, url)
        .withEntity(emailPayload)
        .withHeaders(
          Header("Content-Type", "application/json"),
          Header("Authorization", s"Bearer $token")
        )
    ) <* IO.println("Sent email to cowboys")

}
