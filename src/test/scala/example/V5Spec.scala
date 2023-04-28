package example

import cats.Semigroup
import example.V5.*

import java.time.LocalDate

class V5Spec extends munit.FunSuite {

  val createOrder1 = CreateOrderRequest(
    id       = OrderId("1111"),
    ticker   = "AAPL",
    quantity = 10,
    expiry   = None,
  )

  val createOrder2 = CreateOrderRequest(
    id       = OrderId("2222"),
    ticker   = "AAPL",
    quantity = -2,
    expiry   = None,
  )

  val createOrder3 = CreateOrderRequest(
    id       = OrderId("3333"),
    ticker   = "",
    quantity = -2,
    expiry   = Some(LocalDate.of(2022, 1, 1)),
  )


  test("success") {
    val result = validateOrder(createOrder1, LocalDate.of(2023,4,24))

    assertEquals(result, Right(Order(OrderId("1111"), "AAPL", 10, Expiry.Never)))
  }

  test("one error") {
    val result = validateOrder(createOrder2, LocalDate.of(2023,4,24))

    assertEquals(result, Left(OrderErrorsV2("quantity" -> List("must be positive"))))
  }

  test("two errors") {
    val result = validateOrder(createOrder3, LocalDate.of(2023,4,24))

    assertEquals(result, Left(OrderErrorsV2(
      "quantity" -> List("must be positive"),
      "expiry"   -> List("must be between 2023-04-24 and 2023-05-24"),
      "ticker"   -> List("cannot be empty"),
    )))
  }

  test("multiple orders") {
    val result = validateOrders(List(createOrder1, createOrder2, createOrder3), LocalDate.of(2023,4,24))

    assertEquals(result, Left(MultipleOrderErrorsV2(
      createOrder2.id -> OrderErrorsV2(
        "quantity" -> List("must be positive"),
      ),
      createOrder3.id -> OrderErrorsV2(
        "quantity" -> List("must be positive"),
        "expiry"   -> List("must be between 2023-04-24 and 2023-05-24"),
        "ticker"   -> List("cannot be empty"),
      ),
    )))
  }

}
