import com.google.common.reflect.ClassPath
import moe.yuna.Orange
import moe.yuna.config
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import kotlin.reflect.jvm.internal.ReflectionFactoryImpl
import kotlin.reflect.memberFunctions

/**
 * Created by sanairika on 2016/06/16.
 */
object Main {

  fun init() {
    /* database init */
    Database.connect(
        driver = "com.mysql.jdbc.Driver"
        , url = Orange.config["datasource"]["url"].asText()
        , user = Orange.config["datasource"]["username"].asText()
        , password = Orange.config["datasource"]["password"].asText())
    val roles = Orange.config["roles"].asText().split(",")
    /* flyway init */
    if (roles.contains("db")) {
      val flyway = Flyway()
      flyway.setDataSource(Orange.config["datasource"]["url"].asText()
          , Orange.config["datasource"]["username"].asText()
          , Orange.config["datasource"]["password"].asText()
      );
      flyway.migrate()
    }
    /* quartz init */
    if (roles.contains("job")) {
      Schedule.init()
    }
    if (roles.contains("web")) {
      /* webserver init */
      Server.init()
    }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    Orange.start()
    /*
    * init
    * controller
    * model
    * kafka
    * */
    val cp = ClassPath.from(Thread.currentThread().contextClassLoader);
    val test = cp.getTopLevelClassesRecursive("controller")
    test.filter { it.name.endsWith("Controller") }
        .map { ReflectionFactoryImpl().getOrCreateKotlinClass(it.load()) }
        .forEach { it ->
          it.memberFunctions.find { f -> f.name.equals("register") }?.call(it.objectInstance)
        }
    println(123)
//    init()
  }
}