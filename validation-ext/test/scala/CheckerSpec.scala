package scalaz.contrib

import org.specs2.mutable.Specification
import scalaz.contrib.validator.string._
import scalaz.NonEmptyList

class CheckerSpec extends Specification {

  "Checker checkThen" should {
    val s = "MyTest"
    val c = Checker.check(s)

    "execute more validators when it's not failed yet" in {
      c.checkThat(strLength(6, "not 6"))
        .checkThen(strLength(99, "not 99")) mustEqual YesChecker(s, Vector("not 99"))
    }

    "not execute any more validators when it's already failed" in {
      c.checkThat(strLength(100, "not 100"))
        .checkThen(strLength(99, "not 99")) mustEqual NoChecker(NonEmptyList("not 100"))
    }
  }
}
