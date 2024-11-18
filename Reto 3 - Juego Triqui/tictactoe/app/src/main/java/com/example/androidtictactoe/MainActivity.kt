package com.example.androidtictactoe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var mBoardButtons: Array<ImageView>
    private lateinit var mInfoTextView: TextView
    private lateinit var mButtonPlayAgain: Button
    private lateinit var mButtonConfiguration: Button
    private lateinit var mInfoDifficultyTextView: TextView
    private lateinit var mPlayerWins: TextView
    private lateinit var mBotWins: TextView
    private lateinit var mTies: TextView
    private var playerWins = 0
    private var botWins = 0
    private var ties = 0
    private var difficulty = 0

    companion object {
        const val HUMAN_PLAYER = 'X'
        const val COMPUTER_PLAYER = 'O'
        const val EMPTY_SPACE = ' '
        val TIC_TAC_TOE = charArrayOf(EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE, EMPTY_SPACE)
        val DIFFICULTIES = arrayOf("EASY", "NORMAL", "HARD")
        const val PREFS_NAME = "GameHistory"
        const val PLAYER_WINS_KEY = "playerWins"
        const val BOT_WINS_KEY = "botWins"
        const val TIES_KEY = "ties"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        playerWins = sharedPreferences.getInt(PLAYER_WINS_KEY, 0)
        botWins = sharedPreferences.getInt(BOT_WINS_KEY, 0)
        ties = sharedPreferences.getInt(TIES_KEY, 0)

        mBoardButtons = arrayOf(
            findViewById(R.id.one),
            findViewById(R.id.two),
            findViewById(R.id.three),
            findViewById(R.id.four),
            findViewById(R.id.five),
            findViewById(R.id.six),
            findViewById(R.id.seven),
            findViewById(R.id.eight),
            findViewById(R.id.nine)
        )

        mInfoTextView = findViewById(R.id.information)
        mButtonPlayAgain = findViewById(R.id.playAgainButton)
        mButtonConfiguration = findViewById(R.id.configurationButton)
        mInfoDifficultyTextView = findViewById(R.id.difficulty)
        mPlayerWins = findViewById(R.id.playerWins)
        mBotWins = findViewById(R.id.botWins)
        mTies = findViewById(R.id.draws)
        mInfoDifficultyTextView.visibility = View.GONE
        mPlayerWins.text = "$playerWins wins"
        mBotWins.text = "$botWins wins"
        mTies.text = "$ties draws"


        mButtonPlayAgain.setOnClickListener {
            startNewGame()
        }

        mButtonConfiguration.setOnClickListener {
            showSettingsDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startNewGame() {
        clearBoard()
        mInfoTextView.text = "Player's turn"
        mButtonPlayAgain.visibility = View.GONE
        mButtonConfiguration.visibility = View.GONE
        mInfoDifficultyTextView.visibility = View.VISIBLE
        mInfoDifficultyTextView.text = DIFFICULTIES[difficulty]
        mButtonPlayAgain.text = "PLAY AGAIN!"
    }

    private fun clearBoard() {
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].setImageResource(R.drawable.baseline_square_24)
            mBoardButtons[i].isEnabled = true
            mBoardButtons[i].setOnClickListener(ButtonClickListener(i))
            TIC_TAC_TOE[i] = EMPTY_SPACE
        }
    }

    private inner class ButtonClickListener(private val location: Int) : View.OnClickListener {
        @SuppressLint("SetTextI18n")
        override fun onClick(view: View?) {
            if (mBoardButtons[location].isEnabled) {
                setMove(HUMAN_PLAYER, location)
                var winner = checkForWinner()
                if (winner == 0) {
                    mInfoTextView.text = "Android's turn"
                    val move = getComputerMove()
                    setMove(COMPUTER_PLAYER, move)
                    winner = checkForWinner()
                }
                when (winner) {
                    0 -> mInfoTextView.text = "Player's turn"
                    else -> endGame(winner)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun endGame(winner: Int) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (winner) {
            1 -> {
                mInfoTextView.text = "It's a tie! \uD83D\uDC22"
                ties++
                mTies.text = "$ties draws"
                editor.putInt(TIES_KEY, ties)
            }
            2 -> {
                mInfoTextView.text = "You won! \uD83E\uDD73"
                playerWins++
                mPlayerWins.text = "$playerWins wins"
                editor.putInt(PLAYER_WINS_KEY, playerWins)
            }
            3 -> {
                mInfoTextView.text = "You lose! \uD83D\uDE1F"
                botWins++
                mBotWins.text = "$botWins wins"
                editor.putInt(BOT_WINS_KEY, botWins)
            }
        }

        editor.apply()
        mButtonPlayAgain.visibility = View.VISIBLE
        mButtonConfiguration.visibility = View.VISIBLE
        mInfoDifficultyTextView.visibility = View.GONE

        for (button in mBoardButtons) {
            button.isEnabled = false
        }
    }

    private fun setMove(player: Char, location: Int) {
        TIC_TAC_TOE[location] = player
        mBoardButtons[location].isEnabled = false
        if (player == HUMAN_PLAYER) mBoardButtons[location].setImageResource(R.drawable.x)
        else mBoardButtons[location].setImageResource(R.drawable.circle)
    }

    fun checkForWinner(): Int {
        val winningLines = arrayOf(
            intArrayOf(0, 1, 2), 
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        for (line in winningLines) {
            if (TIC_TAC_TOE[line[0]] != EMPTY_SPACE &&
                TIC_TAC_TOE[line[0]] == TIC_TAC_TOE[line[1]] &&
                TIC_TAC_TOE[line[1]] == TIC_TAC_TOE[line[2]]
            ) {
                return if (TIC_TAC_TOE[line[0]] == HUMAN_PLAYER) 2 else 3
            }
        }

        if (TIC_TAC_TOE.none { it == EMPTY_SPACE }) {
            return 1
        }

        return 0
    }

    fun getComputerMove(): Int {
        return when (difficulty) {
            0 -> randomMove()
            1 -> blockPlayerOrRandomMove()
            2 -> optimalMove()
            else -> -1
        }
    }

    // Dificultad 0: Movimiento aleatorio
    private fun randomMove(): Int {
        val availableMoves = TIC_TAC_TOE.indices.filter { TIC_TAC_TOE[it] == EMPTY_SPACE }
        return if (availableMoves.isNotEmpty()) {
            availableMoves.random()
        } else {
            -1
        }
    }

    // Dificultad 1: Priorizar ganar, bloquear al jugador si puede ganar, o mover al azar
    private fun blockPlayerOrRandomMove(): Int {
        for (i in TIC_TAC_TOE.indices) {
            if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                TIC_TAC_TOE[i] = COMPUTER_PLAYER
                if (checkForWinner() == 3) {
                    TIC_TAC_TOE[i] = EMPTY_SPACE 
                    return i 
                }
                TIC_TAC_TOE[i] = EMPTY_SPACE 
            }
        }
        
        for (i in TIC_TAC_TOE.indices) {
            if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                TIC_TAC_TOE[i] = HUMAN_PLAYER
                if (checkForWinner() == 2) { 
                    TIC_TAC_TOE[i] = EMPTY_SPACE 
                    return i 
                }
                TIC_TAC_TOE[i] = EMPTY_SPACE 
            }
        }
        
        return randomMove()
    }

    // Dificultad 2: Movimiento óptimo (algoritmo Minimax)
    private fun optimalMove(): Int {
        var bestMove = -1
        var bestScore = Int.MIN_VALUE
        for (i in TIC_TAC_TOE.indices) {
            if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                TIC_TAC_TOE[i] = COMPUTER_PLAYER
                val score = minimax(0, false)
                TIC_TAC_TOE[i] = EMPTY_SPACE
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    // Algoritmo Minimax
    private fun minimax(depth: Int, isMaximizing: Boolean): Int {
        val winner = checkForWinner()
        if (winner == 2) return -10 + depth 
        if (winner == 3) return 10 - depth  
        if (TIC_TAC_TOE.none { it == EMPTY_SPACE }) return 0

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in TIC_TAC_TOE.indices) {
                if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                    TIC_TAC_TOE[i] = COMPUTER_PLAYER
                    val score = minimax(depth + 1, false)
                    TIC_TAC_TOE[i] = EMPTY_SPACE
                    bestScore = maxOf(score, bestScore)
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in TIC_TAC_TOE.indices) {
                if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                    TIC_TAC_TOE[i] = HUMAN_PLAYER
                    val score = minimax(depth + 1, true)
                    TIC_TAC_TOE[i] = EMPTY_SPACE
                    bestScore = minOf(score, bestScore)
                }
            }
            return bestScore
        }
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomRadioButton)
        builder.setTitle("Settings")

        val difficulties = arrayOf("Easy", "Normal", "Hard")
        builder.setSingleChoiceItems(difficulties, difficulty) { _, which ->
            difficulty = which
            mInfoDifficultyTextView.text = DIFFICULTIES[which]
        }

        builder.setNegativeButton("Clear History") { dialog, _ ->
            resetHistory()
            clearBoard()
            mInfoTextView.visibility = View.GONE
            mButtonPlayAgain.text = "PLAY"
            dialog.dismiss()
        }

        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        // Crear y mostrar el diálogo
        val dialog = builder.create()
        dialog.show()

        // **Personalizar colores de los botones después de mostrar el diálogo**
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#3988D2"))
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#3988D2"))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun resetHistory() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        playerWins = 0
        botWins = 0
        ties = 0

        mPlayerWins.text = "$playerWins wins"
        mBotWins.text = "$botWins wins"
        mTies.text = "$ties draws"
    }
}
