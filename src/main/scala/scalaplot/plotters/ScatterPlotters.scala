package scalaplot
package plotters

import org.jfree.chart.{ChartFactory, JFreeChart}

import scalaplot.data.PointXYDoubleDataset
import scalaplot.util.ExtractDomainRange

private[scalaplot] trait ScatterPlotters { self: Plotter.type =>

  implicit def scatterXY[F[_], A, Extractor[_, _], X : Numeric, Y : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, String, (X, Y)]
  ): Plotter[F, A, Extractor, Scatter.type, String, Categories.type, (X, Y)] =
    new Plotter[F, A, Extractor, Scatter.type, String, Categories.type, (X, Y)] {
      def create(chart: Chart[F, A, Extractor, Scatter.type, String, Categories.type, (X, Y)]): JFreeChart = {
        val domainRange = extractDomainRange.collect(extractDomainRange.group(chart.domainF, chart.rangeF.head._2)(chart.plotData))
        val (seriesNames, data) = domainRange.unzip
        ChartFactory.createScatterPlot(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          PointXYDoubleDataset(seriesNames.distinct.toArray, data)
        )
      }
    }

}
