package moe.yuna.quartz

import moe.yuna.orange.Orange
import moe.yuna.orange.hasRole
import moe.yuna.orange.plugin
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
