package scalaplot

object BarChartTest extends App {
  Chart("Bar", Bar)
    .dataPoints(
      ("Foo", 10.0, 12.0, 13.0, 14.0),
      ("Bar", 20.0, 12.0, 13.0, 14.0))
    .domain("Wizzle", Categories)(_._1)
    .rangeAxis("Amount")
    .range("A")(_._2)
    .range("B")(_._3)
    .range("C")(_._4)
    .range("D")(_._5)
    .show()
}
