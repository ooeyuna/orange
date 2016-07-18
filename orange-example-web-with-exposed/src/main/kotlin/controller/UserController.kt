package controller

import model.User
import ooeyuna.rika.orange.web.BadRequest
import ooeyuna.rika.orange.web.WebServer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by sanairika on 2016/07/18.
 */
object UserController {

  fun register() {
    WebServer.get("/add") { req, res ->
      val name = req.queryParams("name") ?: throw BadRequest("Name not found")
      transaction {
        User.insert {
          it[username] = name
        }
      }
      res.status(202)
    }

    WebServer.get("/list") { req, res ->
      transaction {
        User.selectAll().toList()
      }.map { User(it) }
    }
  }
}

data class User(
    val id: Int
    , val name: String
) {
  constructor(rs: ResultRow) : this(
      id = rs[User.id].value
      , name = rs[User.username]
  )
}