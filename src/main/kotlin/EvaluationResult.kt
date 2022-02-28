import CharacterResult.CorrectCharacterResult
import CharacterResult.MisplacedCharacterResult

data class EvaluationResult(private val results: List<CharacterResult>) {

    // sort evaluation charters by correct, wrong spot, incorrect
    val characterResultsByEvaluationSort: List<CharacterResult> by lazy {
        results.sortedBy(CharacterResult::evaluationSortOrder)
    }

    // sort evaluation charters by word index
    val characterResultsByIndexSort: List<CharacterResult> by lazy {
        results.sortedBy(CharacterResult::index)
    }

    val isCorrectGuess = results.filterIsInstance<CorrectCharacterResult>().size == results.size

    private val knownValidCharacters: List<Char> by lazy {
        results.filter { it is CorrectCharacterResult || it is MisplacedCharacterResult }.map { it.character }
    }

    fun countKnownValidOccurrences(character: Char) = knownValidCharacters.count { it == character }

    fun logResult(): String =
        characterResultsByIndexSort.joinToString(separator = " ") { characterResult ->
            "${characterResult.logIndicator}${characterResult.character}"
        }

    fun logObfuscatedResult(): String =
        characterResultsByIndexSort.joinToString(separator = "") { characterResult ->
            characterResult.logColorIndicator
        }
}
