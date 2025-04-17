// Some parts of the main code and design were inspired from youtube video series:
// https://www.youtube.com/watch?v=KliGjQQXm00&list=PL4rSCGbo0LQ031Z7EG3RJedkFdXp_Z2LF&index=1

package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var board: Array<CharArray>
    private lateinit var buttons: Array<Array<Button>>
    private var playerTurn = true
    private lateinit var c: TicTacToeAI
    private lateinit var mode: String
    private lateinit var player1: String
    private lateinit var player2: String
    private lateinit var turnTextView: TextView
    private lateinit var resultTextView: TextView

    private var playerStarts = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnTextView = findViewById(R.id.turn_text)
        resultTextView = findViewById(R.id.result)

        player1 = intent.getStringExtra("PLAYER1") ?: "Player 1"
        player2 = intent.getStringExtra("PLAYER2") ?: "Computer"
        mode = intent.getStringExtra("MODE") ?: "PvC"

        c = TicTacToeAI()

        board = Array(3) { CharArray(3) { ' ' } }

        val buttonIds = arrayOf(
            arrayOf(R.id.button1, R.id.button2, R.id.button3),
            arrayOf(R.id.button4, R.id.button5, R.id.button6),
            arrayOf(R.id.button7, R.id.button8, R.id.button9)
        )

        buttons = Array(3) { row ->
            Array(3) { col ->
                findViewById<Button>(buttonIds[row][col]).apply {
                    setOnClickListener { onPlayerMove(row, col) }
                }
            }
        }

        findViewById<Button>(R.id.home_button).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            resetGame()
        }

        updateText()
        resultTextView.visibility = View.INVISIBLE
    }

    private fun onPlayerMove(row: Int, col: Int) {
        if (board[row][col] != ' ') {
            return
        }

        makeMove(row, col, if (playerTurn) 'X' else 'O')

        if (checkGameEnd()) return

        if (mode == "PvP") {
            playerTurn = !playerTurn
            updateText()
        }
        else {
            playerTurn = false
            updateText()
            Handler(Looper.getMainLooper()).postDelayed({
                cMove()
            }, 500)
        }
    }

    private fun makeMove(row: Int, col: Int, symbol: Char) {
        board[row][col] = symbol
        buttons[row][col].setBackgroundResource(if (symbol == 'X') R.drawable.x else R.drawable.o)
        buttons[row][col].text = ""
    }

    private fun cMove() {
        val move = c.getMove(board)
        if (move != null) {
            val (row, col) = move
            makeMove(row, col, 'O')

            if (checkGameEnd()) return
        }
        playerTurn = true
        updateText()
    }

    private fun checkGameEnd(): Boolean {
        val xWin = checkWin('X')
        val oWin = checkWin('O')
        val draw = checkBoard()

        return when {
            xWin -> {
                val winner = player1
                showResult("$winner wins!")
                true
            }
            oWin -> {
                val winner = if (mode == "PvP") player2 else "Computer"
                showResult("$winner wins!")
                true
            }
            draw -> {
                showResult("Draw!")
                true
            }
            else -> false
        }
    }

    private fun updateText() {
        val currentPlayer =
        if (playerTurn) {
            "$player1 (X)"
        }
        else {
            if (mode == "PvP") "$player2 (O)" else "Computer (O)"
        }
        turnTextView.text = "Current turn: $currentPlayer"
    }

    private fun showResult(message: String) {
        resultTextView.text = message
        resultTextView.visibility = View.VISIBLE
        turnTextView.visibility = View.GONE
        disableButtons()
    }

    private fun checkWin(symbol: Char): Boolean {
        for (i in 0..2) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol))
                return true
        }
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)
    }

    private fun checkBoard(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == ' ') return false
            }
        }
        return true
    }

    private fun disableButtons() {
        for (row in buttons) {
            for (button in row) {
                button.isEnabled = false
            }
        }
    }

    private fun resetGame() {
        board = Array(3) { CharArray(3) { ' ' } }
        for (row in buttons) {
            for (button in row) {
                button.text = ""
                button.setBackgroundResource(0)
                button.isEnabled = true
            }
        }

        playerStarts = !playerStarts
        playerTurn = playerStarts

        resultTextView.visibility = View.INVISIBLE
        turnTextView.visibility = View.VISIBLE
        updateText()

        if (mode == "PvC" && !playerTurn) {
            Handler(Looper.getMainLooper()).postDelayed({
                cMove()
            }, 500)
        }
    }
}