import cats.effect.*

final class EmailClient {

  def sendEmail(receivers: List[Cowboy], tonto: Cowboy): IO[Unit] =
    IO.println(s"Sending email")

}
