package ooeyuna.rika.orange.web

import com.fasterxml.jackson.core.JsonProcessingException
import ooeyuna.rika.orange.*
import org.slf4j.LoggerFactory
import spark.*

/**
 * Created by sanairika on 2016/07/17.
 */
object WebServer {
  val log = LoggerFactory.getLogger(WebServer::class.java)

  fun default_register() {
    if (Orange.hasRole("web")) {
      Spark.port(Orange.config()["port"]?.asInt() ?: 8080)
      Orange.plugin("controller")
      Spark.exception(Exception::class.java, ErrorHandle())
    }
  }

  fun getIntParam(req: Request, key: String): Int = processBadRequest(key) { req.params(key).toInt() }
  fun getLongParam(req: Request, key: String): Long = processBadRequest(key) { req.params(key).toLong() }
  fun getDoubleParam(req: Request, key: String): Double = processBadRequest(key) { req.params(key).toDouble() }
  fun getFloatParam(req: Request, key: String): Float = processBadRequest(key) { req.params(key).toFloat() }

  fun getIntQueryParam(req: Request, key: String): Int = processBadRequest(key) { req.queryParams(key).toInt() }
  fun getLongQueryParam(req: Request, key: String): Long = processBadRequest(key) { req.queryParams(key).toLong() }
  fun getDoubleQueryParam(req: Request, key: String): Double = processBadRequest(key) { req.queryParams(key).toDouble() }
  fun getFloatQueryParam(req: Request, key: String): Float = processBadRequest(key) { req.queryParams(key).toFloat() }

  fun <T> processBadRequest(key: String, get: (String) -> T): T = try {
    get.invoke(key)
  } catch (ex: Exception) {
    throw BadRequest("$key Type Error")
  }

  fun get(path: String, route: (Request, Response) -> (Any)) = Spark.get(path, Route { rq, rs -> route.invoke(rq, rs) }, JSONResponseTransformer())
  fun post(path: String, route: (Request, Response) -> (Any)) = Spark.post(path, Route { rq, rs -> route.invoke(rq, rs) }, JSONResponseTransformer())
  fun put(path: String, route: (Request, Response) -> (Any)) = Spark.put(path, Route { rq, rs -> route.invoke(rq, rs) }, JSONResponseTransformer())
  fun delete(path: String, route: (Request, Response) -> (Any)) = Spark.delete(path, Route { rq, rs -> route.invoke(rq, rs) }, JSONResponseTransformer())
}

class JSONResponseTransformer : ResponseTransformer {
  override fun render(model: Any?): String? = Orange.jackson().writeValueAsString(model)
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
        WebServer.log.error("Server Error,url:${req?.url()},header:${req?.headers()},body:${req?.body()}", ex)
        if (Orange.config()["debug"].asBoolean()) {
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
  fun toResponse(): String = Orange.jackson().writeValueAsString(ErrorResponse(code, message))
}

class ObjectNotFound(message: String) : WebError(404, message)

class BadRequest(message: String) : WebError(400, message)

class Unauthorized(message: String) : WebError(401, message)

class Forbidden(message: String) : WebError(403, message)

class Gone(message: String) : WebError(410, message)

class ServerIntevalError() : WebError(500, "Server Error")

data class ErrorResponse(val code: Int, val message: String)