import cats.effect.*
import cats.syntax.all.*

val version = "1.0.1"

object Main extends IOApp {

    def dependencies: Resource[IO, Tonto] = for {
        conf        <- loadConfig.toResource
        tontosClient = TontosClient(conf.apiAuthToken)
        emailClient  = EmailClient(conf.emailAuthToken)
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
