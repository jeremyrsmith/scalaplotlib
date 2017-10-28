package scalaplot

import scala.util.Random.nextGaussian

object LineChartTest extends App {

  // some fake data
  private val data = Stream.iterate((0, 0.0, 0.0)) {
      case (x, y1, y2) => (x + 1, y1 + nextGaussian(), y2 + nextGaussian())
  }.take(100).toSeq

  Chart("Line", Line)
    .data(data)
    .domain("X")(_._1)
    .rangeAxis("Y")
    .range("Y1")(_._2)
    .range("Y2")(_._3)
    .show()
}
