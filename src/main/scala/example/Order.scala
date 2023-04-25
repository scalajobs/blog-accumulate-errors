package example

case class Order(
  id      : OrderId,
  ticker  : String,
  quantity: Long,
  expiry  : Expiry,
)
