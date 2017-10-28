package scalaplot

import org.jfree.chart.JFreeChart
import org.jfree.ui.Drawable

import scalaplot.show.ShowPlot
import scalaplot.util.ExtractorOf

case class Chart[F[_], T, Extractor[_, _], PT <: PlotType, Domain, DomainAxisType <: AxisType, Range](
  title: String,
  plotType: PT,
  plotData: F[T] = Nil,
  domainF: Extractor[T, Domain] = null.asInstanceOf[Extractor[T, Domain]],
  domainAxisLabel: String = "",
  domainAxisType: DomainAxisType = Values,
  rangeF: Seq[(String, Extractor[T, Range])] = Nil,
  rangeAxisLabel: String = "",
  width: Int = 800,
  height: Int = 600
) {

  def data[F1[_], T1](data: F1[T1])(implicit extractorOf: ExtractorOf[F1]): Chart[F1, T1, extractorOf.Extractor, PT, Domain, DomainAxisType, Range] =
    copy(plotData = data, domainF = null.asInstanceOf[extractorOf.Extractor[T1, Domain]], rangeF = Nil)

  def dataPoints[T1](data: T1, datas: T1*): Chart[Seq, T1, Function1, PT, Domain, DomainAxisType, Range] =
    copy[Seq, T1, Function1, PT, Domain, DomainAxisType, Range](plotData = data +: datas, domainF = ((t: T1) => throw new NotImplementedError("Domain was not configured")) : T1 => Domain, rangeF = Nil)

  def domain[Domain1, AxisType1 <: AxisType](
    label: String,
    axisType: AxisType1 = domainAxisType)(
    f: Extractor[T, Domain1]
  ): Chart[F, T, Extractor, PT, Domain1, AxisType1, Range] =
    copy(domainF = f, domainAxisLabel = label, domainAxisType = axisType)

  def x[Domain1, AxisType1 <: AxisType](
    label: String,
    axisType: AxisType1 = domainAxisType)(
    f: Extractor[T, Domain1]
  ): Chart[F, T, Extractor, PT, Domain1, AxisType1, Range] = domain(label, axisType)(f)

  /**
    * For an XY plot where the Y is not dependent on the X (like a scatter plot),
    * we treat the "domain" as the series and the "range" as the tuples that belong to that series.
    */
  def xySeries[X, Y](
    xLabel: String,
    yLabel: String)(
    seriesF: Extractor[T, String])(
    xyF: Extractor[T, (X, Y)]
  ): Chart[F, T, Extractor, PT, String, Categories.type, (X, Y)] =
    copy(domainF = seriesF, rangeF = Seq("" -> xyF), domainAxisType = Categories, domainAxisLabel = xLabel, rangeAxisLabel = yLabel)

  def rangeAxis(label: String): Chart[F, T, Extractor, PT, Domain, DomainAxisType, Range] = copy(rangeAxisLabel = label)

  def range[Range1 >: Range](
    seriesLabel: String)(
    range: Extractor[T, Range1]
  ): Chart[F, T, Extractor, PT, Domain, DomainAxisType, Range1] =
    copy(rangeF = rangeF.map(t => t._1 -> t._2.asInstanceOf[Extractor[T, Range1]]) :+ (seriesLabel -> range))

  def y[Range1 >: Range](
    seriesLabel: String)(
    rangeExtractor: Extractor[T, Range1]
  ): Chart[F, T, Extractor, PT, Domain, DomainAxisType, Range1] = range[Range1](seriesLabel)(rangeExtractor)

  def build()(implicit
    plotter: Plotter[F, T, Extractor, PT, Domain, DomainAxisType, Range]
  ): Plot = Plot(width, height, plotter.create(this))

  def show()(implicit
    plotter: Plotter[F, T, Extractor, PT, Domain, DomainAxisType, Range],
    showPlot: ShowPlot
  ): Unit = showPlot(build())
}

object Chart {

  //class ChartTraversableOps[F[A] <: Traversable[A], T, PT <: PlotType, Domain,]

}