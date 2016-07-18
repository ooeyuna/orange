package ooeyuna.rika.orange

import org.slf4j.LoggerFactory

/**
 * Created by sanairika on 2016/07/17.
 */
object Orange {

  val log = LoggerFactory.getLogger(this.javaClass)

  fun start() {
    config()
    plugin("init", "register")
  }

}
