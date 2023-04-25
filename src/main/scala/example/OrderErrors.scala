package example

import cats.Semigroup

case class OrderErrors(value: Map[FieldId, List[String]])

object OrderErrors {
  def single(fieldId: FieldId, error: String): OrderErrors =
    OrderErrors(Map(fieldId -> List(error)))

  given Semigroup[OrderErrors] = new Semigroup[OrderErrors] {
    def combine(x: OrderErrors, y: OrderErrors): OrderErrors =
      OrderErrors(
        (x.value.toList ++ y.value.toList)
          .groupMapReduce(_._1)(_._2)(_ ++ _)
      )
  }
}


