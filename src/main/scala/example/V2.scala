package example

import cats.implicits.*
import java.time.LocalDate

object V2 {

  def validateTicker(ticker: String): Either[List[String], String] =
    if(ticker.isEmpty)
      Left(List("Ticker cannot be empty"))
    else
      Right(ticker)

  def validateQuantity(quantity: Long): Either[List[String], Long] =
    if(quantity <= 0)
      Left(List("Quantity must be positive"))
    else
      Right(quantity)

  def validateExpiry(optExpiry: Option[LocalDate], today: LocalDate): Either[List[String], Expiry] =
    optExpiry match {
      case None         => Right(Expiry.Never)
      case Some(expiry) =>
        val min = today
        val max = today.plusMonths(1)
        if (expiry.isBefore(min) || expiry.isAfter(max))
          Left(List(s"Expiry must be between $min and $max"))
        else
          Right(Expiry.ValidUntil(expiry))
    }

  def validateOrder(request: CreateOrderRequest, today: LocalDate): Either[List[String], Order] =
    (
      validateTicker(request.ticker),
      validateQuantity(request.quantity),
      validateExpiry(request.expiry, today),
      ).parMapN(
      (ticker, quantity, expiry) => Order(request.id, ticker, quantity, expiry)
    )

}
