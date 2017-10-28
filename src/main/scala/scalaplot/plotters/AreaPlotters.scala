package scalaplot
package plotters

import org.jfree.chart.{ChartFactory, JFreeChart}

import scalaplot.util.{ExtractDomainRange, ToNumber}

private[scalaplot] trait AreaPlotters { self: Plotter.type =>

  implicit def areaXY[F[_], A, Extractor[_, _], Domain : Numeric, Range : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Range]
  ): Plotter[F, A, Extractor, Area.type, Domain, Values.type, Range] =
    new Plotter[F, A, Extractor, Area.type, Domain, Values.type, Range] {
      def create(
        chart: Chart[F, A, Extractor, Area.type, Domain, Values.type, Range]
      ): JFreeChart = {
        val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
        ChartFactory.createXYAreaChart(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          data.ProductSeriesXYDataset(seriesNames.toArray, domainRange)
        )
      }
    }

  implicit def stackedAreaXY[F[_], A, Extractor[_, _], Domain : Numeric, Range : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Range]
  ): Plotter[F, A, Extractor, StackedArea.type, Domain, Values.type, Range] =
    new Plotter[F, A, Extractor, StackedArea.type, Domain, Values.type, Range] {
      def create(
        chart: Chart[F, A, Extractor, StackedArea.type, Domain, Values.type, Range]
      ): JFreeChart = {
        val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
        ChartFactory.createStackedXYAreaChart(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          data.ProductSeriesXYDataset(seriesNames.toArray, domainRange)
        )
      }
    }

}
