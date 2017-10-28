package scalaplot
package plotters

import org.jfree.chart.{ChartFactory, JFreeChart}

import scalaplot.data.{Stats, ProductStatSeriesCategoryDataset}
import scalaplot.util.{ExtractDomainRange, Ord, ToNumber}

private[scalaplot] trait StatsPlotters extends StatsPlotters0 { self: Plotter.type =>

  /**
    * When the domain (categories) are Strings, we probably want to use them as the series for this particular plot.
    * So we flip the series label with the domain categories
    */
  implicit def boxAndWhiskerStatsStringCategories[F[_], A, Extractor[_, _], Range : ToNumber : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, String, Stats[Range]]
  ): Plotter[F, A, Extractor, BoxAndWhisker.type, String, Categories.type, Stats[Range]] =
    new Plotter[F, A, Extractor, BoxAndWhisker.type, String, Categories.type, Stats[Range]] {
      def create(chart: Chart[F, A, Extractor, BoxAndWhisker.type, String, Categories.type, Stats[Range]]): JFreeChart = {
        val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
        val (categorySeries, stats) = domainRange.unzip
        val data = seriesNames.zipWithIndex.map {
          case (name, index) => name -> stats.map(_(index))
        }
        ChartFactory.createBoxAndWhiskerChart(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          ProductStatSeriesCategoryDataset(categorySeries.toArray, data),
          seriesNames.size > 1
        )
      }
    }


}

private[scalaplot] trait StatsPlotters0 { self: StatsPlotters =>

  implicit def boxAndWhiskerStatsCategories[F[_], A, Extractor[_, _], Domain : Ord, Range : ToNumber : Numeric](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Stats[Range]]
  ): Plotter[F, A, Extractor, BoxAndWhisker.type, Domain, Categories.type, Stats[Range]] =
    new Plotter[F, A, Extractor, BoxAndWhisker.type, Domain, Categories.type, Stats[Range]] {
      def create(chart: Chart[F, A, Extractor, BoxAndWhisker.type, Domain, Categories.type, Stats[Range]]): JFreeChart = {
        val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
        ChartFactory.createBoxAndWhiskerChart(
          chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
          ProductStatSeriesCategoryDataset(seriesNames.toArray, domainRange),
          true
        )
      }
    }
}
