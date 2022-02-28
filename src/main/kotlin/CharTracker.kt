class CharTracker {
    private val chars = BooleanArray(26)

    fun contains(char: Char) = chars[char - 'a']

    fun set(char: Char, contains: Boolean) {
        chars[char - 'a'] = contains
    }
}