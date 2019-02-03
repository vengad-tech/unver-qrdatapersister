package com.uner.text_processor

import AES
import com.uner.interfaces.EncrytorDecryptor

class AESStringEncDec : EncrytorDecryptor<String> {
    override fun encrypt(password: String, payload: String): String = AES.encrypt(payload, password)


    override fun decrypt(password: String, payload: String): String = AES.decrypt(payload, password)
}