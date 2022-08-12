package com.altalha.hangman

sealed class GameState {

    class Running(val lettersUsed: String, val underscoredWord: String, val drawable: Int, val hintText: String, score: Int) : GameState()

    class Lost(val  wordToGuess: String) : GameState()

    class Won(val wordToGuess: String, val score: Int) : GameState()
}