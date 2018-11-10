package com.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.msgpack.jackson.dataformat.MessagePackFactory
import java.nio.file.FileSystems
import java.util.*


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

    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(
            Base64.getEncoder().encodeToString(bytes),
            BarcodeFormat.QR_CODE,
            350, 350) // width x height
    val path = FileSystems.getDefault().getPath("qr.png")
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path)
}

enum class Color(val v: Int) {
    RED(0),
    YELLOW(1),
    BLUE(2),
    GREEN(3)
}
