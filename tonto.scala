import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoField
import scala.annotation.tailrec
import scala.concurrent.duration.*
import scala.concurrent.duration.TimeUnit
import scala.util.Random

import PrettyDuration.PrettyPrintableDuration
import cats.effect.*
import cats.syntax.all.*

class Tonto(client: TontosClient, email: EmailClient) {

    private def show(cowboys: List[Cowboy]): IO[Unit] =
        IO.println(s"Cowboys: ${cowboys.length}") *>
            cowboys.traverse_ { it => IO.println(s"\t- ${it.name}, ${it.id}, ${it.email}") }

    private def random(cowboys: List[Cowboy]): Cowboy =
        val list  = Random.shuffle(cowboys)
        val index = Random.nextInt(list.length)
        list(index)

    private def exec: IO[Unit] =
        client.fetchCowboys
            .flatTap(show)
            .flatMap { cowboys =>
                val tonto = random(cowboys)
                IO.println(s"Tonto: $tonto") *>
                    client.postTonto(tonto.id) *>
                    email.sendEmail(cowboys, tonto)
            }

    private def loop(work: IO[Unit]): IO[Nothing] =
        waitTimeUntil(LocalTime.of(6, 0))
            .flatMap(IO.sleep)
            .flatMap(_ => exec)
            .foreverM

    def run(runNow: Boolean): IO[Unit] = {
        if runNow then exec
        else loop { exec }
    }

}
