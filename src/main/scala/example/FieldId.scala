package example

case class FieldId(value: String)

object FieldId {
  val ticker   = FieldId("ticker")
  val quantity = FieldId("quantity")
  val expiry   = FieldId("expiry")
}
