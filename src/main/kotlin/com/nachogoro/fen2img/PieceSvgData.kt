package com.nachogoro.fen2img

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.FileNotFoundException
import java.io.StringReader
import java.lang.IllegalArgumentException
import javax.xml.parsers.DocumentBuilderFactory

class PieceSvgDataProvider(pieceDataMap: Map<Char, String>) {
    private val _pieceDataMap = pieceDataMap
    private val _docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

    // File to use if no custom pieces are used
    private val _defaultPieceFileMap = mapOf(
        'B' to "bishop-white.svg",
        'K' to "king-white.svg",
        'N' to "knight-white.svg",
        'P' to "pawn-white.svg",
        'Q' to "queen-white.svg",
        'R' to "rook-white.svg",
        'b' to "bishop-black.svg",
        'k' to "king-black.svg",
        'n' to "knight-black.svg",
        'p' to "pawn-black.svg",
        'q' to "queen-black.svg",
        'r' to "rook-black.svg"
    )

    private val _svgCache = mutableMapOf<Char, Document>()

    fun getSvgData(piece: Char): Document {
        // If already loaded, just return it
        _svgCache[piece]?.let { return it }

        // If the user has a custom suggestion, use it
        if (_pieceDataMap[piece] != null) {
            val result = _docBuilder.parse(InputSource(StringReader(_pieceDataMap[piece])))

            _svgCache[piece] = result
            return result
        }

        val fileName = _defaultPieceFileMap[piece] ?: throw IllegalArgumentException("Invalid piece $piece")

        val classLoader = javaClass.classLoader
        val resourceStream = classLoader.getResourceAsStream("svg/$fileName")
            ?: throw FileNotFoundException("File not found: $fileName")

        val result = _docBuilder.parse(
            InputSource(StringReader(resourceStream.bufferedReader().use { it.readText() })))

        _svgCache[piece] = result
        return result
    }
}

