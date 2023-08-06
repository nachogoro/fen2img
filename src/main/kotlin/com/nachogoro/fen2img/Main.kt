package com.nachogoro.fen2img

import java.io.File

fun readContents(fileName: String): String = File(fileName).readText()

fun main() {
    val pieces = mapOf<Char, String>('K' to readContents("/home/ic/ugly_king.svg"))
    val img = Fen2Img(Config(orientation=Player.BLACK))
    val svgData = img.Fen2Svg("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 \n")
    File("/home/ic/chessPieces/board.svg").writeText(svgData)
}