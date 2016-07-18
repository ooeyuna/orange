package init

import ooeyuna.rika.orange.web.WebServer


/**
 * Created by sanairika on 2016/07/17.
 */
object Web {

  fun register() {
    WebServer.default_register()
    WebServer.get("/") { req, res ->
      mapOf("hello" to "world")
    }
  }
}