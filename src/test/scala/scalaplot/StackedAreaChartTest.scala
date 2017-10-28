package scalaplot

import scala.util.Random

object StackedAreaChartTest extends App {
  private val data: IndexedSeq[(Double, Double)] = 0 until 100 map { a =>
    a.toDouble -> (a + Random.nextGaussian())
  }

  Chart("Stacked Area", Area)
    .data(data)
    .domain("X")(_._1)
    .rangeAxis("Y")
    .range("Y1")(_._2)
    .range("Y2")(t => 100.0 - t._2)
    .show()
}
