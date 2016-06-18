package lib

import Orange
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.kittinunf.fuel.httpGet
import model.Acers
import model.Bogs
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by sanairika on 2016/06/18.
 */
object AcFunApi {

    val log = LoggerFactory.getLogger(AcFunApi::class.java)

    fun fetchUser() {
        val seed = ThreadLocalRandom.current().nextInt(0, 489) * 10000
        seed.rangeTo(seed + 10000).forEach { uid ->
            "http://acfun.tudou.com/usercard.aspx".httpGet(listOf(Pair("uid", uid))).responseString { req, res, r ->
                val (data, error) = r
                if (res.httpStatusCode != 200 || error != null) {
                    log.warn("laji ac uid:$uid")
                } else {
                    val root = Orange.json.readValue(data, AcFunCommon::class.java)
                    if (root.success) {
                        transaction {
                            val user = root.userjson!!
                            Acers.insertIgnore {
                                it[id] = EntityID(user.uid, Acers)
                                it[name] = user.name
                                it[last_login_date] = DateTime(user.lastLoginDate.toEpochMilli())
                                it[create_time] = DateTime.now()
                                it[update_time] = DateTime.now()
                            }
                        }
                    } else {
                        log.info("user not exist or disabled uid:$uid")
                    }
                }
            }
            Thread.sleep(ThreadLocalRandom.current().nextLong(200L, 300L))
        }
    }

}

data class AcFunUser(
        val uid: Int
        , val name: String
        , @JsonDeserialize(using = LajiacDateDeserializer::class) var lastLoginDate: Instant = Instant.now()
) {


    class LajiacDateDeserializer : JsonDeserializer<Instant>() {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant =
                LocalDateTime.parse(p!!.valueAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n"))
                        .toInstant(ZoneOffset.of("+8"))
    }
}

data class AcFunCommon(
        val success: Boolean
        , val userjson: AcFunUser?
)

object BilibiliApi {

    val log = LoggerFactory.getLogger(BilibiliApi::class.java)

    fun fetchUser() {
        val seed = ThreadLocalRandom.current().nextInt(0, 3279) * 10000
        seed.rangeTo(seed + 10000).forEach { uid ->
            "http://api.bilibili.cn/userinfo".httpGet(listOf("mid" to uid)).responseString { req, res, r ->
                val (data, error) = r
                if (res.httpStatusCode != 200 || error != null) {
                    log.warn("doushi shijie de cuo uid:$uid")
                } else {
                    val root = Orange.json.readValue(data, BilibiliUser::class.java)
                    if (root.code == 0) {
                        transaction {
                            Bogs.insertIgnore {
                                it[id] = EntityID(uid, Bogs)
                                it[name] = root.name
                                it[create_time] = DateTime.now()
                                it[update_time] = DateTime.now()
                            }
                        }
                    } else {
                        log.info("user not exist or disabled uid:$uid")
                    }
                }
            }
            Thread.sleep(ThreadLocalRandom.current().nextLong(200L, 300L))
        }
    }
}

data class BilibiliUser(
        val code: Int
        , val mid: Int?
        , val name: String?
)