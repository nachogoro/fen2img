package com.nachogoro.fen2img.svgutils

import com.nachogoro.fen2img.Config
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

fun emptyBoardSvg(config: Config, dimensionsPx: Int): Document {
    val squareSize = dimensionsPx / 8

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
            rect.setAttribute("y", "${50.0 * i}")
            rect.setAttribute("width", "$squareSize")
            rect.setAttribute("height", "$squareSize")
            chessBoard.appendChild(rect)
        }
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