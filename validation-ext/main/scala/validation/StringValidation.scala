package scalaz.contrib
package validation

import scalaz._
import Scalaz._
import java.util.UUID
import java.lang.IllegalArgumentException
import util.matching.Regex

/**
 * User: travis.stevens@gaiam.com
 * Date: 12/9/12
 */
trait StringValidation {

  def matchRegex[F](r: Regex, f: F): Valid[F,String] =
    valid(r.pattern.matcher(_).matches(), f)

  def notBlank[F](f: F): Valid[F, String] =
    valid(_.trim.size > 0, f)

  def uuid[F](f: F): (String) => ValidationNEL[F, UUID] = s => {try {
      UUID.fromString(s).success
    } catch {
      case ex: IllegalArgumentException => f.failureNel
    }
  }


}
