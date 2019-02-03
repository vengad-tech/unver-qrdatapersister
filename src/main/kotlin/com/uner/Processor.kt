package com.uner

import com.uner.interfaces.EncrytorDecryptor
import com.uner.interfaces.InputDataProcessor
import com.uner.interfaces.QRCodeProcessor
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

val A4_SIZE = Pair(2480, 3508) // width and height of A4 paper size

class Processor<T>(
    val setting: Settings,
    val inputDataProcessor: InputDataProcessor<T>,
    val qrCodeProcessor: QRCodeProcessor<T>,
    val encDec: EncrytorDecryptor<T>
) {
    var total_qr_per_age: Int

    init {
        total_qr_per_age =
                (floor((A4_SIZE.first / setting.pixelSize).toDouble()) *
                        floor((A4_SIZE.second / setting.pixelSize).toDouble())).toInt()

    }

    fun encode() {
        val inputData = inputDataProcessor.readData(setting.source)
        val encryptedData = encDec.encrypt(setting.password, inputData)
        val chunkedData = inputDataProcessor.chunkData(encryptedData)

        val totalImages: Int = ceil(chunkedData.size.toDouble() / this.total_qr_per_age).toInt()
        val sizeOfText = chunkedData.size
        println("Processing text: $sizeOfText")
        println("Total QR per page ${this.total_qr_per_age}")
        println("Total images that would be created are $totalImages")

        for (imageNumber in 0 until totalImages) {
            val result = BufferedImage(
                A4_SIZE.first, A4_SIZE.second, //work these out
                BufferedImage.TYPE_BYTE_GRAY
            )
            val g = result.graphics
            // painting white background
            g.color = Color.WHITE
            g.fillRect(0, 0, A4_SIZE.first, A4_SIZE.second)
            var x = 0
            var y = 0
            for (data in chunkedData.subList(
                min(imageNumber * this.total_qr_per_age, chunkedData.size),
                min((imageNumber * this.total_qr_per_age) + this.total_qr_per_age, chunkedData.size)
            )) {
                val stream = qrCodeProcessor.generateQRCode(data)
                val bi = ImageIO.read(stream)
                if (x + bi.width > result.width) {
                    x = 0
                    y += bi.height
                }
                g.drawImage(bi, x, y, null)
                x += setting.pixelSize
            }
            val outputFile = File("${setting.destination}/$imageNumber.png")
            println("Writing to output image file $outputFile")
            ImageIO.write(result, "png", outputFile)
        }


    }

    fun decode() {
        val dir = File(this.setting.source)
        var textValues = ArrayList<T>()
        if (dir.isDirectory) {
            val directoryListing = dir.listFiles()
            if (directoryListing != null) {
                for (child in directoryListing!!) {
                    println("Processing $child")
                    val values = qrCodeProcessor.readQRCode(child)
                    println("Discovered ${values?.size} blocks")
                    if (values != null) {
                        textValues.addAll(values)
                    }
                }
            }
        } else if (dir.isFile) {
            val values = qrCodeProcessor.readQRCode(dir)
            if (values != null) {
                println("Discovered ${values?.size} blocks")
                textValues.addAll(values)
            }
        }
        val encryptedText = inputDataProcessor.unChunkData(textValues)
        println("Encypted message: $encryptedText")
        val decryptedData = encDec.decrypt(setting.password, encryptedText)
        if (setting.writeToStdout) {
            println("Data\n: $decryptedData")
        }
        if (setting.destination.isNotEmpty()) {
            inputDataProcessor.writeData(setting.destination, decryptedData)
        }

    }

}