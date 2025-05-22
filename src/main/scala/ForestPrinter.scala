object ForestPrinter {

  def print(forest: Array[Array[Cell]]): String = {
    val height = forest.length
    val width = if (height > 0) forest(0).length else 0

    val lines = for (y <- 0 until height) yield {
      val row = for (x <- 0 until width) yield f"${forest(y)(x).symbol}"
      row.mkString(" ")
    }

    lines.mkString("\n")
  }
}
