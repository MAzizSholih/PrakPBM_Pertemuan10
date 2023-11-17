package com.mazizs.susunkata.data

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

//Daftar dengan semua kata untuk Game
val allWords: Set<String> =
    setOf(
        "at",
        "sea",
        "home",
        "arise",
        "banana",
        "android",
        "birthday",
        "briefcase",
        "motorcycle",
        "cauliflower"
    )

/**
 * Memetakan kata-kata sesuai panjangnya. Setiap kata di semua Kata memiliki panjang yang unik. Hal ini diperlukan karena
 * kata-kata dipilih secara acak di dalam GameViewModel dan pemilihannya tidak dapat diprediksi
 */
private val wordLengthMap: Map<Int, String> = allWords.associateBy({ it.length }, { it })

internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""
