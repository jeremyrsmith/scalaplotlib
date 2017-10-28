package scalaplot

import scalaplot.data.Stats

object BoxAndWhiskerTest extends App {

  Chart("Box and Whisker", BoxAndWhisker)
    .dataPoints(
      "A" -> Stats(10.0, 50.0, 20.8, 20.5, 7.5),
      "B" -> Stats(20.0, 60.0, 30.8, 30.5, 7.9),
      "C" -> Stats(30.0, 70.0, 40.8, 40.5, 7.2),
      "D" -> Stats(40.0, 80.0, 50.8, 50.5, 7.5)
    )
    .domain("Foo", Categories)(_._1)
    .y("Stats")(_._2)
    .show()

}
