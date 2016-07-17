package controller

import moe.yuna.orange.web.WebServer

/**
 * Created by sanairika on 2016/07/17.
 */
object TestController {

  fun register() {
    WebServer.get("/test") { req, res -> mapOf("123" to "1222") }
  }
}