import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.Client
import org.http4s.ember.client.*

val version = "1.0.0"

object Main extends IOApp {

    def dependencies: Resource[IO, Tonto] = for {
        client      <- EmberClientBuilder.default[IO].build
        conf        <- loadConfig.toResource
        tontosClient = TontosClient(client, conf.apiAuthToken)
        emailClient  = EmailClient(client, conf.emailAuthToken)
    } yield Tonto(tontosClient, emailClient)

    override def run(args: List[String]): IO[ExitCode] = {
        val runNow = args.contains("now")
        IO.println(s"Version: $version") *>
            dependencies
                .use(tonto => tonto.run(runNow))
                .handleErrorWith(e => IO.println(s"Error: $e"))
                .void
                .as(ExitCode.Success)
    }

}
