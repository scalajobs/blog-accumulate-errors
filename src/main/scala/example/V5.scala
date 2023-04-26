package example

import cats.implicits.*

import java.time.LocalDate

// Accumulate errors in a custom type
object V5 {

  def validateTicker(ticker: String): Either[OrderErrorsV2, String] =
    if(ticker.isEmpty)
      Left(OrderErrorsV2.single(FieldId.ticker, "cannot be empty"))
    else
      Right(ticker)

  def validateQuantity(quantity: Long): Either[OrderErrorsV2, Long] =
    if(quantity <= 0)
      Left(OrderErrorsV2.single(FieldId.quantity, "must be positive"))
    else
      Right(quantity)

  def validateExpiry(optExpiry: Option[LocalDate], today: LocalDate): Either[OrderErrorsV2, Expiry] =
    optExpiry match {
      case None         => Right(Expiry.Never)
      case Some(expiry) =>
        val min = today
        val max = today.plusMonths(1)
        if (expiry.isBefore(min) || expiry.isAfter(max))
          Left(OrderErrorsV2.single(FieldId.expiry, s"must be between $min and $max"))
        else
          Right(Expiry.ValidUntil(expiry))
    }

  def validateOrder(request: CreateOrderRequest, today: LocalDate): Either[OrderErrorsV2, Order] =
    (
      validateTicker(request.ticker),
      validateQuantity(request.quantity),
      validateExpiry(request.expiry, today),
    ).parMapN(
      (ticker, quantity, expiry) => Order(request.id, ticker, quantity, expiry)
    )
    // No given instance of type cats.NonEmptyParallel[([B] =>> Either[example.OrderErrors, B])] was found for parameter p of method parMapN in class Tuple3ParallelOps.
    //[error]    |I found:
    //[error]    |
    //[error]    |    cats.implicits.catsParallelForEitherAndValidated[example.OrderErrors](
    //[error]    |      /* missing */summon[cats.kernel.Semigroup[example.OrderErrors]]
    //[error]    |    )
    //[error]    |
    //[error]    |But no implicit values were found that match type cats.kernel.Semigroup[example.OrderErrors].

  def validateOrders(requests: List[CreateOrderRequest], today: LocalDate): Either[MultipleOrderErrorsV2, List[Order]] =
    requests
      .parTraverse(request =>
        validateOrder(request, today)
          .leftMap(MultipleOrderErrorsV2.forOrder(request.id, _))
      )

}
