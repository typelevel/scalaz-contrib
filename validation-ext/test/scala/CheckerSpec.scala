package scalaz.contrib

import org.specs2.mutable.Specification
import scalaz.contrib.validator.string._
import scalaz.{Failure, NonEmptyList}

class CheckerSpec extends Specification {

  "Checker checkThen" should {
    val s = "MyTest"
    val c = Checker.check(s)

    "execute more validators when it's not failed yet" in {
      c.checkThat(strLength(6, "not 6"))
        .checkThen(strLength(99, "not 99"))
        .checkThen(strLength(100, "not 100")).toValidation mustEqual Failure(NonEmptyList("not 99"))
    }

    "execute no more validators when it's already failed" in {
      c.checkThat(strLength(100, "not 100"))
        .checkThen(strLength(99, "not 99")).toValidation mustEqual Failure(NonEmptyList("not 100"))
    }
  }
}
