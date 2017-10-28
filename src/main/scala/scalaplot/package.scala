import org.apache.spark.sql.TypedColumn

package object scalaplot {

  type AnyCol[A, B] = TypedColumn[Any, B]

}
