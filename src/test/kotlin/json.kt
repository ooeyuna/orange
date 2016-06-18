import lib.AcFunCommon
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Created by sanairika on 2016/06/18.
 */
class JsonTest {

    fun read(path: String) = this.javaClass.getResourceAsStream(path)

    @Test
    fun ac() {
        val json = Orange.json.readValue(read("/ac.json"), AcFunCommon::class.java)
        assertEquals("怂货一枚", json.userjson!!.name)
    }

    @Test
    fun ac_error() {
        val json = Orange.json.readValue(read("/ac_error.json"), AcFunCommon::class.java)
        assertFalse(json.success)
    }
}