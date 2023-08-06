package com.nachogoro.fen2img

enum class Player {
    WHITE,
    BLACK
}

data class Config(
    val orientation: Player = Player.WHITE,
    val lightSquareColor: String = "#f0d9b5",
    val darkSquareColor: String = "#b58863",
    val svgData: Map<Char, String> = mapOf())