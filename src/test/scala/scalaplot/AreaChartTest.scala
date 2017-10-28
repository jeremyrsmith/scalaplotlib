package scalaplot

import scala.util.Random

object AreaChartTest extends App {
  private val data = 0 until 100 map { a =>
    a -> (a + Random.nextGaussian())
  }

  Chart("Area", Area)
    .data(data)
    .domain("X")(_._1)
    .rangeAxis("Y")
    .range("Y1")(_._2)
    .range("Y2")(t => 100.0 - t._2)
    .show()
}
