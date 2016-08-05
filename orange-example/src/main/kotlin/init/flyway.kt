package init

import ooeyuna.rika.exposed.Exposed
import ooeyuna.rika.orange.Orange
import ooeyuna.rika.orange.Order
import ooeyuna.rika.orange.hasRole
import org.flywaydb.core.Flyway

/**
 * Created by sanairika on 2016/07/17.
 */
object Flyway {

  @Order(0)
  fun register() {
    if (Orange.hasRole("db")) {
      val flyway = Flyway()
      flyway.setDataSource(Exposed.datasource)
      flyway.migrate()
    }
  }
}