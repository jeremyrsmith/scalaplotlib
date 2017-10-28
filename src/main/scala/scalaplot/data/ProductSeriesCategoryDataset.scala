package scalaplot.data

import java.util

import scala.collection.JavaConverters._
import org.jfree.data.DomainOrder
import org.jfree.data.category.CategoryDataset
import org.jfree.data.general.{DatasetChangeListener, DatasetGroup}

import scalaplot.util.{Ord, ToNumber}

class ProductSeriesCategoryDataset[X : Ord, Y : ToNumber](
  seriesNames: Array[String],
  xValues: Vector[X],
  yValues: Vector[Vector[Y]],
  domainOrder: DomainOrder = DomainOrder.NONE
) extends CategoryDataset {

  protected val xIndices: Map[X, Int] = xValues.zipWithIndex.toMap
  protected val yIndices: Map[String, Int] = seriesNames.zipWithIndex.toMap

  @inline def value(row: Int, column: Int): Y = yValues(row)(column)

  def getRowCount: Int = xValues.length

  def getColumnCount: Int = seriesNames.length

  def getValue(row: Int, column: Int): Number = ToNumber[Y].apply(value(row, column))

  def addChangeListener(listener: DatasetChangeListener): Unit = ()

  def setGroup(group: DatasetGroup): Unit = ()

  def removeChangeListener(listener: DatasetChangeListener): Unit = ()

  def getGroup: DatasetGroup = null

  def getRowKeys: util.List[_] = xValues.asJava

  def getColumnKeys: util.List[_] = seriesNames.toSeq.asJava

  def getRowKey(row: Int): Comparable[_] = Ord[X].toComparable(xValues(row))

  def getValue(rowKey: Comparable[_], columnKey: Comparable[_]): Number = getValue(getRowIndex(rowKey), getColumnIndex(columnKey))

  def getColumnKey(column: Int): Comparable[_] = seriesNames(column)

  def getRowIndex(key: Comparable[_]): Int = xIndices(key.asInstanceOf[X])

  def getColumnIndex(key: Comparable[_]): Int = yIndices(key.asInstanceOf[String])

}

object ProductSeriesCategoryDataset {
  def apply[X : Ord, Y : ToNumber](
    seriesNames: Array[String],
    values: Seq[(X, Seq[Y])]
  ): ProductSeriesCategoryDataset[X, Y] = {
    val (xValues, yValues) = values.unzip
    new ProductSeriesCategoryDataset(seriesNames, xValues.toVector, yValues.map(_.toVector).toVector)
  }
}