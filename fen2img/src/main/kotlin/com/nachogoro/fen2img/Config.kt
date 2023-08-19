package com.nachogoro.fen2img

/**
 * Enum representing the possible players in a chess game.
 */
enum class Player {
    WHITE,
    BLACK
}
/**
 * Configuration data class for customizing the visual representation of a FEN (Forsyth–Edwards Notation) chessboard.
 *
 * @property orientation Defines which player's perspective to use for the board orientation.
 *                       `Player.WHITE` means the white pieces will be at the bottom, and vice versa.
 * @property rankAndFileLabels Whether to show rank and file labels along the edges of the board.
 * @property lightSquareColor Hexadecimal color string for the light squares of the chessboard.
 * @property darkSquareColor Hexadecimal color string for the dark squares of the chessboard.
 * @property svgData A map of chess piece characters to their SVG string representations.
 *                   Used to override default SVG representations with custom ones.
 */
data class Config(
    val orientation: Player = Player.WHITE,
    val rankAndFileLabels: Boolean = true,
    val lightSquareColor: String = "#f0d9b5",
    val darkSquareColor: String = "#b58863",
    val svgData: Map<Char, String> = mapOf())