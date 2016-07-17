package moe.yuna.orange

/**
 * Created by sanairika on 2016/07/17.
 */

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.File
import java.io.FileReader

/**
 * Created by sanairika on 2016/07/17.
 */
fun Orange.config(): JsonNode {
  return OrangeConfig.config
}

object OrangeConfig {

  val configPossiblePaths = arrayOf(
      System.getProperty("config")
      , "application.yml"
      , "config/application.yml")

  val classpathPossiblePaths = arrayOf(
      "application.yml"
      , "config/application.yml")

  val defaultConfigValues = mapOf(
      "env" to "dev"
      , "roles" to "web,db,job"
      , "scanPackage" to ""
  )

  val config = load_config()

  private fun load_config(): JsonNode {
    /* log file moe.yuna.getConfig: -Dlogback.configurationFile */
    /* get env and moe.yuna.getConfig file */
    val file_config = readConfigFromFile() ?: readConfigFromClasspathFile() ?: throw RuntimeException("Orange Application Config Not Found!!Read From ${configPossiblePaths}")

    val config = file_config as ObjectNode

    System.getProperties()
        .filter { it.key.toString().startsWith("orange.", true) }
        .forEach {
          config.put(it.key.toString().substringAfter("orange."), it.value.toString())
        }

    defaultConfigValues.forEach {
      if (!config.has(it.key)) {
        config.put(it.key, it.value)
      }
    }
    Orange.log.info("Orange Application Config Init: ${config.toString()}")

    return config
  }

  private fun readConfigFromFile(): JsonNode? {
    val path = configPossiblePaths.find { it != null && File(it).exists() }
    return if (path == null) null else {
      Orange.log.debug("read config from ${path}")
      Orange.yaml().readTree(FileReader(path))
    }
  }

  private fun readConfigFromClasspathFile(): JsonNode? {
    val klassLoader = this.javaClass.classLoader
    val path = classpathPossiblePaths.find { klassLoader.getResourceAsStream(it) != null }
    return if (path == null) null else {
      Orange.log.debug("read config from classpath:${path}")
      Orange.yaml().readTree(klassLoader.getResourceAsStream(path))
    }
  }

}