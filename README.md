# Wordle Bot

A Wordle Bot written in Kotlin that was written to compete in a team coding challenge. 
The rules allowed for using the valid list of all wordle words (~13k), but not the much smaller list of solutions.

## Build
`./gradlew shadowJar`

## Run
```
# java -jar wordle-bot.jar salet codes

Correct word to guess: codes
~s -a -l +e -t
-n -i +d ~o -r
+c +o +d +e +s
🟨⬛⬛🟩⬛
⬛⬛🟩🟨⬛
🟩🟩🟩🟩🟩
You guessed the correct word in 3 attempts!
Time to execute: 58 milliseconds
```
## Simulate Word

```
Simulations.runGuessOnAllValidWords("salet")

Ran complete simulation for 'salet' 12972 times
Average guess time is: 4.319611470860314
Time to execute: 5753 milliseconds
```

This is not currently possible from the command line
