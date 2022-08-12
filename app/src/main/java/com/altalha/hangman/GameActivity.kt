package com.altalha.hangman

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children

class GameActivity : AppCompatActivity() {

    private val gameManager = GameManager()

    private lateinit var wordTextView: TextView
    private lateinit var usedLetterText: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostText: TextView
    private lateinit var gameWonText: TextView
    private lateinit var newGameButton: Button
    private lateinit var letterLayout: ConstraintLayout
    private lateinit var hintTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        imageView = findViewById(R.id.imageView)
        wordTextView = findViewById(R.id.wordText)
        usedLetterText = findViewById(R.id.usedLetterText)
        gameLostText = findViewById(R.id.gameLostText)
        gameWonText = findViewById(R.id.gameWinText)
        newGameButton = findViewById(R.id.newGameButton)
        letterLayout = findViewById(R.id.letterLayout)
        hintTextView = findViewById(R.id.hintText)

        newGameButton.setOnClickListener {
            startNewGame()
        }

        val gameState = gameManager.startNewGame()
        updateUI(gameState)

        letterLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = gameManager.play((letterView).text[0])
                    updateUI(gameState)
                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun startNewGame() {
        gameLostText.visibility = View.GONE
        gameWonText.visibility = View.GONE
        val gameState = gameManager.startNewGame()
        imageView.clearColorFilter()
        usedLetterText.visibility = View.VISIBLE
        letterLayout.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE
        letterLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }

        updateUI(gameState)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Lost -> showGameLost(gameState.wordToGuess)
            is GameState.Running -> {
                wordTextView.text = gameState.underscoredWord
                usedLetterText.text = "Letters used: ${gameState.lettersUsed}"
                imageView.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))
                hintTextView.text = gameState.hintText
            }
            is GameState.Won -> showGameWon(gameState.wordToGuess, gameState.score)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showGameWon(wordToGuess: String, scores: Int) {
        wordTextView.text = wordToGuess.uppercase()
        gameWonText.visibility = View.VISIBLE
        letterLayout.visibility = View.GONE
        usedLetterText.text = "Your Sore: $scores"
        hintTextView.visibility = View.GONE
        imageView.setColorFilter(resources.getColor(R.color.white))
    }

    private fun showGameLost(wordToGuess: String) {
        wordTextView.text = wordToGuess.uppercase()
        gameLostText.visibility = View.VISIBLE
        usedLetterText.visibility = View.GONE
        hintTextView.visibility = View.GONE
        imageView.setColorFilter(resources.getColor(R.color.white))
        letterLayout.visibility = View.GONE
    }





}