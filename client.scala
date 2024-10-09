import cats.effect.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.literal.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.client.Client
import org.http4s.dsl.io.*
import org.http4s.implicits.*

case class Cowboy(
    email: String,
    name: String,
    id: Int,
    tonto: Int
)

case class TontoRequest(
    id: Int
)

final class TontosClient(client: Client[IO], token: String) {

    val url = uri"https://thecowboys.one/api"

    def fetchCowboys: IO[List[Cowboy]] =
        client.expect[List[Cowboy]](url / "cowboys")

    def postTonto(id: Int): IO[Unit] =
        IO.println(s"Posting tonto for cowboy with id: $id") *>
            client
                .expect[Json](
                    Request[IO](Method.POST, url / "tontos")
                        .withEntity(TontoRequest(id))
                        .withHeaders(
                            Header("Content-Type", "application/json"),
                            Header("Authorization", s"Bearer $token")
                        )
                )
                .flatMap(IO.println) *>
            IO.println(s"Posted tonto for cowboy with id: $id")

}
