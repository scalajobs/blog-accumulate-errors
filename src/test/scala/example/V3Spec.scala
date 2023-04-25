package example

import example.V3.*

import java.time.LocalDate

class V3Spec extends munit.FunSuite {

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

    assertEquals(result, Left(OrderErrors.single(FieldId.quantity, "must be positive")))
  }

  test("two errors") {
    val result = validateOrder(createOrder3, LocalDate.of(2023,4,24))

    assertEquals(result, Left(OrderErrors(Map(
      FieldId.quantity -> List("must be positive"),
      FieldId.expiry   -> List("must be between 2023-04-24 and 2023-05-24"),
      FieldId.ticker   -> List("cannot be empty"),
    ))))
  }

  test("multiple orders") {
    val result = validateOrders(List(createOrder1, createOrder2, createOrder3), LocalDate.of(2023,4,24))

    assertEquals(result, Left(MultipleOrderErrors(Map(
      createOrder2.id -> OrderErrors(Map(
        FieldId.quantity -> List("must be positive"),
      )),
      createOrder3.id -> OrderErrors(Map(
        FieldId.quantity -> List("must be positive"),
        FieldId.expiry   -> List("must be between 2023-04-24 and 2023-05-24"),
        FieldId.ticker   -> List("cannot be empty"),
      )),
    ))))
  }

}
