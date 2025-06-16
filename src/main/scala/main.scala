object Main extends App {
  val width = 100
  val height = 48
  val steps = 150

  val cellSize = 3.0 // 3m
  val windDirection: Direction = E // Ouest -> Est
  val windSpeed: Double = 0 // 0 m/s

  val treeProb = 0.15
  val grassProb = 0.4

  val treeBurnSteps = 4
  val treeRegrowSteps = 30
  val fireProb = 0.02

  val forest: Array[Array[ForestCell]] = ForestGenerator.generate(width, height, fireProb)

  val simulatedForestConsole: List[Array[Array[ForestCell]]] = Simulation.simulateN(
    forest, steps, treeProb, grassProb,
    windDirection, windSpeed,
    treeBurnSteps, treeRegrowSteps
  )

  Simulation.consoleAnimation(simulatedForestConsole, 100)

  // Pour les graphes
  val windSpeeds = List(0.0, 2.0, 4.0, 6.0, 8.0, 10.0)
  val nRuns = 10

  for (windSpeed <- windSpeeds; run <- 1 to nRuns) {
    val simulatedForest: List[Array[Array[ForestCell]]] = Simulation.simulateN(
      forest, steps, treeProb, grassProb,
      windDirection, windSpeed,
      treeBurnSteps, treeRegrowSteps
    )
    Simulation.exportBurnedSummary(simulatedForest, f"summary_wind${windSpeed.toInt}_run${run}.csv")
  }
}
