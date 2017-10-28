package scalaplot.data

import scalaplot.util.ToNumber

/**
  * Data type for a straightforward distribution plot, like box-and-whisker. Doesn't include outliers.
 *
  * @param min     The minimum value
  * @param max     The maximum value
  * @param mean    The mean value
  * @param median  The median value
  * @param stdDev  The standard deviation (kept as a Double due to no typeclass support for sqrt)
  */
case class Stats[T : ToNumber](
  min: T,
  max: T,
  mean: T,
  median: T,
  stdDev: Double
) {
  def toNumber[U <: Number](implicit tn: ToNumber.Aux[T, U]): Stats[U] = copy(
    min = tn(min), max = tn(max), mean = tn(mean), median = tn(median)
  )
}

object Stats {
  implicit def toNumber[T](implicit toNumber: ToNumber[T]): ToNumber.Aux[Stats[T], toNumber.Out] = new ToNumber[Stats[T]] {
    type Out = toNumber.Out
    def apply(s: Stats[T]): Out = toNumber(s.median)
  }

}
