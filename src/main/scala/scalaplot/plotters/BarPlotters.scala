package scalaplot
package plotters

import org.jfree.chart.{ChartFactory, JFreeChart}

import scalaplot.util.{ExtractDomainRange, Ord, ToNumber}

private[scalaplot] trait BarPlotters { self: Plotter.type =>

  implicit def bar[F[_], A, Extractor[_, _], Domain : Ord, Range : ToNumber](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Range]
  ): Plotter[F, A, Extractor, Bar.type, Domain, Categories.type, Range] = new Plotter[F, A, Extractor, Bar.type, Domain, Categories.type, Range] {
    def create(chart: Chart[F, A, Extractor, Bar.type, Domain, Categories.type, Range]): JFreeChart = {
      val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
      ChartFactory.createBarChart(
        chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
        data.ProductSeriesCategoryDataset(seriesNames.toArray, domainRange)
      )
    }
  }

  implicit def stackedBar[F[_], A, Extractor[_, _], Domain : Ord, Range : ToNumber](implicit
    extractDomainRange: ExtractDomainRange[Extractor, F, A, Domain, Range]
  ): Plotter[F, A, Extractor, StackedBar.type, Domain, Categories.type, Range] = new Plotter[F, A, Extractor, StackedBar.type, Domain, Categories.type, Range] {
    def create(chart: Chart[F, A, Extractor, StackedBar.type, Domain, Categories.type, Range]): JFreeChart = {
      val (domainRange, seriesNames) = extractDomainRange(chart.domainF, chart.rangeF)(chart.plotData)
      ChartFactory.createStackedBarChart(
        chart.title, chart.domainAxisLabel, chart.rangeAxisLabel,
        data.ProductSeriesCategoryDataset(seriesNames.toArray, domainRange)
      )
    }
  }



}
