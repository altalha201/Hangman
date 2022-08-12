package com.altalha.hangman

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.random.Random

class GameManager {

    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    private lateinit var wordToGuess: String
    private lateinit var hintText: String
    private val maxTries = 7
    private var currentTries = 0
    private var scores = 8
    private var drawable: Int = R.drawable.game0

    fun startNewGame() : GameState {
        lettersUsed = ""
        currentTries = 0
        drawable = R.drawable.game0
        val  randomIndex = Random.nextInt(
            0,
            GameConstants.words.size
        )

        wordToGuess = GameConstants.words[randomIndex]
        generateUnderscores(wordToGuess)
        if (randomIndex <= GameConstants.hints.size) {
            hintText = GameConstants.hints[randomIndex]
        }
        else {
            hintText = "Hint Here"
        }
        return getGameState()
    }

    private fun generateUnderscores(word: String) {
        val sb = StringBuilder()
        val skip = Random.nextInt(0, word.length)
        val skipCharacter = word[skip]

        word.forEach { cher ->
            if (cher.equals(" ")) sb.append("/")
            else sb.append("_")
        }
        underscoreWord = sb.toString()
        underscoreWord = underscoreWord.substring(0, skip) + skipCharacter.uppercase() + underscoreWord.substring(skip+1)
    }

    private fun getHangmanDrawable(): Int {
        return when (currentTries) {
            0 -> R.drawable.game0
            1 -> R.drawable.game1
            2 -> R.drawable.game2
            3 -> R.drawable.game3
            4 -> R.drawable.game4
            5 -> R.drawable.game5
            6 -> R.drawable.game6
            7 -> R.drawable.game7
            else -> R.drawable.game7
        }
    }

    private fun getScore(): Int {
        return when(currentTries) {
            0 -> 8
            1 -> 7
            2 -> 6
            3 -> 5
            4 -> 4
            5 -> 3
            6 -> 2
            7 -> 1
            else -> 0
        }
    }

    private fun getGameState() : GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            return GameState.Won(wordToGuess, scores)
        }

        if (currentTries == maxTries) {
            return GameState.Lost(wordToGuess)
        }

        drawable= getHangmanDrawable()
        scores = getScore()
        return GameState.Running(lettersUsed, underscoreWord, drawable, hintText, scores)
    }

    fun play(letter: Char) : GameState {
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, drawable, hintText, scores)
        }
        lettersUsed += letter
        val indexes = mutableListOf<Int>()

        wordToGuess.forEachIndexed { index, c ->
            if (c.equals(letter, true)) {
                indexes.add(index)
            }
        }

        var  finalUnderscoreWord = "" + underscoreWord
        indexes.forEach{ index ->
            val sb = StringBuilder(finalUnderscoreWord).also { it.setCharAt(index, letter) }
            finalUnderscoreWord = sb.toString()
        }

        if (indexes.isEmpty()) {
            currentTries++
        }

        underscoreWord = finalUnderscoreWord
        return getGameState()

    }

}