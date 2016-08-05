package controller

import model.Bog
import ooeyuna.rika.orange.Orange
import ooeyuna.rika.orange.jackson
import ooeyuna.rika.orange.web.ObjectNotFound
import ooeyuna.rika.orange.web.WebServer
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import spark.Request
import spark.Response
import spark.Route
import spark.Spark.*
import java.time.Instant

/**
 * Created by sanairika on 2016/06/17.
 */

object BogController {

  fun register() {
    WebServer.get("/bog", { req, res ->
      val limit = req.queryParams("limit")?.toInt() ?: 10
      val offset = req.queryParams("offset")?.toInt() ?: 0
      transaction {
        Bog.all().limit(limit, offset).toList().map { BogEntity(it) }
      }
    })
    WebServer.get("/bog/:id", { req, res -> transaction { BogEntity(Bog.findById(WebServer.getIntParam(req, "id")) ?: throw ObjectNotFound("")) } })
    put("/bog/:id", BogPutRoute())
    post("/bog/:id", { req, res ->
      val be = Orange.jackson().readerFor(BogEntity::class.java).readValue<BogEntity>(req!!.body())
      transaction {
        Bog.Companion.new {
          name = be.name
          r_seventeen = be.r_seventeen
          update_time = DateTime.now()
          create_time = DateTime.now()
        }
      }
    })
    delete("/bog/:id", { req, res -> transaction { Bog.get(WebServer.getIntParam(req, "id")).delete() } })
  }
}

class BogPutRoute : Route {
  override fun handle(req: Request?, res: Response?): Any? {
    val be = Orange.jackson().readerFor(BogEntity::class.java).readValue<BogEntity>(req!!.body())
    return transaction {
      Bog.Companion.get(WebServer.getIntParam(req, "id")).apply {
        name = be.name
        r_seventeen = be.r_seventeen
        update_time = DateTime.now()
        flush()
      }
    }
  }
}

data class BogEntity(
    val id: Int
    , val name: String
    , val r_seventeen: Boolean
    , val update_time: Instant
    , val create_time: Instant
) {
  constructor(bog: Bog) : this(
      id = bog.id.value
      , name = bog.name
      , r_seventeen = bog.r_seventeen
      , update_time = Instant.ofEpochMilli(bog.update_time.millis)
      , create_time = Instant.ofEpochMilli(bog.create_time.millis)
  )
}