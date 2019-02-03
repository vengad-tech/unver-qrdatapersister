package com.uner

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class UnverArgs(parser: ArgParser) {


    val mode by parser.mapping(
        "--encode" to Mode.ENCODE,
        "--decode" to Mode.DECODE,
        help = "mode of operation"
    )

    val source by parser.storing(
        "-s", "--source",
        help = " when encoding it refers to source filename i.e text file which we need to encode as OR images. (currently only plain text files are supported)\n" +
                "When decoding source file refers to image to decode. You can also point to directory that has many images"
    )

    val destination by parser.storing(
        "-d", "--destination",
        help = "when encoding, destination file path refers to folder when images would be stored\n" +
                "When decoding destination file path refers to folder where text file would be written to"
    ).default("")

    val stdout by parser.flagging(
        "-o", "--stdout",
        help = "enable verbose mode"
    )

    val redundancy by parser.storing(
        "-r", "--redundancy",
        help = "Redundancy factor"
    ) { toInt() }.default(3)

    val pixel by parser.storing(
        "-p", "--pixel",
        help = "QR code pixel size"
    ) { toInt() }.default(500)
}

enum class Mode {
    ENCODE,
    DECODE
}