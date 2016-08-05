package init

/**
 * Created by sanairika on 2016/07/17.
 */
import ooeyuna.rika.orange.*
import ooeyuna.rika.orange.web.ErrorHandle
import spark.Spark

/**
 * Created by sanairika on 2016/06/17.
 */
object Web {

  @Order(99)
  fun register() {
    if (Orange.hasRole("web")) {
      Spark.port(Orange.config()["port"]?.asInt() ?: 8080)
      Orange.plugin("controller")
      Spark.get("", { req, res -> "( ゜- ゜)つロ 乾杯~ - bilibili" })
      Spark.exception(Exception::class.java, ErrorHandle())
    }
  }
}