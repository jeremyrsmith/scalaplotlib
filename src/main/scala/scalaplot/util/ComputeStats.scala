package scalaplot
package util

import org.apache.spark.sql.{Dataset, Encoder, TypedColumn}

import scalaplot.data.Stats
import Fractional.Implicits._

abstract class ComputeStats[F[_], Extractor[_, _], A, Range] {
  def computeStats(
    fa: F[A],
    extractors: Map[String, Extractor[A, Range]]
  ): Map[String, Stats[Range]]
}

object ComputeStats {
  // TODO: Performance could be improved here by quite a bit
  implicit def traversable[F[T] <: Traversable[T], A, Range : ToNumber](implicit
    fracRange: Fractional[Range]
  ): ComputeStats[F, Function1, A, Range] = new ComputeStats[F, Function1, A, Range] {
    def computeStats(fa: F[A], extractors: Map[String, A => Range]): Map[String, Stats[Range]] = {
      extractors.mapValues {
        fn =>
          val values = fa.map(fn).toSeq.sorted
          val size = values.size
          val sum = values.sum
          val mean = sum / fracRange.fromInt(size)
          val stdDev = math.sqrt((values.map(v => (v - mean) * (v - mean)).sum / fracRange.fromInt(size)).toDouble)
          val median = values(size / 2)
          Stats(
            min = values.head,
            max = values.last,
            mean = mean,
            median = median,
            stdDev = stdDev
          )
      }
    }
  }

  implicit def dataset[A, Range : ToNumber](implicit
    statsEncoder: Encoder[Stats[Range]],
    mapEncoder: Encoder[Tuple1[scala.collection.Map[String, Stats[Range]]]]
  ): ComputeStats[Dataset, AnyCol, A, Range] = new ComputeStats[Dataset, AnyCol, A, Range] {
    def computeStats(fa: Dataset[A], extractors: Map[String, TypedColumn[Any, Range]]): Map[String, Stats[Range]] = {
      import org.apache.spark.sql.functions.{struct, map, lit, avg, stddev, min, max, ntile}
      import fa.sparkSession.implicits._

      val selects = extractors.toSeq.flatMap {
        case (name, col) => Seq(
          lit(name),
          struct(
            min(col) as "min",
            max(col) as "max",
            avg(col) as "mean",
            stddev(col) as "stdDev",
            // unfortunately Spark 2.0.0 doesn't expose a way to get the median without hive
            // TODO: fix this
            avg(col) as "median"
          )
        )
      }
      fa.select(map(selects: _*) as "_1").as[Tuple1[scala.collection.Map[String, Stats[Range]]]].collect().head._1.toMap
    }
  }

}
