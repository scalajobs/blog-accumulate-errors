package example

import cats.Semigroup

case class MultipleOrderErrorsV2(value: Map[OrderId, OrderErrorsV2])

object MultipleOrderErrorsV2 {
  
  def forOrder(orderId: OrderId, errors: OrderErrorsV2): MultipleOrderErrorsV2 =
    MultipleOrderErrorsV2(Map(orderId -> errors))

  given Semigroup[MultipleOrderErrorsV2] = new Semigroup[MultipleOrderErrorsV2] {
    def combine(x: MultipleOrderErrorsV2, y: MultipleOrderErrorsV2): MultipleOrderErrorsV2 =
      MultipleOrderErrorsV2(x.value ++ y.value)
  }
}