package com.uner.interfaces

import java.io.ByteArrayInputStream
import java.io.File

interface QRCodeProcessor<T> {
    fun generateQRCode(dataToEncode: T): ByteArrayInputStream
    fun readQRCode(qrCodeimage: File): List<T>?

}