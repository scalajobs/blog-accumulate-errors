package example

import example.V2.*

import java.time.LocalDate

class V2Spec extends munit.FunSuite {

  test("success") {
    val result = validateOrder(
      CreateOrderRequest(
        id       = OrderId("1234"),
        ticker   = "AAPL",
        quantity = 10,
        expiry   = None,
      ),
      LocalDate.of(2023,4,24),
    )

    assertEquals(result, Right(Order(OrderId("1234"), "AAPL", 10, Expiry.Never)))
  }

  test("one error") {
    val result = validateOrder(
      CreateOrderRequest(
        id       = OrderId("1234"),
        ticker   = "AAPL",
        quantity = -2,
        expiry   = None,
      ),
      LocalDate.of(2023,4,24),
    )

    assertEquals(result, Left("Quantity must be positive"))
  }

  test("multiple errors") {
    val result = validateOrder(
      CreateOrderRequest(
        id       = OrderId("1234"),
        ticker   = "",
        quantity = -2,
        expiry   = Some(LocalDate.of(2022, 1, 1)),
      ),
      LocalDate.of(2023,4,24),
    )

    assertEquals(result, Left(
      "Ticker cannot be emptyQuantity must be positiveExpiry must be between 2023-04-24 and 2023-05-24"
    ))
  }

}
