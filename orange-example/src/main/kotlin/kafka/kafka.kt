package kafka

import moe.yuna.Orange
import Schedule
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by sanairika on 2016/06/19.
 */
object KafkaTest {

  fun init() {
    thread {
      val prop = Properties().apply {
        put("bootstrap.servers", Orange.config["kafka"]["servers"])
        put("group.id", Orange.config["kafka"]["groupid"])
        put("enable.auto.commit", "true")
        put("auto.commit.interval.ms", "1000")
        put("session.timeout.ms", "30000")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("max.poll.records", Orange.config["kafka"]["maxpoll"])
      }
      val consumer = KafkaConsumer<String, String>(prop).apply {
        subscribe(listOf("test1"))
      }
      while (true) {
        consumer.poll(0).forEach {
          println(it.value())
        }
      }
    }
  }

  fun main(args: Array<String>) {
    KafkaTest.init()
    Schedule.scheduler.start()
    val props = Properties()
    props.put("bootstrap.servers", "meiling:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    val producer = KafkaProducer<String, String>(props);
    (1..1000).forEach {
      producer.send(ProducerRecord<String, String>("test1", Integer.toString(it), Integer.toString(it)));
    }

    producer.close();
  }
}