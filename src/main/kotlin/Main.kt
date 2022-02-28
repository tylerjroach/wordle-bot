import Game.play
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val timeInMillis = measureTimeMillis {
        playWithArgs(args)
        //Simulations.runGuessOnAllValidWords("salet")
    }
    println("Time to execute: $timeInMillis milliseconds")
}

fun playWithArgs(args: Array<String>) {
    val guess = args.getOrElse(0) {
        println("No guess provided in args. Selecting random word.")
        WordleConfig.validWords.random()
    }

    val answer = args.getOrElse(1) {
        println("No correct word provided in args. Selecting random word.")
        WordleConfig.validWords.random()
    }
    playGame(guess, answer)
}

fun playGame(guess: String, answer: String) {
    println("Correct word to guess: $answer")
    val gameSummary = play(guess, Evaluator(answer))
    printLogs(gameSummary.logs)
}

private fun printLogs(logs: List<String>) {
    logs.forEach { println(it) }
}