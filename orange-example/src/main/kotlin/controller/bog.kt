package controller

import Helper
import moe.yuna.Orange
import model.Bog
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
        Helper.get("/bog", { req, res ->
            val limit = Helper.getIntParam({ req.queryParams(it) }, "limit") ?: 10
            val offset = Helper.getIntParam({ req.queryParams(it) }, "offset") ?: 0
            transaction {
                Bog.Companion.all().limit(limit, offset).toList().map { BogEntity(it) }
            }
        })
        Helper.get("/bog/:id", { req, res -> transaction { BogEntity(Bog.Companion.get(Helper.getIntParam(req, "id"))) } })
        put("/bog/:id", BogPutRoute())
        post("/bog/:id", { req, res ->
            val be = Orange.json.readerFor(BogEntity::class.java).readValue<BogEntity>(req!!.body())
            transaction {
                Bog.Companion.new {
                    name = be.name
                    r_seventeen = be.r_seventeen
                    update_time = DateTime.now()
                    create_time = DateTime.now()
                }
            }
        })
        delete("/bog/:id", { req, res -> transaction { Bog.Companion.get(Helper.getIntParam(req, "id")).delete() } })
    }
}

class BogPutRoute : Route {
    override fun handle(req: Request?, res: Response?): Any? {
        val be = Orange.json.readerFor(BogEntity::class.java).readValue<BogEntity>(req!!.body())
        return transaction {
            Bog.Companion.get(Helper.getIntParam(req, "id")).apply {
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