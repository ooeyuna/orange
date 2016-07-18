import model.User
import ooeyuna.rika.orange.Orange
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by sanairika on 2016/07/18.
 */
object Main {

  @JvmStatic
  fun main(args: Array<String>) {
    Orange.start()
    transaction {
      SchemaUtils.create(User)
    }
  }
}