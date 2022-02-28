import CharacterResult.*
import CharFrequencies.ScoreMethod.*

object WordUtils {

    fun wordWithMostCommonCharacters(possibleWords: List<String>): String {
        val scoredCharacters = CharFrequencies(possibleWords, FREQUENCY)

        return possibleWords.maxByOrNull { word ->
            word.toSet().fold(0) { acc, char ->
                acc + scoredCharacters.get(char)
            }
        } ?: possibleWords[0]
    }

    fun highestScoringWordExcludingCharacters(fullDictionary: List<String>, possibleWords: List<String>, evaluator: Evaluator, guessNumber: Int): String {
        val scoredCharacters = CharFrequencies(possibleWords, WEIGHTED)
        val topScoringWords = mutableListOf<String>()
        var topScore = 0.0

        fullDictionary.forEach { word ->
            val seen = CharTracker()
            val score = word.foldIndexed(0.0) { index, acc, character ->
                val characterScore = scoredCharacters.get(character)

                val add = if (evaluator.knownCorrectCharactersInPosition[index] == character && guessNumber > 2) {
                    // correct in exact position
                    characterScore / 3.0
                } else if (evaluator.knownAbsentCharacters.contains(character) || evaluator.misplacedCharacterResults.contains(MisplacedCharacterResult(index, character))) {
                    // absent or misplaced in position
                    0.0
                } else if(seen.contains(character)) {
                    // seen
                    if (guessNumber <= 2) 0.0 else 2.0
                } else if (evaluator.knownCorrectCharacters.contains(character) || (evaluator.knownMisplacedCharacters.contains(character))) {
                    // check if is correct anywhere, or misplaced anywhere
                    if (guessNumber <= 2) 1.0 else characterScore / 3.0
                } else {
                    characterScore.toDouble()
                }
                seen.set(character, true)
                acc + add
            }
            if (score >= topScore) {
                if (score > topScore) {
                    topScoringWords.clear()
                    topScore = score
                }
                topScoringWords.add(word)
            }
        }

        if (topScoringWords.size == 1) {
            return topScoringWords.first()
        }

        val topScoringWordsInPossibleList = topScoringWords.filter { possibleWords.contains(it) }.map { it }
        return if (topScoringWordsInPossibleList.isNotEmpty()) {
            wordWithMostCommonCharacters(topScoringWordsInPossibleList)
        } else {
            topScoringWords.first()
        }
    }

   fun filterIncompatibleWords(possibleWords: List<String>, guess: String, evaluationResult: EvaluationResult): List<String> {
        // build a sequence and process all filter operations that need run in a single iteration of word list
        return evaluationResult.characterResultsByEvaluationSort.runningFold(
            possibleWords.asSequence().filterNot { it == guess }
        ) { accumulatingSequence, characterResult ->
            filterByCharacterResult(accumulatingSequence, characterResult, evaluationResult)
        }.last().toList()
    }

    // Continuous sequence operation (non-terminal)
    private fun filterByCharacterResult(possibleWords: Sequence<String>, characterResult: CharacterResult, evaluationResult: EvaluationResult): Sequence<String> {
        return when(characterResult) {
            is CorrectCharacterResult -> filterCorrectCharacterResult(possibleWords, characterResult)
            is MisplacedCharacterResult -> filterMisplacedCharacterResult(possibleWords, characterResult)
            is AbsentCharacterResult -> filterAbsentCharacterResult(possibleWords, characterResult, evaluationResult)
        }
    }

    private fun filterCorrectCharacterResult(words: Sequence<String>, characterResult: CharacterResult) =
        words.filter { it[characterResult.index] == characterResult.character }

    private fun filterMisplacedCharacterResult(words: Sequence<String>, characterResult: CharacterResult) =
        words.filter { it[characterResult.index] != characterResult.character && it.contains(characterResult.character) }

    private fun filterAbsentCharacterResult(words: Sequence<String>, characterResult: CharacterResult, evaluationResult: EvaluationResult): Sequence<String> {
        val guessCharacter = characterResult.character
        val knownOccurrencesOfAbsentCharacter = evaluationResult.countKnownValidOccurrences(guessCharacter)
        return if (knownOccurrencesOfAbsentCharacter == 0) {
            words.filterNot { it.contains(guessCharacter) }
        } else {
            words.filterNot { word -> word.count { it == guessCharacter } > knownOccurrencesOfAbsentCharacter }
        }
    }
}