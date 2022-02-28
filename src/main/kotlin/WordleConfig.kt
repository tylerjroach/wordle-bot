object WordleConfig {

    const val CHARACTERS_IN_WORD = 5

    val validWords: List<String> by lazy {
        Thread.currentThread().contextClassLoader.getResourceAsStream("valid_wordle_words.txt")!!
            .bufferedReader()
            .readLines()
    }
}