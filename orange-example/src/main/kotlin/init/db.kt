package init

import ooeyuna.rika.exposed.Exposed
import ooeyuna.rika.orange.Order

/**
 * Created by sanairika on 2016/07/17.
 */
object Database {

  @Order(-1)
  fun register() {
    Exposed.default_register()
  }
}