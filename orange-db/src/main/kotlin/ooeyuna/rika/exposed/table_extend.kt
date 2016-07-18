package ooeyuna.rika.exposed

import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

/**
 * Created by sanairika on 2016/07/18.
 */
fun Table.timestamp() {
  val createTime = datetime("createTime").default(DateTime.now())
  val updateTime = datetime("updateTime").default(DateTime.now())
}