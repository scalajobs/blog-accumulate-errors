package example

import cats.Semigroup

opaque type OrderErrorsV2 = Map[String, List[String]]

object OrderErrorsV2 {
  def apply(pairs: (String, List[String])*): OrderErrorsV2 =
    pairs.toMap

  given Semigroup[OrderErrorsV2] = new Semigroup[OrderErrorsV2] {
    def combine(x: OrderErrorsV2, y: OrderErrorsV2): OrderErrorsV2 =
        (x.toList ++ y.toList)
          .groupMapReduce(_._1)(_._2)(_ ++ _)
  }
}


