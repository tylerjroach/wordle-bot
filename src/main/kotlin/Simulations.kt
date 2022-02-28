import Game.play
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object Simulations {

    fun runGuessOnAllValidWords(guess: String) {
        val tries = AtomicInteger(0)
        val guessMap = ConcurrentHashMap<Int, MutableList<String>>()
        runBlocking {
            WordleConfig.validWords.forEach { answer ->
                launch(Dispatchers.IO) {
                    val gameSummary = play(guess, Evaluator(answer))
                    tries.addAndGet(gameSummary.attempts)
                    appendGuess(guessMap, gameSummary, answer)
                }
            }
        }
        println("Ran complete simulation for '$guess' ${WordleConfig.validWords.size} times")
        println("Average guess time is: ${tries.toDouble() / WordleConfig.validWords.size}")
    }

    @Synchronized fun appendGuess(guessMap: ConcurrentHashMap<Int, MutableList<String>>, gameSummary: GameSummary, answer: String) {
        val previousValue = guessMap[gameSummary.attempts]
        if (previousValue == null) {
            guessMap[gameSummary.attempts] = mutableListOf(answer)
        } else {
            guessMap[gameSummary.attempts]?.add(answer)
        }
    }
}