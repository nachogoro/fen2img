package com.nachogoro.fen2img

import com.nachogoro.fen2img.svgutils.addPieceToBoard
import com.nachogoro.fen2img.svgutils.emptyBoardSvg
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.StringWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Converts a FEN (Forsyth–Edwards Notation) string representation of a chessboard to visual representations
 * in SVG (Scalable Vector Graphics) and PNG (Portable Network Graphics) formats.
 *
 * This class takes into account configuration options like custom piece SVGs and board orientation.
 *
 * @property config The configuration parameters used for the FEN to image conversion.
 */
class Fen2Img(config: Config = Config()) {
    private val _config: Config = config
    private val _svgProvider = PieceSvgDataProvider(_config.svgData)

    /**
     * Converts a FEN string representation of a chessboard to its SVG representation.
     *
     * This method produces a full SVG representation of the chessboard based on the provided FEN string.
     * It will adjust the pieces and board orientation based on the configuration provided during instantiation.
     *
     * @param fen The FEN string representation of the chessboard.
     * @return The SVG string representation of the chessboard.
     */
    fun Fen2Svg(fen: String): String {
        val dimensionsPx = 400
        val squareSize = dimensionsPx / 8
        val paddingFraction = 0.15
        val pieceSizeFraction = 1 - 2*paddingFraction
        val pieceOffset = paddingFraction * squareSize

        val matrix = FenParser.fenToMatrix(fen)

        val chessBoardSVG = emptyBoardSvg(_config, dimensionsPx)

        // Add the pieces
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val rank = if (_config.orientation == Player.WHITE) 7-i else i
                val file = if (_config.orientation == Player.WHITE) j else 7-j

                val pieceKey = matrix[rank][file]

                if (pieceKey != '-') {
                    // Retrieve the SVG data
                    val pieceSVG = _svgProvider.getSvgData(pieceKey)

                    // Add piece to board
                    addPieceToBoard(
                        chessBoardSVG,
                        pieceSVG,
                        squareSize * j + pieceOffset,
                        squareSize * i + pieceOffset,
                        squareSize.toDouble(),
                        pieceSizeFraction)
                }
            }
        }

        // Output the chessboard SVG as a string
        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val source = DOMSource(chessBoardSVG)
        val writer = StringWriter()
        val result = StreamResult(writer)
        transformer.transform(source, result)

        return writer.toString()
    }

    /**
     * Converts a FEN string representation of a chessboard to a PNG byte array.
     *
     * This method first converts the FEN to SVG format and then transcodes the SVG to a PNG byte array.
     * This allows for a rasterized, portable representation of the chessboard. The resulting PNG will
     * have a size defined by the `boardPx` parameter.
     *
     * @param fen The FEN string representation of the chessboard.
     * @param boardPx The desired size (in pixels) of the resulting PNG image's width and height.
     * @return A ByteArray representation of the PNG image.
     */
    fun Fen2Png(fen: String, boardPx: Int): ByteArray {
        val svgData = Fen2Svg(fen)
        val transcoder = PNGTranscoder()
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, boardPx.toFloat())
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, boardPx.toFloat())

        val inputSvgImage = ByteArrayInputStream(svgData.toByteArray())
        val input = TranscoderInput(inputSvgImage)

        val ostream = ByteArrayOutputStream()
        val output = TranscoderOutput(ostream)

        transcoder.transcode(input, output)

        ostream.flush()
        return ostream.toByteArray()
    }
}