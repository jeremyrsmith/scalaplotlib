package scalaplot.data

import org.jfree.data.DomainOrder
import org.jfree.data.general.{DatasetChangeListener, DatasetGroup}
import org.jfree.data.xy.XYDataset

class PointXYDoubleDataset(
  seriesNames: Array[String],
  values: Array[Array[(Double, Double)]]
) extends XYDataset {

  def getItemCount(series: Int): Int = values(series).size

  def getXValue(series: Int, item: Int): Double = values(series)(item)._1

  def getYValue(series: Int, item: Int): Double = values(series)(item)._2

  def getDomainOrder: DomainOrder = DomainOrder.ASCENDING

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

object PointXYDoubleDataset {
  def apply[X : Numeric, Y : Numeric](seriesNames: Array[String], values: Seq[Seq[(X, Y)]]): PointXYDoubleDataset = {
    import Numeric.Implicits._
    val doubles = values.map(_.map(t => t._1.toDouble -> t._2.toDouble).toArray).toArray
    new PointXYDoubleDataset(seriesNames, doubles)
  }
}
