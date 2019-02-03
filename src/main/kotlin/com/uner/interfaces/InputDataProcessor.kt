package com.uner.interfaces

interface InputDataProcessor<T> {
    fun  readData(dataLocation: String): T
    fun  writeData(dataLocation: String, data: T)
    fun  chunkData(dataToChunk: T): List<T>
    fun  unChunkData(dataToUnChunk: List<T>): T
}