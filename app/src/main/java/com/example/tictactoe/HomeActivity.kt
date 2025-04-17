package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var player1Input: EditText
    private lateinit var player2Input: EditText
    private lateinit var pvpButton: Button
    private lateinit var pvcButton: Button
    private lateinit var startGameButton: Button
    private var gameMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        player1Input = findViewById(R.id.player1Name)
        player2Input = findViewById(R.id.player2Name)
        pvpButton = findViewById(R.id.pvpButton)
        pvcButton = findViewById(R.id.pvcButton)
        startGameButton = findViewById(R.id.startGameButton)

        pvpButton.setOnClickListener {
            gameMode = "PvP"
            pvpButton.isSelected = true
            pvcButton.isSelected = false
            player2Input.visibility = View.VISIBLE
        }

        pvcButton.setOnClickListener {
            gameMode = "PvC"
            pvpButton.isSelected = false
            pvcButton.isSelected = true
            player2Input.visibility = View.GONE
        }


        startGameButton.setOnClickListener {
            val name1 = player1Input.text.toString().trim()
            val name2 = player2Input.text.toString().trim()

            if (name1.isEmpty()) {
                Toast.makeText(this, "Enter Player 1 name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (gameMode.isEmpty()) {
                Toast.makeText(this, "Select a game mode", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PLAYER1", name1)
            intent.putExtra("MODE", gameMode)
            if (gameMode == "PvP") {
                if (name2.isEmpty()) {
                    Toast.makeText(this, "Enter Player 2 name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                intent.putExtra("PLAYER2", name2)
            }
            startActivity(intent)
        }
    }
}