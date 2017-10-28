package scalaplot

sealed abstract class AxisType

case object Categories extends AxisType
case object Values extends AxisType