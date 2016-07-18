package ooeyuna.rika.quartz

import ooeyuna.rika.orange.Orange
import ooeyuna.rika.orange.hasRole
import ooeyuna.rika.orange.plugin
import org.quartz.impl.StdSchedulerFactory

/**
 * Created by sanairika on 2016/07/18.
 */
object Schedule {
  val scheduler = StdSchedulerFactory.getDefaultScheduler();

  fun default_register() {
    if (Orange.hasRole("job")) {
      Orange.plugin("job")
      scheduler.start()
    }
  }
}
