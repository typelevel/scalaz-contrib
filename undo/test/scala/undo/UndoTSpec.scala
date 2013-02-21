package scalaz.contrib
package undo

import scalaz.syntax.monad._
import scalaz.std.option._

import org.specs2.mutable.Specification

object UndoTSpec extends Specification {
  // TODO: Omitting the type parameters on hput leads to a compiler infinite loop
  // if UndoT.undoTMonadState is imported.

  import UndoT._

  val result: UndoT[Option, Int, _] =
    for {
      one           <- hput[Option, Int](1)
      two           <- hput[Option, Int](2)
      three         <- hput[Option, Int](3)
      twoAgain      <- undo[Option, Int]
      four          <- hput[Option, Int](4)
      twoAgainAgain <- undo[Option, Int]
      fourAgain     <- redo[Option, Int]
    } yield ()

  "undoT" should {
    "give the correct result" in {
      result.exec(1) must beEqualTo(Some(4))
    }
  }

}
