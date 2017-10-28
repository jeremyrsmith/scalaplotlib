package scalaplot

import org.jfree.chart.JFreeChart

import scala.util.Random

object ScatterPlotTest extends App {

  private def random() = Random.nextGaussian() * 100

  private val data = Seq.fill(100) {
    Seq(
      ("Foo", random() -> random()),
      ("Bar", random() -> random())
    )
  }.flatten

  Chart("Scatter", Scatter)
    .data(data)
    .xySeries("X", "Y")(_._1)(_._2)
    .show()
}
