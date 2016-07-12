package controller

import Helper
import ObjectNotFound
import Orange
import model.Acers
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import spark.Spark.delete
import spark.Spark.post

/**
 * Created by sanairika on 2016/06/17.
 */

object AcerController {

    fun register() {

        Helper.get("/acer", { req, res ->
            val limit = Helper.getIntParam({ req.queryParams(it) }, "limit") ?: 10
            val offset = Helper.getIntParam({ req.queryParams(it) }, "offset") ?: 0
            transaction { Acers.selectAll().limit(limit, offset).map { Acer(it) } }
        })
        Helper.get("/acer/:id", { req, res -> transaction { Acer(Acers.findById(Helper.getIntParam(req, "id")) ?: throw ObjectNotFound("Not Found")) } })
        Helper.get("/acer/:id/realgirlfriend", { req, res -> throw ObjectNotFound("Are You Sure?") })
        post("/acer/:id", { req, res ->
            val acer = Orange.json.readerFor(Acer::class.java).readValue<Acer>(req!!.body())
            transaction {
                Acers.insert {
                    it[name] = acer.name
                    it[girlfriend] = acer.girlfriend
                    it[leader_value] = acer.leader_value
                    it[last_login_date] = acer.last_login_date
                    it[create_time] = DateTime.now()
                    it[update_time] = DateTime.now()
                }
            }
            res.status(201)
        })
        delete("/acer/:id", { req, res -> transaction { Acers.deleteWhere { Acers.id eq EntityID(Helper.getIntParam(req, "id"), Acers) } } })
    }
}

data class Acer(
        val id: Int = 0
        , val name: String
        , val leader_value: Long
        , val girlfriend: Int
        , val create_time: DateTime
        , val update_time: DateTime
        , val last_login_date: DateTime
) {
    constructor(rr: ResultRow) : this(
            id = rr[Acers.id].value
            , name = rr[Acers.name]
            , leader_value = rr[Acers.leader_value]
            , girlfriend = rr[Acers.girlfriend]
            , last_login_date = rr[Acers.last_login_date]
            , create_time = rr[Acers.create_time]
            , update_time = rr[Acers.update_time]

    )
}