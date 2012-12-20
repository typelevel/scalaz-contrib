package scalaz.contrib

import scalaz._

package object converter {

  type Converter[F, T, U] = T => Validation[F, U]

  object string extends StringConverters

  object all extends StringConverters

}
