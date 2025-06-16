import java.io.{BufferedWriter, FileWriter}

object Simulation {

  def simulateN(initialForest: Array[Array[ForestCell]],
                n: Int,
                treeProb: Double,
                grassProb: Double,
                windDirection: Direction = N,
                windSpeed: Double = 0.0,
                burnSteps: Int,
                regrowSteps: Int
               ): List[Array[Array[ForestCell]]] = {
    (1 to n).foldLeft(List(initialForest)) { (history, _) =>
      val sim = new ForestSimulator(
        history.last, treeProb, grassProb,
        windDirection, windSpeed, burnSteps, regrowSteps
      )
      sim.step()
      history :+ sim.forestHistory.last
    }
  }


  def consoleAnimation(simulatedForest: List[Array[Array[ForestCell]]], sleepTime: Int): Unit = {
    simulatedForest.zipWithIndex.foreach { case (forest, idx) =>
      Thread.sleep(sleepTime)
      print("\u001b[2J\u001b[H")
      println(s"\nÉtape $idx :")
      println(ForestPrinter.printLogo(forest))
    }
  }

  def exportCSV(simulation: List[Array[Array[ForestCell]]], fileName: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(fileName))
    for ((forest, step) <- simulation.zipWithIndex) {
      writer.write(s"# Step $step\n")
      for (row <- forest) {
        val line = row.map(_.textSymbol).mkString(",")
        writer.write(line)
        writer.newLine()
      }
      writer.newLine()
    }
    writer.close()
    println(s"Export CSV terminé : $fileName")
  }

  def exportStructuredCSV(simulation: List[Array[Array[ForestCell]]], fileName: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(fileName))
    writer.write("step,x,y,state\n")

    for ((forest, step) <- simulation.zipWithIndex) {
      for (y <- forest.indices; x <- forest(y).indices) {
        val cell = forest(y)(x)
        writer.write(s"$step,$x,$y,${cell.textSymbol}\n")
      }
    }

    writer.close()
    println(s"Export structuré terminé : $fileName")
  }

  def exportBurnedSummary(simulation: List[Array[Array[ForestCell]]], fileName: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(fileName))
    val totalCells = simulation.head.length * simulation.head(0).length
    // Pour chaque cellule, garde en mémoire si elle a brûlé au moins une fois
    val burned = Array.fill(simulation.head.length, simulation.head(0).length)(false)
    var burnedCumul = 0

    writer.write("step,burned,percent_burned\n")
    for ((forest, step) <- simulation.zipWithIndex) {
      var newlyBurned = 0
      for (y <- forest.indices; x <- forest(y).indices) {
        val cell = forest(y)(x)
        if (!burned(y)(x) && cell.textSymbol == "F") {
          burned(y)(x) = true
          newlyBurned += 1
        }
      }
      burnedCumul += newlyBurned
      val percent = burnedCumul.toDouble / totalCells * 100
      writer.write(s"$step,$burnedCumul,$percent\n")
    }
    writer.close()
    println(s"Export synthétique terminé : $fileName")
  }
}
