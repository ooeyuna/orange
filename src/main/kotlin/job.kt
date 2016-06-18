import lib.AcFunApi
import lib.BilibiliApi
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory

/**
 * Created by sanairika on 2016/06/16.
 */

object Schedule {
    val scheduler = StdSchedulerFactory.getDefaultScheduler();
    fun init() {
        val acjob = JobBuilder.newJob(AcFunJob::class.java).build()
        val bjob = JobBuilder.newJob(BilibiliJob::class.java).build()
        val actrigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever().withIntervalInHours(1))
                .build()
        val btrigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever().withIntervalInHours(1))
                .build()
        scheduler.scheduleJob(acjob, actrigger)
        scheduler.scheduleJob(bjob, btrigger)
        scheduler.start();
    }
}

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