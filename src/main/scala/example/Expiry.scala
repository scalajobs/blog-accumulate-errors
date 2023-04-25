package example

import java.time.LocalDate

enum Expiry {
  case Never
  case ValidUntil(date: LocalDate)
}
