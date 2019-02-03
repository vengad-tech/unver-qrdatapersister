package com.uner.text_processor

import com.uner.Settings
import com.uner.interfaces.InputDataProcessor
import java.io.File
import java.util.ArrayList

const val BLOCK_HEADER_SIZE = 3
const val TEXT_SPLIT_SIZE = 500

class TextInputDataProcessor(private val settings: Settings) : InputDataProcessor<String> {

    override fun readData(dataLocation: String): String {
        return File(dataLocation).readText()
    }

    override fun writeData(dataLocation: String, data: String) {
        File(dataLocation).writeText(data)
    }

    override fun chunkData(dataToChunk: String): List<String> {
        val strings = ArrayList<String>()
        var index = 0
        var blockId = 0
        while (index < dataToChunk.length) {
            var block = dataToChunk.substring(index, Math.min(index + TEXT_SPLIT_SIZE, dataToChunk.length))
            block = """${blockId.toString().padEnd(3)}$block"""
            println("Added block $block")
            for (i in 1..this.settings.redundancyFactor) {
                strings.add(block)
            }
            index += TEXT_SPLIT_SIZE
            blockId++
        }
        strings.shuffle()
        println("Total data size ${dataToChunk.length}")
        return strings
    }

    override fun unChunkData(dataToUnChunk: List<String>): String =
        dataToUnChunk.distinctBy { it.substring(0, BLOCK_HEADER_SIZE).trimEnd().toInt() }
            .sortedBy { it.substring(0,
                BLOCK_HEADER_SIZE
            ).trimEnd().toInt() }.map { it.substring(BLOCK_HEADER_SIZE) }
            .toList().joinToString(separator = "")

}