class CharFrequencies(words: List<String>, method: ScoreMethod) {
    private val frequencies = IntArray(26)

    init {
        words.forEach { word ->
            word.forEach { char ->
                val previousScore = get(char)
                val add = if (method == ScoreMethod.WEIGHTED) {
                    if (previousScore == 0) 1000 else 1
                } else {
                    1
                }
                set(char, previousScore + add)
            }
        }
    }

    private fun set(char: Char, value: Int) {
        frequencies[char - 'a'] = value
    }

    fun get(char: Char) = frequencies[char - 'a']

    enum class ScoreMethod {
        WEIGHTED, FREQUENCY
    }
}