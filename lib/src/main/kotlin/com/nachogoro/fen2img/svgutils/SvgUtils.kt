package com.nachogoro.fen2img.svgutils

import com.nachogoro.fen2img.Config
import com.nachogoro.fen2img.Player
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Generates an SVG representation of an empty chessboard.
 *
 * @param config Configuration parameters for the board such as colors and orientation.
 * @param dimensionsPx The size of the board in pixels.
 * @return The SVG document representing the chessboard.
 *
 * The board will be divided into 8x8 squares. Each square's size is computed based on the provided dimension.
 * Additionally, labels for ranks (numbers) and files (letters) are added to the board.
 */
fun emptyBoardSvg(config: Config, dimensionsPx: Int): Document {
    val squareSize = dimensionsPx / 8
    val labelOffset = squareSize * 0.05

    val ranks = arrayOf("8", "7", "6", "5", "4", "3", "2", "1")
    val files = arrayOf("a", "b", "c", "d", "e", "f", "g", "h")

    // Create SVG Document for chessboard
    val dbFactory = DocumentBuilderFactory.newInstance()
    val docBuilder = dbFactory.newDocumentBuilder()
    val chessBoardSVG = docBuilder.newDocument()

    val chessBoard = chessBoardSVG.createElement("svg") as Element
    chessBoardSVG.appendChild(chessBoard)
    chessBoard.setAttribute("xmlns", "http://www.w3.org/2000/svg")
    chessBoard.setAttribute("width", "$dimensionsPx")
    chessBoard.setAttribute("height", "$dimensionsPx")

    // Create the chessboard squares
    for (i in 0 until 8) {
        for (j in 0 until 8) {
            val rect = chessBoardSVG.createElement("rect") as Element
            rect.setAttribute("fill", if ((i + j) % 2 == 0) config.lightSquareColor else config.darkSquareColor)
            rect.setAttribute("x", "${squareSize * j}")
            rect.setAttribute("y", "${squareSize * i}")
            rect.setAttribute("width", "$squareSize")
            rect.setAttribute("height", "$squareSize")
            chessBoard.appendChild(rect)
        }

        // Add rank labels to the left-most cell
        val rankText = chessBoardSVG.createElement("text") as Element
        rankText.setAttribute("x", "$labelOffset")
        rankText.setAttribute("y", "${squareSize * (i+0.25)}")
        rankText.setAttribute("fill", if (i % 2 != 0) config.lightSquareColor else config.darkSquareColor)
        rankText.textContent = if (config.orientation == Player.WHITE) ranks[i] else ranks[7-i]
        chessBoard.appendChild(rankText)
    }

    // Add file labels to the bottom-most cell
    for (j in 0 until 8) {
        val fileText = chessBoardSVG.createElement("text") as Element
        fileText.setAttribute("x", "${squareSize * (j + 0.85)}")  // Adjust 0.85 for horizontal positioning
        fileText.setAttribute("y", "${dimensionsPx - labelOffset}")
        fileText.setAttribute("fill", if (j % 2 == 0) config.lightSquareColor else config.darkSquareColor)
        fileText.textContent = if (config.orientation == Player.WHITE) files[j] else files[7-j]
        chessBoard.appendChild(fileText)
    }

    return chessBoardSVG
}

/**
 * Adds a chess piece to the SVG representation of a chessboard.
 *
 * @param boardSVG The SVG document of the chessboard to which the piece should be added.
 * @param pieceSVG The SVG representation of the chess piece to be added.
 * @param x The horizontal position (from the left) on the board where the piece should be placed.
 * @param y The vertical position (from the top) on the board where the piece should be placed.
 * @param squareSize The size of each square on the board in pixels.
 * @param pieceSizeFraction The fraction of the square size that the piece should occupy.
 *
 * The function computes the appropriate scale for the chess piece based on the provided square size and
 * the intrinsic size of the piece. The piece is then translated to the specified position and scaled accordingly.
 */
fun addPieceToBoard(
    boardSVG: Document,
    pieceSVG: Document,
    x: Double,
    y: Double,
    squareSize: Double,
    pieceSizeFraction: Double)
{
    // Ignore units in height attribute
    val pieceSize: Double = pieceSVG.documentElement.getAttribute("height")
        .replace(Regex("[^\\d.]"), "").toDouble()
    val pieceScale = squareSize / pieceSize * pieceSizeFraction

    // Wrap the piece SVG in a <g> element with a transform attribute to adjust its position and size
    val pieceWrapper = boardSVG.createElement("g") as Element
    pieceWrapper.setAttribute(
        "transform",
        "translate($x, $y) scale($pieceScale)"
    )
    boardSVG.firstChild.appendChild(pieceWrapper)

    // Import the piece SVG into the chessboard SVG
    val piece = boardSVG.importNode(pieceSVG.documentElement, true)
    pieceWrapper.appendChild(piece)
}