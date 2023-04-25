package example

import example.V1.*

import java.time.LocalDate

class V1Spec extends munit.FunSuite {

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

  test("two errors") {
    val result = validateOrder(
      CreateOrderRequest(
        id       = OrderId("1234"),
        ticker   = "",
        quantity = -2,
        expiry   = None,
      ),
      LocalDate.of(2023,4,24),
    )

    assertEquals(result, Left("Ticker cannot be empty"))
  }

}
