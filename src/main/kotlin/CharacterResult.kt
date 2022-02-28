sealed class CharacterResult(val logIndicator: Char, val logColorIndicator: String, val evaluationSortOrder: Int) {
    abstract val index: Int
    abstract val character: Char

    data class CorrectCharacterResult(override val index: Int, override val character: Char): CharacterResult('+', "🟩", 0)
    data class MisplacedCharacterResult(override val index: Int, override val character: Char): CharacterResult('~', "🟨" , 1)
    data class AbsentCharacterResult(override val index: Int, override val character: Char) : CharacterResult('-', "⬛", 2)
}