package example

import cats.Semigroup

opaque type MultipleOrderErrorsV2 = Map[OrderId, OrderErrorsV2]

object MultipleOrderErrorsV2 {

  def apply(pairs: (OrderId, OrderErrorsV2)*): MultipleOrderErrorsV2 =
    pairs.toMap
  
  def forOrder(orderId: OrderId, errors: OrderErrorsV2): MultipleOrderErrorsV2 =
    Map(orderId -> errors)

  given Semigroup[MultipleOrderErrorsV2] = new Semigroup[MultipleOrderErrorsV2] {
    def combine(x: MultipleOrderErrorsV2, y: MultipleOrderErrorsV2): MultipleOrderErrorsV2 =
      x ++ y
  }
}