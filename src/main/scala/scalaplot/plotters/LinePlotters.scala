package scalaplot
package plotters

import org.jfree.chart.{ChartFactory, JFreeChart}

import scalaplot.data.ProductSeriesXYDataset
import scalaplot.util.ExtractDomainRange

private[scalaplot] trait LinePlotters { self: Plotter.type =>

  implicit def lineXY[F[_], A, Extractor[_, _], Domain : Numeric, Range : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Range]
  ): Plotter[F, A, Extractor, Line.type, Domain, Values.type, Range] =
    new Plotter[F, A, Extractor, Line.type, Domain, Values.type, Range] {
      def create(chart: Chart[F, A, Extractor, Line.type, Domain, Values.type, Range]): JFreeChart = {
        val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
        ChartFactory.createXYLineChart(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          ProductSeriesXYDataset(seriesNames.toArray, domainRange)
        )
      }
    }

}
