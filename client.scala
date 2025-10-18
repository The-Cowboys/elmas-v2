import cats.effect.*
import sttp.client4.Response
import sttp.client4.quick.*

case class Cowboy(
    email: String,
    name: String,
    id: Int,
    tonto: Int
)

case class TontoRequest(
    id: Int
)

final class TontosClient(token: String) {

    val base = "http://34.135.73.247/api"

    def fetchCowboys: IO[List[Cowboy]] = IO.blocking {
        val lol = quickRequest.get(uri"$base/cowboys").send()
        println(lol.body)
        List.empty
    }.onError(e => IO.println(s"ERROR: GET $base") *> IO(e.printStackTrace()))

    def postTonto(id: Int): IO[Unit] =
        IO.println(s"Posting tonto for cowboy with id: $id") *>
            IO.blocking {
                "Nothing!!"
            }
            /*client
                .expect[Json](
                    Request[IO](Method.POST, uri"$base/cowboys")
                        .withEntity(TontoRequest(id))
                        .withHeaders(
                            Header("Content-Type", "application/json"),
                            Header("Authorization", s"Bearer $token")
                        )
                )*/
                .flatMap(IO.println) *>
            IO.println(s"Posted tonto for cowboy with id: $id")

}
