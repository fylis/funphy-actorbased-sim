import scala.util.Random

object ForestGenerator {
  def randomNonFireCell(): Cell = {
    Random.nextInt(4) match {
      case 0 => Tree()
      case 1 => Grass()
      case 2 => Water()
      case 3 => Soil()
    }
  }

  def generate(width: Int, height: Int): Array[Array[Cell]] = {
    val totalCells = width * height
    val maxFires = (totalCells * 0.08).toInt

    val forest = Array.fill(height, width)(randomNonFireCell())

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
