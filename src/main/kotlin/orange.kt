import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import org.slf4j.LoggerFactory

/**
 * Created by sanairika on 2016/06/16.
 */

object Orange {

    val log = LoggerFactory.getLogger("com.dingding.makoto.utils")
    val yaml = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
    val json = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .registerModule(JodaModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    lateinit var config: Config

    fun <T : Any> http_process(req: Request, res: Response, r: Result<T, FuelError>): T? {
        val (data, error) = r
        when (res.httpStatusCode) {
            200 -> {
                return data
            }
            else -> {
                log.error("Read ${req.path} Error!!(status:${res.httpStatusCode},message:${res.httpResponseMessage})", error)
                val ex = error ?: FuelError().apply { exception = RuntimeException("request error") }
                throw ex
            }
        }
    }
}

object Kaori{

}