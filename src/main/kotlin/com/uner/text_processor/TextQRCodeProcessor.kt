package com.uner.text_processor

import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.NotFoundException
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import com.uner.Settings
import com.uner.interfaces.QRCodeProcessor
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO


class TextQRCodeProcessor(private val settings: Settings) : QRCodeProcessor<String> {
    override fun readQRCode(qrCodeimage: File): List<String>? {
        val bufferedImage = ImageIO.read(qrCodeimage)
        if (qrCodeimage.absolutePath.endsWith("jpg") || qrCodeimage.absolutePath.endsWith("png")) {


            val source = BufferedImageLuminanceSource(bufferedImage)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            var resultString = ArrayList<String>()
            val hints = Hashtable<DecodeHintType, Any>()
            hints[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE
            hints[DecodeHintType.PURE_BARCODE] = java.lang.Boolean.FALSE
            return try {
                for (result in QRCodeMultiReader().decodeMultiple(bitmap, hints)) {
                    resultString.add(result.text)
                }
                resultString
            } catch (e: NotFoundException) {
                println("There is no QR code in the image")
                null
            }
        } else {
            println("Unknown file: $qrCodeimage")
            return null
        }
    }

    override fun generateQRCode(dataToEncode: String): ByteArrayInputStream {
        val r = QRCode.from(dataToEncode).withSize(this.settings.pixelSize, this.settings.pixelSize).to(ImageType.PNG)
            .stream()
        return ByteArrayInputStream(r.toByteArray())
    }

}