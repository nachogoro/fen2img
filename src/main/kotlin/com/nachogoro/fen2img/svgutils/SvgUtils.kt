package com.nachogoro.fen2img.svgutils

import com.nachogoro.fen2img.Config
import com.nachogoro.fen2img.Player
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

fun emptyBoardSvg(config: Config, dimensionsPx: Int): Document {
    val squareSize = dimensionsPx / 8
    val labelOffset = squareSize * 0.05  // Adjust as needed for label positioning

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

fun addPieceToBoard(
    boardSVG: Document,
    pieceSVG: Document,
    x: Double,
    y: Double,
    squareSize: Double,
    pieceSizeFraction: Double)
{
    val pieceSize: Double = pieceSVG.documentElement.getAttribute("height").toDouble()
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