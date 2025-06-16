import scala.util.Random

sealed trait Direction {
  def dx: Int;

  def dy: Int
}

case object N extends Direction {
  val dx = 0;
  val dy = -1
}
case object NE extends Direction {
  val dx = 1;
  val dy = -1
}
case object E extends Direction {
  val dx = 1;
  val dy = 0
}
case object SE extends Direction {
  val dx = 1;
  val dy = 1
}
case object S extends Direction {
  val dx = 0;
  val dy = 1
}
case object SW extends Direction {
  val dx = -1;
  val dy = 1
}
case object W extends Direction {
  val dx = -1;
  val dy = 0
}
case object NW extends Direction {
  val dx = -1;
  val dy = -1
}

object Directions {
  val all = List(N, NE, E, SE, S, SW, W, NW)
}

class ForestSimulator(initialForest: Array[Array[ForestCell]], treeProb: Double, grassProb: Double,
                      windDirection: Direction, windSpeed: Double, burnSteps: Int, regrowSteps: Int) {
  var forestHistory: List[Array[Array[ForestCell]]] = List(initialForest.map(_.clone))

  private def windSectorDirections(dir: Direction): List[Direction] = dir match {
    case N => List(SW, S, SE)
    case NE => List(S, SW, W)
    case E => List(SW, W, NW)
    case SE => List(W, NW, N)
    case S => List(NW, N, NE)
    case SW => List(N, NE, E)
    case W => List(NE, E, SE)
    case NW => List(E, SE, S)
  }

  private def hasFireInDirections(x: Int, y: Int, grid: Array[Array[ForestCell]], dirs: List[Direction]): Boolean = {
    val h = grid.length; val w = grid(0).length
    dirs.exists { dir =>
      val nx = x + dir.dx; val ny = y + dir.dy
      nx >= 0 && nx < w && ny >= 0 && ny < h && grid(ny)(nx).isInstanceOf[Fire]
    }
  }

  private def windEffectFactor(speed: Double): Double = 1.0 + 0.08 * speed

  def step(): Unit = {
    val prev = forestHistory.last.map(_.clone)
    val height = prev.length
    val width = prev(0).length

    val newForest = Array.tabulate(height, width) { (y, x) =>
      prev(y)(x) match {

        case Fire(age, fromGrass) =>
          if (age + 1 >= burnSteps) Soil(0, fromGrass)
          else Fire(age + 1, fromGrass)

        case Soil(age, fromGrass) =>
          if (age + 1 >= regrowSteps * 2) Tree()
          else if (age + 1 >= regrowSteps) Grass()
          else Soil(age + 1, fromGrass)

        case t: Tree =>
          val fireNearby = if (windSpeed == 0) hasFireInDirections(x, y, prev, Directions.all)
          else hasFireInDirections(x, y, prev, windSectorDirections(windDirection))
          if (fireNearby && Math.random() < treeProb * windEffectFactor(windSpeed)) Fire(0, fromGrass = false)
          else t

        case g: Grass =>
          val fireNearby = if (windSpeed == 0) hasFireInDirections(x, y, prev, Directions.all)
          else hasFireInDirections(x, y, prev, windSectorDirections(windDirection))
          if (fireNearby && Math.random() < grassProb * windEffectFactor(windSpeed)) Fire(0, fromGrass = true)
          else g

        case other => other
      }
    }

    forestHistory = forestHistory :+ newForest
  }
}