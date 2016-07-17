package model

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

/**
 * Created by sanairika on 2016/06/17.
 */
object Bogs : IntIdTable() {
  val name = varchar("name", 128)
  val r_seventeen = bool("r_seventeen").default(true)
  val create_time = datetime("create_time").default(DateTime.now())
  val update_time = datetime("update_time").default(DateTime.now())
}

object Acers : IntIdTable() {
  val name = varchar("name", 128)
  val leader_value = long("leader_value").default(65535)
  val girlfriend = integer("girlfriend").default(2)
  val last_login_date = datetime("last_login_date")
  val create_time = datetime("create_time").default(DateTime.now())
  val update_time = datetime("update_time").default(DateTime.now())

  fun findById(id: Int): ResultRow? = transaction {
    select {
      Acers.id eq EntityID(id, Acers)
    }.limit(1).firstOrNull()
  }
}

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE
    , setterVisibility = JsonAutoDetect.Visibility.NONE
    , fieldVisibility = JsonAutoDetect.Visibility.NONE
    , isGetterVisibility = JsonAutoDetect.Visibility.NONE)
class Bog(id: EntityID<Int>) : IntEntity(id) {
  var name by Bogs.name
  var r_seventeen by Bogs.r_seventeen
  var create_time by Bogs.create_time
  var update_time by Bogs.update_time

  companion object : IntEntityClass<Bog>(Bogs) {

  }
}