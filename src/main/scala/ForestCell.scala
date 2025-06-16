trait ForestCell {
  def symbol: String
  def textSymbol: String
}

case class Tree() extends ForestCell {
  def symbol: String = "\uD83C\uDF33"
  def textSymbol: String = "T"
}

case class Grass() extends ForestCell {
  def symbol: String = "\uD83C\uDF3F"
  def textSymbol: String = "G"
}

case class Water() extends ForestCell {
  def symbol: String = "\uD83C\uDF0A"
  def textSymbol: String = "W"
}

case class Soil(age: Int = 0, fromGrass: Boolean = false) extends ForestCell {
  def symbol: String = "\uD83D\uDFEB"
  def textSymbol: String = "S"
}

case class Fire(age: Int = 0, fromGrass: Boolean = false) extends ForestCell {
  def symbol: String = "\uD83D\uDD25"
  def textSymbol: String = "F"
}

case class Lightning() extends ForestCell {
  def symbol: String = "⚡"
  def textSymbol: String = "L"
}