import WordUtils.filterIncompatibleWords
import WordUtils.highestScoringWordExcludingCharacters
import WordUtils.wordWithMostCommonCharacters

object Game {

    fun play(
        firstGuess: String,
        evaluator: Evaluator,
        dictionary: List<String> = WordleConfig.validWords,
        possibleWords: List<String> = WordleConfig.validWords
    ): GameSummary {
        val evaluationResult = evaluator.evaluate(firstGuess)
        return if (evaluationResult.isCorrectGuess) {
            GameSummary.fromEvaluator(evaluator)
        } else {
            val filteredWords = filterIncompatibleWords(possibleWords, firstGuess, evaluationResult)

            val nextGuess = if (filteredWords.size > 4) {
                highestScoringWordExcludingCharacters(
                    fullDictionary = dictionary,
                    possibleWords = filteredWords,
                    evaluator = evaluator,
                    guessNumber = evaluator.results.size + 1)
            } else {
                wordWithMostCommonCharacters(filteredWords)
            }
            play(nextGuess, evaluator, dictionary, filteredWords)
        }
    }
}