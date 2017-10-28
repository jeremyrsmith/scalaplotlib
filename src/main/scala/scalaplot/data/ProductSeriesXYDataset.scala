package scalaplot.data

import org.jfree.data.DomainOrder
import org.jfree.data.general.{DatasetChangeListener, DatasetGroup}
import org.jfree.data.xy.TableXYDataset

class ProductSeriesXYDataset(
  seriesNames: Array[String],
  xValues: Array[Double],
  yValues: Array[Array[Double]],
  domainOrder: DomainOrder = DomainOrder.NONE
) extends TableXYDataset {
  import Numeric.Implicits._

  def getItemCount: Int = xValues.length

  def getItemCount(series: Int): Int = xValues.length

  def getXValue(series: Int, item: Int): Double = xValues(item)

  def getYValue(series: Int, item: Int): Double = yValues(item)(series)

  def getDomainOrder: DomainOrder = domainOrder

  def getX(series: Int, item: Int): Number = getXValue(series, item)

  def getY(series: Int, item: Int): Number = getYValue(series, item)

  def getSeriesKey(series: Int): Comparable[_] = seriesNames(series)

  def indexOf(seriesKey: Comparable[_]): Int = seriesNames.indexOf(seriesKey.asInstanceOf[String])

  def getSeriesCount: Int = seriesNames.length

  def addChangeListener(listener: DatasetChangeListener): Unit = ()

  def setGroup(group: DatasetGroup): Unit = ()

  def removeChangeListener(listener: DatasetChangeListener): Unit = ()

  def getGroup: DatasetGroup = null
}

object ProductSeriesXYDataset {
  def apply[X : Numeric, Y : Numeric](
    seriesNames: Array[String],
    values: Seq[(X, Seq[Y])]
  ): ProductSeriesXYDataset = {
    import Numeric.Implicits._
    val (xValues, yValues) = values.map {
      case (x, y) => x.toDouble -> y.map(_.toDouble)
    }.unzip
    new ProductSeriesXYDataset(seriesNames, xValues.toArray, yValues.map(_.toArray).toArray)
  }
}
