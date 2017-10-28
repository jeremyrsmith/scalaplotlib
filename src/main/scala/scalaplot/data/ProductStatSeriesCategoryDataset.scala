package scalaplot.data

import java.util

import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset

import scalaplot.util.{Ord, ToNumber}

class ProductStatSeriesCategoryDataset[X : Ord, Y : Numeric : ToNumber](
  seriesNames: Array[String],
  xValues: Vector[X],
  stats: Vector[Vector[Stats[Y]]]
) extends ProductSeriesCategoryDataset[X, Stats[Y]](seriesNames, xValues, stats) with BoxAndWhiskerCategoryDataset {

  import Numeric.Implicits._

  def getMinRegularValue(row: Int, column: Int): Number = ToNumber[Y].apply(value(row, column).min)

  def getMinRegularValue(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getMinRegularValue(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getQ1Value(row: Int, column: Int): Number = value(row, column) match {
    case v => v.median.toDouble() - v.stdDev
  }

  def getQ1Value(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getQ1Value(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getMinOutlier(row: Int, column: Int): Number = null

  def getMinOutlier(rowKey: Comparable[_], columnKey: Comparable[_]): Number = null

  def getMaxOutlier(row: Int, column: Int): Number = null

  def getMaxOutlier(rowKey: Comparable[_], columnKey: Comparable[_]): Number = null

  def getMaxRegularValue(row: Int, column: Int): Number = ToNumber[Y].apply(value(row, column).max)

  def getMaxRegularValue(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getMaxRegularValue(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getQ3Value(row: Int, column: Int): Number = value(row, column) match {
    case v => v.median.toDouble() + v.stdDev
  }

  def getQ3Value(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getQ3Value(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getMedianValue(row: Int, column: Int): Number = ToNumber[Y].apply(value(row, column).median)

  def getMedianValue(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getMedianValue(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getMeanValue(row: Int, column: Int): Number = ToNumber[Y].apply(value(row, column).mean)

  def getMeanValue(rowKey: Comparable[_], columnKey: Comparable[_]): Number =
    getMeanValue(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getOutliers(row: Int, column: Int): util.List[_] = java.util.Collections.emptyList()

  def getOutliers(rowKey: Comparable[_], columnKey: Comparable[_]): util.List[_] = java.util.Collections.emptyList()
}

object ProductStatSeriesCategoryDataset {
  def apply[X : Ord, Y : ToNumber : Numeric](
    seriesNames: Array[String],
    values: Seq[(X, Seq[Stats[Y]])]
  ): ProductStatSeriesCategoryDataset[X, Y] = {
    val (xValues, yValues) = values.unzip
    new ProductStatSeriesCategoryDataset(seriesNames, xValues.toVector, yValues.map(_.toVector).toVector)
  }

}