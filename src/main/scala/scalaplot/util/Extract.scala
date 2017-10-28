package scalaplot.util

import org.apache.spark.sql.{Dataset, Encoder, TypedColumn}

import scala.annotation.implicitNotFound
import scala.collection.TraversableLike
import scala.collection.generic.CanBuildFrom

@implicitNotFound(
"""Don't know how to use ${F} as a data source.
In order to use ${F} as a data source, there needs to be a scalaplot.util.Extractor defined.""")
abstract class ExtractorOf[F[_]] {
  type Extractor[_, _]
}

object ExtractorOf {
  type Aux[F[_], Extractor0[_, _]] = ExtractorOf[F] { type Extractor[A, B] = Extractor0[A, B] }

  implicit def traversableFn[F[T] <: Traversable[T]]: ExtractorOf.Aux[F, Function1] = new ExtractorOf[F] {
    type Extractor[A, B] = A => B
  }

  implicit val sparkColumn: ExtractorOf.Aux[Dataset, ({ type L[A, B] = TypedColumn[Any, B]})#L] = new ExtractorOf[Dataset] {
    type Extractor[A, B] = TypedColumn[Any, B]
  }
}

abstract class ExtractDomainRange[Extractor[_, _], F[_], A, Domain, Range] {
  def apply(
    ed: Extractor[A, Domain],
    er: Seq[(String, Extractor[A, Range])]
  ): F[A] => (Seq[(Domain, Seq[Range])], Seq[String]) = {
    fa =>
      val (names, extractors) = er.unzip
      (map(ed, extractors) andThen collect)(fa) -> names
  }
  def group(eg: Extractor[A, Domain], er: Extractor[A, Range]): F[A] => F[(Domain, Seq[Range])]
  def map(ed: Extractor[A, Domain], er: Seq[Extractor[A, Range]]): F[A] => F[(Domain, Seq[Range])]
  def collect: F[(Domain, Seq[Range])] => Seq[(Domain, Seq[Range])]
}

object ExtractDomainRange {

  implicit def traversableFns[F[T] <: TraversableLike[T, F[T]], A, Domain, Range](implicit
    canBuildFrom: CanBuildFrom[F[A], (Domain, Seq[Range]), F[(Domain, Seq[Range])]]
  ): ExtractDomainRange[Function1, F, A, Domain, Range] =
    new ExtractDomainRange[Function1, F, A, Domain, Range] {
      def map(ed: A => Domain, er: Seq[A => Range]): F[A] => F[(Domain, Seq[Range])] = {
        fa => fa.map[(Domain, Seq[Range]), F[(Domain, Seq[Range])]] {
          row => ed(row) -> er.map(_.apply(row))
        }
      }

      def group(eg: A => Domain, er: A => Range): F[A] => F[(Domain, Seq[Range])] = {
        fa =>
          val grouped: Map[Domain, Seq[Range]] = fa.groupBy(eg).mapValues {
            a => a.toSeq.map(er)
          }
          val builder = canBuildFrom.apply()
          grouped.foreach(builder.+=)
          builder.result()
      }

      def collect: F[(Domain, Seq[Range])] => Seq[(Domain, Seq[Range])] = _.toSeq
    }

  implicit def sparkColumns[A, Domain : Encoder, Range](implicit
    rangeSeqEncoder: Encoder[Seq[Range]]
  ): ExtractDomainRange[({ type L[A1, B] = TypedColumn[Any, B] })#L, Dataset, A, Domain, Range] =
    new ExtractDomainRange[({ type L[A1, B] = TypedColumn[Any, B] })#L, Dataset, A, Domain, Range] {
      def map(ed: TypedColumn[Any, Domain], er: Seq[TypedColumn[Any, Range]]): Dataset[A] => Dataset[(Domain, Seq[Range])] = {
        ds => ds.select(ed, org.apache.spark.sql.functions.array(er: _*).as[Seq[Range]])
      }
      def group(ed: TypedColumn[Any, Domain], er: TypedColumn[Any, Range]): Dataset[A] => Dataset[(Domain, Seq[Range])] = {
        ds => ds.groupBy(ed).agg(org.apache.spark.sql.functions.collect_list(er))
            .select(ed, er.as[Seq[Range]])
      }
      def collect: Dataset[(Domain, Seq[Range])] => Seq[(Domain, Seq[Range])] = _.collect().toSeq
    }

}
