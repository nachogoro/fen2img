package com.nachogoro.fen2img

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.FileNotFoundException
import java.io.StringReader
import java.lang.IllegalArgumentException
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Provides SVG data for chess pieces, either from custom input or default resource files.
 *
 * This class is designed to fetch SVG representations of chess pieces based on their character representation:
 * - Uppercase characters ('K', 'Q', 'R', 'N', 'B', 'P') represent white pieces.
 * - Lowercase characters ('k', 'q', 'r', 'n', 'b', 'p') represent black pieces.
 *
 * By default, the class fetches SVGs from embedded resource files. However, users can provide custom SVG strings
 * for specific pieces via the `pieceDataMap` constructor parameter.
 *
 * The fetched SVGs are cached to enhance performance on subsequent requests.
 *
 * @property pieceDataMap A map of chess piece characters to their SVG string representations.
 */
class PieceSvgDataProvider(pieceDataMap: Map<Char, String> = emptyMap()) {
    private val _pieceDataMap = pieceDataMap
    private val _docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

    /* Resource file to use if no custom pieces are used */
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

    /* Cache of parsed SVG data */
    private val _svgCache = mutableMapOf<Char, Document>()

    /**
     * Fetches the SVG data document for a specified chess piece.
     *
     * If the SVG data for the given piece is already cached, it is returned directly. Otherwise, the SVG data is
     * fetched either from the provided custom mappings (`pieceDataMap`) or the default resource files.
     *
     * @param piece The character representation of the chess piece.
     * @return The parsed SVG data as a [Document].
     *
     * @throws IllegalArgumentException If the provided character does not correspond to a valid chess piece.
     * @throws FileNotFoundException If the default resource file for the given piece is not found.
     */
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

