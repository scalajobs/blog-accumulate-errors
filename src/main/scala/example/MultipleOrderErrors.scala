package example

import cats.Semigroup

case class MultipleOrderErrors(value: Map[OrderId, OrderErrors])

object MultipleOrderErrors {
  
  def forOrder(orderId: OrderId, errors: OrderErrors): MultipleOrderErrors =
    MultipleOrderErrors(Map(orderId -> errors))

  given Semigroup[MultipleOrderErrors] = new Semigroup[MultipleOrderErrors] {
    def combine(x: MultipleOrderErrors, y: MultipleOrderErrors): MultipleOrderErrors =
      MultipleOrderErrors(x.value ++ y.value)
  }
}