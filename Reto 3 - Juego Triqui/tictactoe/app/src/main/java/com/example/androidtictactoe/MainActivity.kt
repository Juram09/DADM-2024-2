package com.example.androidtictactoe

import android.annotation.SuppressLint
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
    private lateinit var mButtonDifficulty: Button
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        // Inicializar botones del tablero
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

        // Inicializar vistas adicionales
        mInfoTextView = findViewById(R.id.information)
        mButtonPlayAgain = findViewById(R.id.playAgainButton)
        mButtonDifficulty = findViewById(R.id.configurationButton)
        mInfoDifficultyTextView = findViewById(R.id.difficulty)
        mPlayerWins = findViewById(R.id.playerWins)
        mBotWins = findViewById(R.id.botWins)
        mTies = findViewById(R.id.draws)
        mInfoDifficultyTextView.visibility = View.GONE

        mButtonPlayAgain.setOnClickListener {
            startNewGame()
        }

        mButtonDifficulty.setOnClickListener {
            changeDifficulty()
        }
    }

    private fun changeDifficulty() {
        difficulty = (difficulty+1)%3
        mButtonDifficulty.text = DIFFICULTIES[difficulty]
    }

    @SuppressLint("SetTextI18n")
    private fun startNewGame() {
        clearBoard()
        mInfoTextView.text = "Player's turn"
        mButtonPlayAgain.visibility = View.GONE
        mButtonDifficulty.visibility = View.GONE
        mInfoDifficultyTextView.text = DIFFICULTIES[difficulty]
        mInfoDifficultyTextView.visibility = View.VISIBLE
        mButtonPlayAgain.text = "PLAY AGAIN!"
    }

    private fun clearBoard() {
        // Reset all buttons
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
            // Verifica si el botón está habilitado
            if (mBoardButtons[location].isEnabled) {
                // Realiza el movimiento del jugador
                setMove(HUMAN_PLAYER, location)

                // Comprueba si hay un ganador
                var winner = checkForWinner()
                if (winner == 0) {
                    mInfoTextView.text = "Android's turn"

                    // Movimiento del jugador Android
                    val move = getComputerMove()
                    setMove(COMPUTER_PLAYER, move)

                    // Vuelve a comprobar el estado del juego
                    winner = checkForWinner()
                }

                // Actualiza la información en pantalla según el resultado
                when (winner) {
                    0 -> mInfoTextView.text = "Player's turn"
                    else -> endGame(winner)
                }

            }
        }
    }
    // Finaliza el juego y muestra el botón Play Again
    @SuppressLint("SetTextI18n")
    private fun endGame(winner: Int) {
        when (winner) {
            1 -> {
                mInfoTextView.text = "It's a tie!"
                ties++
                mTies.text = "$ties draws"
            }
            2 -> {
                mInfoTextView.text = "You won!"
                playerWins++
                mPlayerWins.text = "$playerWins wins"
            }
            3 -> {
                mInfoTextView.text = "Android won!"
                botWins++
                mBotWins.text = "$botWins wins"
            }
        }
        mButtonPlayAgain.visibility = View.VISIBLE
        mButtonDifficulty.visibility = View.VISIBLE
        mInfoDifficultyTextView.visibility = View.GONE// Muestra el botón al finalizar el juego


        // Desactiva los botones restantes
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
        // Líneas ganadoras
        val winningLines = arrayOf(
            intArrayOf(0, 1, 2), // Filas
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), // Columnas
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), // Diagonales
            intArrayOf(2, 4, 6)
        )

        // Verifica si un jugador ganó
        for (line in winningLines) {
            if (TIC_TAC_TOE[line[0]] != EMPTY_SPACE &&
                TIC_TAC_TOE[line[0]] == TIC_TAC_TOE[line[1]] &&
                TIC_TAC_TOE[line[1]] == TIC_TAC_TOE[line[2]]
            ) {
                return if (TIC_TAC_TOE[line[0]] == HUMAN_PLAYER) 2 else 3
            }
        }

        // Verifica si hay un empate
        if (TIC_TAC_TOE.none { it == EMPTY_SPACE }) {
            return 1
        }

        // Ningún ganador aún
        return 0
    }

    // Lógica básica: elige la primera casilla vacía
    fun getComputerMove(): Int {
        return when (difficulty) {
            0 -> randomMove()
            1 -> blockPlayerOrRandomMove()
            2 -> optimalMove()
            else -> -1
        }
    }// Retorna -1 si no hay movimientos válidos

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
        // 1. Verifica si el bot puede ganar en la próxima jugada
        for (i in TIC_TAC_TOE.indices) {
            if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                TIC_TAC_TOE[i] = COMPUTER_PLAYER
                if (checkForWinner() == 3) { // El bot ganaría en esta posición
                    TIC_TAC_TOE[i] = EMPTY_SPACE // Restaurar el tablero
                    return i // Realizar el movimiento ganador
                }
                TIC_TAC_TOE[i] = EMPTY_SPACE // Restaurar el tablero
            }
        }

        // 2. Verifica si el jugador puede ganar en la próxima jugada y bloquea
        for (i in TIC_TAC_TOE.indices) {
            if (TIC_TAC_TOE[i] == EMPTY_SPACE) {
                TIC_TAC_TOE[i] = HUMAN_PLAYER
                if (checkForWinner() == 2) { // El jugador ganaría en esta posición
                    TIC_TAC_TOE[i] = EMPTY_SPACE // Restaurar el tablero
                    return i // Bloquear movimiento
                }
                TIC_TAC_TOE[i] = EMPTY_SPACE // Restaurar el tablero
            }
        }

        // 3. Si no hay jugadas críticas, mover al azar
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
        if (winner == 2) return -10 + depth // Jugador gana
        if (winner == 3) return 10 - depth  // Bot gana
        if (TIC_TAC_TOE.none { it == EMPTY_SPACE }) return 0 // Empate

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
}