// The minmax PvC logic was created using AI tools (Deepseek) and later edited to suit my needs

// Prompt given to Deepseek: "implement minmax logic for android tic tac toe game in kotlin"


package com.example.tictactoe

import kotlin.math.max
import kotlin.math.min

class TicTacToeAI {
    fun getMove(board: Array<CharArray>): Pair<Int, Int>? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null

        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == ' ') {
                    board[row][col] = 'O'
                    val score = minimax(board, 0, false)
                    board[row][col] = ' '

                    if (score > bestScore) {
                        bestScore = score
                        bestMove = Pair(row, col)
                    }
                }
            }
        }

        return bestMove
    }

    private fun minimax(board: Array<CharArray>, depth: Int, isMaximizing: Boolean): Int {
        val result = checkWinner(board)
        if (result != null) {
            return when (result) {
                'O' -> 10 - depth
                'X' -> depth - 10
                else -> 0
            }
        }

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (row in 0..2) {
                for (col in 0..2) {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'O'
                        val score = minimax(board, depth + 1, false)
                        board[row][col] = ' ' // Undo move
                        bestScore = max(score, bestScore)
                    }
                }
            }
            return bestScore
        }
        else {
            var bestScore = Int.MAX_VALUE
            for (row in 0..2) {
                for (col in 0..2) {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'X'
                        val score = minimax(board, depth + 1, true)
                        board[row][col] = ' ' // Undo move
                        bestScore = min(score, bestScore)
                    }
                }
            }
            return bestScore
        }
    }

    private fun checkWinner(board: Array<CharArray>): Char? {
        for (row in 0..2) {
            if (board[row][0] != ' ' && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return board[row][0]
            }
        }

        for (col in 0..2) {
            if (board[0][col] != ' ' && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col]
            }
        }


        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        if (board.all { row -> row.none { it == ' ' } }) {
            return 'D'
        }

        return null
    }
}