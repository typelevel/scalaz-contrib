package scalaz.contrib
import scalaz.ValidationNEL

package object validation {
  type Valid[F, T] = T => ValidationNEL[F, T]
}
