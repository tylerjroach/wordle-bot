import CharacterResult.*
import WordleConfig.CHARACTERS_IN_WORD

class Evaluator(private val answer: String, val results: MutableList<EvaluationResult> = mutableListOf()) {

    val clearResultLogs = mutableListOf<String>()
    val obfuscatedResultLogs = mutableListOf<String>()

    val knownCorrectCharactersInPosition = CharArray(5)
    val knownCorrectCharacters = CharTracker()
    val knownMisplacedCharacters = CharTracker()
    val knownAbsentCharacters = CharTracker()
    val misplacedCharacterResults = mutableSetOf<MisplacedCharacterResult>()

    fun evaluate(guess: String) = evaluateGuess(guess, answer).apply {
        results.add(this)
        characterResultsByIndexSort.forEach { result ->
            when(result) {
                is CorrectCharacterResult -> {
                    knownCorrectCharactersInPosition[result.index] = result.character
                    knownCorrectCharacters.set(result.character, true)
                }
                is MisplacedCharacterResult -> {
                    misplacedCharacterResults.add(result)
                    knownMisplacedCharacters.set(result.character, true)
                }
                is AbsentCharacterResult -> knownAbsentCharacters.set(result.character, true)
            }
        }
        clearResultLogs.add(logResult())
        obfuscatedResultLogs.add(logObfuscatedResult())
    }

    companion object {
        fun evaluateGuess(guess: String, correctWord: String): EvaluationResult {
            if (guess.count() != correctWord.count() || guess.count() != CHARACTERS_IN_WORD) {
                throw RuntimeException("Guess and/or correct word are not $CHARACTERS_IN_WORD characters long.")
            }

            val characterOutcomes = arrayOfNulls<CharacterResult>(CHARACTERS_IN_WORD)
            val processedCharacters = mutableListOf<Char>()

            guess.forEachIndexed { index, char ->
                if (char == correctWord[index]) {
                    characterOutcomes[index] = CorrectCharacterResult(index, char)
                    processedCharacters.add(char)
                }
            }
            guess.forEachIndexed { index, char ->
                if (char != correctWord[index]) {
                    val occurrenceInCorrectWord = correctWord.count { it == char }
                    val occurrenceInProcessedCharacters = processedCharacters.count { it == char }
                    characterOutcomes[index] = if (occurrenceInCorrectWord > occurrenceInProcessedCharacters) {
                        MisplacedCharacterResult(index, char)
                    } else {
                        AbsentCharacterResult(index, char)
                    }
                    processedCharacters.add(char)
                }
            }
            return EvaluationResult(characterOutcomes.filterNotNull())
        }
    }
}