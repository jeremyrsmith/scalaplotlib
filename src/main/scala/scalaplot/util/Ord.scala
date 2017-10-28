package scalaplot.util

// wraps Ordering so that we can get import-free resolution of things that don't have an Ordering in implicit scope
abstract class Ord[-T] {
  def compare(a: T, b: T): Int
  def ordering[U <: T]: Ordering[U]
  def toComparable(a: T): Comparable[_] = a match {
    case cmp: Comparable[_] => cmp
    case c => new Comparable[T] {
      def compareTo(that: T): Int = compare(c, that)
    }
  }
}

object Ord extends Ord0 {
  def apply[T](implicit ord: Ord[T]): Ord[T] = ord
}

private[util] trait Ord0 extends Ord1 { self: Ord.type =>
  implicit def fromOrdering[T : Ordering]: Ord[T] = new Ord[T] {
    override def compare(x: T, y: T): Int = implicitly[Ordering[T]].compare(x, y)
    override def ordering[U <: T]: Ordering[U] = new Ordering[U] {
      override def compare(x: U, y: U): Int = implicitly[Ordering[T]].compare(x, y)
    }
  }
}

private[util] trait Ord1 { self: Ord.type =>
  implicit def forComparable[T <: Comparable[U], U >: T]: Ord[T] = new Ord[T] {
    override def compare(x: T, y: T): Int = x.compareTo(y)
    override def ordering[U1 <: T]: Ordering[U1] = new Ordering[U1] {
      override def compare(x: U1, y: U1): Int = x.compareTo(y)
    }
  }
}
