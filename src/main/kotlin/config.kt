import org.slf4j.LoggerFactory
import java.io.FileReader

/**
 * Created by sanairika on 2016/06/16.
 */
data class Config(
        val datasource: DataSourceConfig
        , val port: Int
        , val debug: Boolean = false
) {
    lateinit var roles: List<String>
    lateinit var env: String

    companion object {

        val log = LoggerFactory.getLogger(Config::class.java)

        fun init() {
            /* log file config: -Dlogback.configurationFile */
            /* get env and config file */
            val env = System.getProperty("orange.env", "dev")
            val roles = System.getProperty("orange.roles", "web,db,job").split(",")
            val config = readConfigFromFile()
            config.env = env
            config.roles = roles
            log.info("init config: ${config}")
            Orange.config = config
        }

        private fun readConfigFromFile(): Config {
            val configPath = System.getProperty("orange.config")
            if (configPath != null) {
                log.info("read config from ${configPath}")
                return Orange.yaml.readValue(FileReader(configPath), Config::class.java)
            } else {
                log.info("-Dorange.config Not Found!!read config from classpath:/application.yml")
                return Orange.yaml.readValue(Config::class.java.getResourceAsStream("/application.yml"), Config::class.java)
            }
        }
    }
}

data class DataSourceConfig(
        val url: String
        , val username: String
        , val password: String
)

object Static {
    val orange = """
「オレンジ」
作詞・作曲：MICHIRU 編曲：シライシ紗トリ 歌：7!!
テレビアニメ『四月は君の嘘』エンディングテーマ

小さな肩を並べて歩いた
何でもない事で笑い合い 同じ夢を見つめていた
耳を澄ませば 今でも聞こえる
君の声 オレンジ色に染まる街の中

君がいないと本当に退屈だね
寂しいと言えば笑われてしまうけど
残されたもの 何度も確かめるよ
消えることなく輝いている

雨上がりの空のような 心が晴れるような
君の笑顔を憶えている 思い出して笑顔になる
きっと二人はあの日のまま 無邪気な子供のまま
巡る季節を駆け抜けていく それぞれの明日を見て

一人になれば不安になると
眠りたくない夜は 話し続けていた

君はこれから何を見ていくんだろう
私はここで何を見ていくのだろう
沈む夕焼け オレンジに染まる街に
そっと涙を預けてみる

何億もの光の中 生まれた一つの愛
変わらなくても変わってしまっても 君は君だよ 心配無いよ
いつか二人が大人になって 素敵な人に出会って
かけがえのない家族を連れて この場所で逢えるといいな

雨上がりの空のような 心が晴れるような
君の笑顔を憶えている 思い出して笑顔になる
何億もの光の中 生まれた一つの愛
巡る季節を駆け抜けていく それぞれの明日を見て
それぞれの夢を選んで
 """
}