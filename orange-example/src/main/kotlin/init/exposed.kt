package init

import moe.yuna.Orange
import moe.yuna.Order
import moe.yuna.config
import org.jetbrains.exposed.sql.Database

/**
 * Created by sanairika on 2016/07/17.
 */
object Exposed {

  @Order(0)
  fun register() {
    Database.connect(
        driver = "com.mysql.jdbc.Driver"
        , url = Orange.config()["datasource"]["url"].asText()
        , user = Orange.config()["datasource"]["username"].asText()
        , password = Orange.config()["datasource"]["password"].asText())
  }
}