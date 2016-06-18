import com.fasterxml.jackson.core.JsonProcessingException
import controller.AcerController
import controller.BogController
import org.slf4j.LoggerFactory
import spark.*

/**
 * Created by sanairika on 2016/06/17.
 */
object Server {
    val log = LoggerFactory.getLogger(Server::class.java)

    fun init() {
        Spark.port(Orange.config.port)
        Spark.get("", { req, res -> "<code>${Static.orange}</code>" })
        AcerController.register()
        BogController.register()
        Spark.exception(Exception::class.java, ErrorHandle())
    }
}


class JSONResponseTransformer : ResponseTransformer {
    override fun render(model: Any?): String? = Orange.json.writeValueAsString(model)
}

object Helper {
    fun getParam(req: Request?, key: String): String = req!!.params("id") ?: throw BadRequest("$key Not Found")

    fun getIntParam(req: Request?, key: String): Int {
        try {
            return getParam(req, key).toInt()
        } catch(ex: NumberFormatException) {
            throw BadRequest("$key Type Error")
        }
    }

    fun getIntParam(get: (String) -> (String?), key: String): Int? {
        try {
            return get.invoke(key)?.toInt()
        } catch(ex: NumberFormatException) {
            throw BadRequest("$key Type Error")
        }
    }

    fun get(path: String, route: (Request, Response) -> (Any)) {
        Spark.get(path, Route { rq, rs -> route.invoke(rq, rs) }, JSONResponseTransformer())
    }
}


class ErrorHandle : ExceptionHandler {
    override fun handle(ex: Exception?, req: Request?, res: Response?) {
        when (ex) {
            is WebError -> {
                res?.status(ex.code)
                res?.body(ex.toResponse())
            }
            is JsonProcessingException -> {
                res?.status(400)
                res?.body("json parser fail")
            }
            else -> {
                res?.status(500)
                Server.log.error("Server Error,url:${req?.url()},header:${req?.headers()},body:${req?.body()}", ex)
                if (Orange.config.debug) {
                    // just print application exception stack
                    res?.body("CauseBy ${ex?.javaClass?.name.orEmpty()}:${ex?.message.orEmpty()}<br/>Application Stack:<br/>${ex?.stackTrace?.filter { it.className.contains("com.dingding") }?.joinToString("<br/>").orEmpty()}")
                } else {
                    res?.body(ServerIntevalError().toResponse())
                }
            }
        }
    }
}

open class WebError(val code: Int, override val message: String) : Exception(message) {
    fun toResponse(): String = Orange.json.writeValueAsString(ErrorResponse(code, message))
}

class ObjectNotFound(message: String) : WebError(404, message)

class BadRequest(message: String) : WebError(400, message)

class Unauthorized(message: String) : WebError(401, message)

class Forbidden(message: String) : WebError(403, message)

class Gone(message: String) : WebError(410, message)

class ServerIntevalError() : WebError(500, "Server Error")

data class ErrorResponse(val code: Int, val message: String)