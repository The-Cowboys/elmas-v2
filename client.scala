import cats.effect.*
import io.circe.*
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.client.Client
import org.http4s.implicits.*
import org.typelevel.ci.CIString

case class Cowboy(
    email: String,
    name: String,
    id: Int,
    tonto: Int
)

case class TontoRequest(
    id: Int
)

final class TontosClient(client: Client[IO], apiUrl: Uri, token: String) {

    def fetchCowboys: IO[List[Cowboy]] =
        IO.println(s"Fetching cowboys to: $apiUrl") *> client.expect[List[Cowboy]](apiUrl / "cowboys")

    def postTonto(id: Int): IO[Unit] =
        IO.println(s"Posting tonto for cowboy ($id) to $apiUrl") *>
            client
                .expect[Json](
                    Request[IO](Method.POST, apiUrl / "tontos")
                        .withEntity(TontoRequest(id))
                        .withHeaders(
                            Header.Raw(CIString("Content-Type"), "application/json"),
                            Header.Raw(CIString("Authorization"), s"Bearer $token")
                        )
                )
                .flatMap(IO.println) *>
            IO.println(s"Posted tonto for cowboy with id: $id")

}
