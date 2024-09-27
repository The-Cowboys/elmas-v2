import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.Client
import org.http4s.ember.client.*

object Main extends IOApp {

    def dependencies: Resource[IO, Tonto] = for {
        client      <- EmberClientBuilder.default[IO].build
        conf        <- loadConfig.toResource
        tontosClient = TontosClient(client, conf.apiAuthToken)
        emailClient  = EmailClient(client, conf.emailAuthToken)
    } yield Tonto(tontosClient, emailClient)

    override def run(args: List[String]): IO[ExitCode] =
        run.as(ExitCode.Success)

    def run: IO[Unit] =
        dependencies
            .use(_.run)
            .handleErrorWith(e => IO.println(s"Error: $e"))
            .void

}
