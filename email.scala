import cats.effect.{ IO, Resource }
import sttp.client4.Response
import sttp.client4.quick.*

final class EmailClient(token: String) {

    val url = uri"https://api.resend.com/emails"

    def sendEmail(cowboys: List[Cowboy], tonto: Cowboy): IO[Unit] =
        // val receivers = cowboys.map(_.email) // List("franconecat@gmail.com", "menoscharla@gmail.com")
        // val emailPayload = Json.obj(
        //     "from"    -> "The Cowboys <tonto@thecowboys.one>".asJson,
        //     "to"      -> receivers.asJson,
        //     "subject" -> "El más tonto del día es...".asJson,
        //     "html"    -> s"<p>Felicidades, <strong>${tonto.name}</strong> es el más tonto :D</p>".asJson
        // )

        IO.println("Sending emails") *>
            IO.blocking {
                println("Sent email to cowboys")
            }
            // *> client.expect[Unit](
            // Request[IO](Method.POST, url)
            //     .withEntity(emailPayload)
            //     .withHeaders(
            //         Header("Content-Type", "application/json"),
            //         Header("Authorization", s"Bearer $token")
            //     )
            <* IO.println("Sent email to cowboys")

}
