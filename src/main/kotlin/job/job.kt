package job

import lib.AcFunApi
import lib.BilibiliApi
import org.quartz.Job
import org.quartz.JobExecutionContext

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