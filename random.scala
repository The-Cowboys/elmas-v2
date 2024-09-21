import scala.util.Random

def random(cowboys: List[Cowboy]): Cowboy =
  val list  = Random.shuffle(cowboys)
  val index = Random.nextInt(list.length)
  list(index)
