package init

import moe.yuna.Orange
import moe.yuna.Order
import moe.yuna.config
import moe.yuna.hasRole
import org.flywaydb.core.Flyway

/**
 * Created by sanairika on 2016/07/17.
 */
object Flyway {

  @Order(-1)
  fun register() {
    if (Orange.hasRole("db")) {
      val flyway = Flyway()
      flyway.setDataSource(Orange.config()["datasource"]["url"].asText()
          , Orange.config()["datasource"]["username"].asText()
          , Orange.config()["datasource"]["password"].asText()
      );
      flyway.migrate()
    }
  }
}