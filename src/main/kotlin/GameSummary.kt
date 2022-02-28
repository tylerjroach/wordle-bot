data class GameSummary(val attempts: Int, val logs: List<String>) {

    companion object {
        fun fromEvaluator(evaluator: Evaluator) = GameSummary(
            evaluator.results.size,
            evaluator.clearResultLogs + evaluator.obfuscatedResultLogs + "You guessed the correct word in ${evaluator.results.size} attempts!"
        )
    }
}
