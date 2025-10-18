import PrettyDuration.PrettyPrintableDuration
import cats.effect.*

import java.time.{LocalDateTime, LocalTime, ZoneId}
import scala.annotation.tailrec
import scala.concurrent.duration.*

def waitTimeUntil(scheduledTime: LocalTime): IO[FiniteDuration] = IO.realTimeInstant.flatMap { instant =>
    val zone = ZoneId.systemDefault()

    val now         = LocalDateTime.ofInstant(instant, zone)
    val adjustedNow = LocalDateTime.of(now.toLocalDate, scheduledTime)

    val next       = if now.isAfter(adjustedNow) then adjustedNow.plusDays(1) else adjustedNow
    val nextMillis = next.atZone(zone).toInstant.toEpochMilli

    val wait = (nextMillis - instant.toEpochMilli).millis

    IO.println(s"now: $now") *>
        IO.println(s"next: $next") *>
        IO.println(s"wait: ${wait.pretty}") *>
        IO.println("")
            .as(wait)
}

object PrettyDuration {

    private val timeUnitList: List[TimeUnit] =
        DAYS :: HOURS :: MINUTES :: SECONDS :: MILLISECONDS :: MICROSECONDS :: NANOSECONDS :: Nil

    implicit class PrettyPrintableDuration(val duration: Duration) extends AnyVal {

        @tailrec
        private def prettyRec(
            acc: List[FiniteDuration],
            remUnits: List[TimeUnit],
            rem: FiniteDuration,
            isPast: Boolean
        ): String = {
            remUnits match {
                case h :: t =>
                    if rem > Duration(1, h) then {
                        val x = Duration(rem.toUnit(h).toLong, h)
                        prettyRec(x :: acc, t, rem - x, isPast)
                    } else {
                        prettyRec(acc, t, rem, isPast)
                    }
                case Nil =>
                    acc.reverse.map(_.toString).mkString(" ") + (if isPast then " ago" else "")
            }
        }

        def pretty: String = {
            duration match {
                case Duration.Zero                          => "now"
                case f: FiniteDuration if f < Duration.Zero => prettyRec(Nil, timeUnitList, f * -1, isPast = true)
                case f: FiniteDuration                      => prettyRec(Nil, timeUnitList, f, isPast = false)
                case Duration.Inf                           => "infinite"
                case Duration.MinusInf                      => "minus infinite"
                case _                                      => "undefined"
            }
        }

    }

}
