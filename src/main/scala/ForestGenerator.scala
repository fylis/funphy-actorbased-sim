import scala.util.Random

object ForestGenerator {
  private def randomNonFireForestCell(): ForestCell = {
    Random.nextInt(7) match {
      case 0 | 1 | 2 => Tree()
      case 3 | 4 => Grass()
      case 5 => Water()
      case 6 => Soil()
    }
  }

  def generate(width: Int, height: Int, fireProb: Double): Array[Array[ForestCell]] = {
    val totalForestCells = width * height
    val maxFires = (totalForestCells * fireProb).toInt

    val forest = Array.fill(height, width)(randomNonFireForestCell())

    val allPositions = for {
      y <- 0 until height
      x <- 0 until width
    } yield (x, y)

    val firePositions = Random.shuffle(allPositions).take(maxFires)

    for ((x, y) <- firePositions) {
      forest(y)(x) = Fire()
    }

    forest
  }

}
