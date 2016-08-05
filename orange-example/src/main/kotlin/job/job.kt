package job

import lib.AcFunApi
import lib.BilibiliApi
import ooeyuna.rika.quartz.Schedule
import org.quartz.*

/**
 * Created by sanairika on 2016/06/18.
 */

class AcFunJob : Job {
  override fun execute(context: JobExecutionContext?) {
    AcFunApi.fetchUser()
  }
}

class BilibiliJob : Job {
  override fun execute(context: JobExecutionContext?) {
    BilibiliApi.fetchUser()
  }
}

object BilibiliJobRegister {
  fun register() {
    val bjob = JobBuilder.newJob(BilibiliJob::class.java).build()
    val btrigger = TriggerBuilder.newTrigger()
        .withSchedule(SimpleScheduleBuilder.repeatHourlyForever().withIntervalInHours(1))
        .build()
    Schedule.scheduler.scheduleJob(bjob, btrigger)
  }
}
