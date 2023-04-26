package example

import cats.Semigroup

case class OrderErrorsV2(value: Map[FieldId, List[String]])

object OrderErrorsV2 {
  def single(fieldId: FieldId, error: String): OrderErrorsV2 =
    OrderErrorsV2(Map(fieldId -> List(error)))

  given Semigroup[OrderErrorsV2] = new Semigroup[OrderErrorsV2] {
    def combine(x: OrderErrorsV2, y: OrderErrorsV2): OrderErrorsV2 =
      OrderErrorsV2(
        (x.value.toList ++ y.value.toList)
          .groupMapReduce(_._1)(_._2)(_ ++ _)
      )
  }
}


