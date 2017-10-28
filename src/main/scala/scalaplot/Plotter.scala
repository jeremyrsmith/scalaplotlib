package scalaplot

import org.jfree.ui.Drawable

import scalaplot.plotters._

abstract class Plotter[F[_], T, Extractor[_, _], PT <: PlotType, Domain, DomainAxisType <: AxisType, Range] {
  def create(chart: Chart[F, T, Extractor, PT, Domain, DomainAxisType, Range]): Drawable
}

object Plotter extends AreaPlotters
  with BarPlotters
  with StatsPlotters
  with LinePlotters
  with PiePlotters
  with ScatterPlotters
