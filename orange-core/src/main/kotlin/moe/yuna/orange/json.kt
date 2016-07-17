package moe.yuna.orange

/**
 * Created by sanairika on 2016/07/17.
 */

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Created by sanairika on 2016/07/17.
 */
fun Orange.jackson() = ObjectMapper()
    .registerModule(KotlinModule())
    .registerModule(JavaTimeModule())
    .registerModule(JodaModule())
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)

fun Orange.yaml() = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
