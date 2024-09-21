import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.Client
import org.http4s.ember.client.*

object Main extends IOApp.Simple {

  def show(cowboys: List[Cowboy]): IO[Unit] =
    IO.println(s"Cowboys: ${cowboys.length}") *>
      cowboys.traverse_ { it => IO.println(s"\t- ${it.name}, ${it.id}, ${it.email}") }

  def dependencies: Resource[IO, (Client[IO], AppConf)] = for {
    client <- EmberClientBuilder.default[IO].build
    conf   <- loadConfig.toResource
  } yield (client, conf)

  override def run: IO[Unit] =
    dependencies
      .use { (client, conf) =>
        val tontosClient = TontosClient(client, "token")
        val emailClient  = EmailClient()
        tontosClient.fetchCowboys
          .flatTap(show)
          .flatMap { cowboys =>
            val tonto = random(cowboys)
            IO.both(
              tontosClient.postTonto(tonto.id),
              emailClient.sendEmail(cowboys, tonto)
            )
          }
      }
      .handleErrorWith(e => IO.println(s"Error: $e"))
      .void

}
