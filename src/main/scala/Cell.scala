trait Cell {
  def symbol: String
}

case class Tree() extends Cell {
  def symbol: String = "\uD83C\uDF33"
}

case class Grass() extends Cell {
  def symbol: String = "\uD83C\uDF3F"
}

case class Water() extends Cell {
  def symbol: String = "\uD83C\uDF0A"
}

case class Soil() extends Cell {
  def symbol: String = "\uD83D\uDFEB"
}

case class Fire() extends Cell {
  def symbol: String = "\uD83D\uDD25"
}