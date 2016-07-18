package ooeyuna.rika.exposed

import com.alibaba.druid.pool.DruidDataSourceFactory
import ooeyuna.rika.orange.Orange
import ooeyuna.rika.orange.config
import org.jetbrains.exposed.sql.Database
import java.util.*
import javax.sql.DataSource

/**
 * Created by sanairika on 2016/07/17.
 */
object Exposed {

  lateinit var datasource: DataSource

  fun default_register() {
    val node = Orange.config()["datasource"];
    val p = Properties()
    node.fields().forEach {
      p.put(it.key, it.value.asText())
    }
    val ds = DruidDataSourceFactory.createDataSource(p)
    datasource = ds
    Database.connect(ds)
  }

}