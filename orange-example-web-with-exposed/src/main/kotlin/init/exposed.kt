package init

import ooeyuna.rika.exposed.Exposed

/**
 * Created by sanairika on 2016/07/18.
 */
object Database {

  fun register() {
    Exposed.default_register()
  }
}