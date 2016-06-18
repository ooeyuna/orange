import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

/**
 * Created by sanairika on 2016/06/16.
 */
object Main {

    fun init() {
        /* application config init */
        Config.init()
        /* database init */
        Database.connect(
                driver = "com.mysql.jdbc.Driver"
                , url = Orange.config.datasource.url
                , user = Orange.config.datasource.username
                , password = Orange.config.datasource.password)
        /* flyway init */
        if (Orange.config.roles.contains("db")) {
            val flyway = Flyway()
            flyway.setDataSource(Orange.config.datasource.url, Orange.config.datasource.username, Orange.config.datasource.password);
            flyway.migrate()
        }
        /* quartz init */
        if (Orange.config.roles.contains("job")) {
            Schedule.init()
        }
        if (Orange.config.roles.contains("web")) {
            /* webserver init */
            Server.init()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        init()
    }
}