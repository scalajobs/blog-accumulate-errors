package example

import java.time.LocalDate

case class CreateOrderRequest(
  id      : OrderId,
  ticker  : String,
  quantity: Long,
  expiry  : Option[LocalDate],
)
