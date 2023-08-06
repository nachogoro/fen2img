package com.nachogoro.fen2img

class FenParser {
    companion object {
        fun fenToMatrix(fen: String): Array<CharArray> {
            // Initialize an empty 8x8 board
            val board = Array(8) { CharArray(8) { '-' } }

            // Split the FEN string into its components and take the first one, which represents the board
            val fenParts = fen.split(" ")
            val boardDescription = fenParts[0]

            // Split the board description into ranks (rows)
            val ranks = boardDescription.split("/")

            // Iterate over each rank (row) in the board description
            for ((rowIndex, rank) in ranks.withIndex()) {
                var colIndex = 0

                // Iterate over each character in the rank description
                for (char in rank) {
                    if (char.isDigit()) {
                        // The character is a number, indicating empty squares
                        val emptySquares = Character.getNumericValue(char)

                        // Fill the appropriate number of squares with '-'
                        for (i in 0 until emptySquares) {
                            board[7 - rowIndex][colIndex] = '-'
                            colIndex++
                        }
                    } else {
                        // The character is a piece
                        board[7 - rowIndex][colIndex] = char
                        colIndex++
                    }
                }
            }

            return board
        }
    }
}
