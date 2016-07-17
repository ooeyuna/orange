package model

import moe.yuna.exposed.timestamp
import org.jetbrains.exposed.dao.IntIdTable

/**
 * Created by sanairika on 2016/07/18.
 */
object User : IntIdTable() {
  val username = varchar("username", 255)
  val t = timestamp()
}