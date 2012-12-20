package scalaz.contrib

import scalaz._

package object converter {

  type Converter[E, T, U] = T => Validation[E, U]

  object string extends StringConverters

  object all extends StringConverters

}
