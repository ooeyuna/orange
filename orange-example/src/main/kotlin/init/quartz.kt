package init

import ooeyuna.rika.orange.Order
import ooeyuna.rika.quartz.Schedule

/**
 * Created by sanairika on 2016/08/05.
 */
object Job {

  @Order(99)
  fun register() {
    Schedule.default_register()
  }
}