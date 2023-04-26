package example

import cats.implicits.*

import java.time.LocalDate

type OrderErrorsV1         = Map[FieldId, List[String]]
type MultipleOrderErrorsV1 = Map[OrderId, OrderErrorsV1]

object OrderErrorV1 {
  def single(fieldId: FieldId, error: String): OrderErrorsV1 =
    Map(fieldId -> List(error))
}

// Accumulate errors in a Map
object V4 {

  def validateTicker(ticker: String): Either[OrderErrorsV1, String] =
    if(ticker.isEmpty)
      Left(OrderErrorV1.single(FieldId.ticker, "cannot be empty"))
    else
      Right(ticker)

  def validateQuantity(quantity: Long): Either[OrderErrorsV1, Long] =
    if(quantity <= 0)
      Left(OrderErrorV1.single(FieldId.quantity, "must be positive"))
    else
      Right(quantity)

  def validateExpiry(optExpiry: Option[LocalDate], today: LocalDate): Either[OrderErrorsV1, Expiry] =
    optExpiry match {
      case None         => Right(Expiry.Never)
      case Some(expiry) =>
        val min = today
        val max = today.plusMonths(1)
        if (expiry.isBefore(min) || expiry.isAfter(max))
          Left(OrderErrorV1.single(FieldId.expiry, s"must be between $min and $max"))
        else
          Right(Expiry.ValidUntil(expiry))
    }

  def validateOrder(request: CreateOrderRequest, today: LocalDate): Either[OrderErrorsV1, Order] =
    (
      validateTicker(request.ticker),
      validateQuantity(request.quantity),
      validateExpiry(request.expiry, today),
    ).parMapN(
      (ticker, quantity, expiry) => Order(request.id, ticker, quantity, expiry)
    )

  def validateOrders(requests: List[CreateOrderRequest], today: LocalDate): Either[MultipleOrderErrorsV1, List[Order]] =
    requests
      .parTraverse(request =>
        validateOrder(request, today)
          .leftMap(orderError => Map(request.id -> orderError))
      )

}
