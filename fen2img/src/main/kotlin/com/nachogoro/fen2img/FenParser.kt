package com.nachogoro.fen2img

/**
 * Class in charge of parsing FEN strings.
 */
class FenParser {
    companion object {
        /**
         * Converts a FEN (Forsyth–Edwards Notation) string representation of a chessboard
         * to a 2D character array matrix representation.
         *
         * The matrix is organized as follows:
         * - The rows of the matrix (first dimension) correspond to ranks on the chessboard, with `board[0]`
         * representing rank 8 and `board[7]` representing rank 1.
         * - The columns of the matrix (second dimension) correspond to files on the chessboard, with `board[x][0]`
         * representing file 'a' and `board[x][7]` representing file 'h'.
         *
         * Thus, to get the piece at a specific rank and file from the matrix, you can use:
         * `board[8 - rank][file - 'a']`
         *
         * And conversely, given a matrix coordinate `(i, j)`, the corresponding rank and file would be:
         * `rank = 8 - i`
         * `file = 'a' + j`
         *
         * Each square of the matrix is represented by a character:
         * - Uppercase characters represent white pieces.
         * - Lowercase characters represent black pieces.
         * - '-' represents an empty square.
         *
         * @param fen The FEN string representation of the chessboard. It should be properly formatted.
         * @return A 8x8 matrix where each cell represents a square on the chessboard.
         *
         * Example:
         * ```
         * val fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
         * val board = fenToMatrix(fen)
         * println(board[0][0])  // prints 'r' (Black rook at rank 8 and file 'a')
         * ```
         */
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
