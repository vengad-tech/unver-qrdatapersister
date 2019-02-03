package com.uner

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import com.uner.text_processor.AESStringEncDec
import com.uner.text_processor.TextInputDataProcessor
import com.uner.text_processor.TextQRCodeProcessor
import java.io.File
import java.lang.IllegalArgumentException

fun readUserPassword(rentry: Boolean = false, firstAttemptPassword: String = ""): String {
    var prompt: String = if (rentry) "re-enter" else "enter"

    val password = System.console().readPassword("Please $prompt password (at-least 16 chars)").contentToString()
    if (password.length < 16) {
        println("Password must be at-least 16 chars long for better security")
        return readUserPassword()
    }
    if (!rentry) {
        return readUserPassword(rentry = true, firstAttemptPassword = password)
    } else {
        if (!password.equals(firstAttemptPassword)) {
            println("Re-entered password does not match with previous entered password.")
        }
    }
    return password
}

fun validateArgs(args: UnverArgs) {
    val sourceFile = File(args.source)
    when (args.mode) {
        Mode.ENCODE -> {
            if (!sourceFile.isFile) {
                throw IllegalArgumentException("Provided ${args.source} file does not exist.")
            }
        }
        Mode.DECODE -> {
            if (!sourceFile.isFile && !sourceFile.isDirectory) {
                throw IllegalArgumentException("Provided ${args.source} file/directory does not exist.")
            }
        }
    }
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::UnverArgs).run {
        validateArgs(this)
        val settings = Settings(
            destination = destination,
            source = source,
            password = readUserPassword(),
            pixelSize = pixel,
            redundancyFactor = redundancy,
            writeToStdout = stdout
        )
        val textDataInputProcessor = TextInputDataProcessor(settings)
        val textQRCodeProcessor = TextQRCodeProcessor(settings)
        val encDec = AESStringEncDec()
        val textProcessor = Processor(settings, textDataInputProcessor, textQRCodeProcessor, encDec)

        println(source)
        when (mode) {
            Mode.ENCODE -> {
                textProcessor.encode()
            }
            Mode.DECODE -> {
                textProcessor.decode()
            }
        }
    }

}