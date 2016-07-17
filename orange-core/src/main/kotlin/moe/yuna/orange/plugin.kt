package moe.yuna.orange

/**
 * Created by sanairika on 2016/07/17.
 */

import com.google.common.reflect.ClassPath
import kotlin.reflect.jvm.internal.ReflectionFactoryImpl
import kotlin.reflect.memberFunctions

/**
 * Created by sanairika on 2016/07/17.
 */
fun Orange.plugin(relativePath: String, registerMethod: String = "register") {
  val cp = ClassPath.from(Thread.currentThread().contextClassLoader);
  var scanPackage = config()["scanPackage"].asText()
  if (scanPackage.isEmpty() || scanPackage.endsWith(".")) {
    scanPackage += relativePath
  } else {
    scanPackage = scanPackage + "." + relativePath
  }
  val classes = cp.getTopLevelClassesRecursive(scanPackage)
  classes
      .map {
        val klass = ReflectionFactoryImpl().getOrCreateKotlinClass(it.load())
        val method = klass.memberFunctions.find { f -> f.name.equals(registerMethod) }
        klass to method
      }
      .filter { it.second != null }
      .sortedBy {
        val an = it.second!!.annotations.find { a -> a is Order }
        if (an == null) {
          return@sortedBy 0
        } else {
          return@sortedBy (an as Order).value
        }
      }
      .forEach {
        it.second!!.call(it.first.objectInstance)
        log.debug("Register ${it.first.simpleName}")
      }
  log.info("Orange Application Plugin Register.relativePath:$relativePath,registerMethod:$registerMethod")
}

fun Orange.hasRole(role: String) = role in role.split(",")

annotation class Order(
    val value: Int = 0
)