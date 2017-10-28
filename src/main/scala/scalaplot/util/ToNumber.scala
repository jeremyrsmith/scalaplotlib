package scalaplot.util

abstract class ToNumber[T] {
  type Out <: Number
  def apply(t: T): Out
}

object ToNumber {

  type Aux[T, Out0 <: Number] = ToNumber[T] { type Out = Out0 }
  def apply[T](implicit inst: ToNumber[T]): ToNumber[T] = inst
  def instance[T, Out0 <: Number](fn: T => Out0): Aux[T, Out0] = new ToNumber[T] {
    type Out = Out0
    def apply(t: T): Out = fn(t)
  }

  implicit val byte: ToNumber[Byte] = instance(Byte.box)
  implicit val short: ToNumber[Short] = instance(Short.box)
  implicit val int: ToNumber[Int] = instance(Int.box)
  implicit val long: ToNumber[Long] = instance(Long.box)
  implicit val float: ToNumber[Float] = instance(Float.box)
  implicit val double: ToNumber[Double] = instance(Double.box)

  implicit def number[T <: Number]: ToNumber[T] = instance(identity)

}