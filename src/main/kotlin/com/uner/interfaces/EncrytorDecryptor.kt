package com.uner.interfaces

interface EncrytorDecryptor<T> {
    fun encrypt(password: String, payload: T): T
    fun decrypt (password: String, payload: T): T
}