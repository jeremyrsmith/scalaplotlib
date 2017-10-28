package scalaplot

sealed trait PlotType

case object Area extends PlotType
case object StackedArea extends PlotType
case object Bar extends PlotType
case object StackedBar extends PlotType
case object BoxAndWhisker extends PlotType
case object Pie extends PlotType
case object Line extends PlotType
case object Scatter extends PlotType
