package example

import java.time.LocalDate

// Fail early
object V1 {

  def validateTicker(ticker: String): Either[String, String] =
    if(ticker.isEmpty)
      Left("Ticker cannot be empty")
    else
      Right(ticker)

  def validateQuantity(quantity: Long): Either[String, Long] =
    if(quantity <= 0)
      Left("Quantity must be positive")
    else
      Right(quantity)

  def validateExpiry(optExpiry: Option[LocalDate], today: LocalDate): Either[String, Expiry] =
    optExpiry match {
      case None         => Right(Expiry.Never)
      case Some(expiry) =>
        val min = today
        val max = today.plusMonths(1)
        if (expiry.isBefore(min) || expiry.isAfter(max))
          Left(s"Expiry must be between $min and $max")
        else
          Right(Expiry.ValidUntil(expiry))
    }

  def validateOrder(request: CreateOrderRequest, today: LocalDate): Either[String, Order] =
    for {
      ticker   <- validateTicker(request.ticker)
      quantity <- validateQuantity(request.quantity)
      expiry   <- validateExpiry(request.expiry, today)
    } yield Order(request.id, ticker, quantity, expiry)

}
