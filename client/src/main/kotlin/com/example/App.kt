package com.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import org.msgpack.jackson.dataformat.MessagePackFactory


fun main(args: Array<String>)  {
    val objectMapper = ObjectMapper(MessagePackFactory())
    val map = mapOf(
            "number" to 10,
            "flag" to true,
            "color" to Color.YELLOW.v,
            "description" to "hogemogehogemoge")

    val bytes = objectMapper.writeValueAsBytes(map)

    println("Sending ${bytes.size} bytes. Serialized object is $map")
    println("----------------------------------------")

    val url = "http://localhost:5000/api/messagepack"
    val (_, _, result) = Fuel.post(url)
            .body(bytes)
            .responseString()
    result.fold(success = ::println) { throw it }
}

enum class Color(val v: Int) {
    RED(0),
    YELLOW(1),
    BLUE(2),
    GREEN(3)
}
