package com.mazizs.susunkata.ui.test

import com.mazizs.susunkata.data.MAX_NO_OF_WORDS
import com.mazizs.susunkata.data.SCORE_INCREASE
import com.mazizs.susunkata.data.getUnscrambledWord
import com.mazizs.susunkata.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        /**
         *  Peringatan: Cara mengambil uiState ini berfungsi karena MutableStateFlow digunakan. Dalam
         *  unit mendatang akan belajar tentang penggunaan lanjutan StateFlow yang menciptakan aliran
         *  data dan perlu bereaksi untuk menangani aliran tersebut. Untuk skenario tersebut akan menulis
         *  pengujian unit menggunakan metode/pendekatan yang berbeda. Ini berlaku untuk semua penggunaan
         *  viewModel.uiState.value di kelas ini.
         **/
        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.KataAcaksaatini)

        //Menegaskan bahwa kata saat ini diacak
        assertNotEquals(unScrambledWord, gameUiState.KataAcaksaatini)
        //Menegaskan bahwa jumlah kata saat ini disetel ke 1
        assertTrue(gameUiState.JumlahKatasaatini == 1)
        //Menegaskan bahwa skor awalnya adalah 0
        assertTrue(gameUiState.skor == 0)
        //Menegaskan bahwa tebakan kata yang salah adalah salah
        assertFalse(gameUiState.TebakanKatayangSalah)
        //Menegaskan bahwa permainan belum berakhir
        assertFalse(gameUiState.PermainanBerakhir)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        //Memberi kata yang salah sebagai masukan
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        //Menegaskan bahwa skor tidak berubah
        assertEquals(0, currentGameUiState.skor)
        //Menegaskan bahwa metode checkUserGuess() memperbarui isGuesssedWordWrong dengan benar
        assertTrue(currentGameUiState.TebakanKatayangSalah)
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.KataAcaksaatini)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value

        //Menegaskan bahwa pembaruan metode checkUserGuess() isGuesssedWordWrong telah diperbarui dengan benar
        assertFalse(currentGameUiState.TebakanKatayangSalah)
        //Menegaskan bahwa skor diperbarui dengan benar
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.skor)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.KataAcaksaatini)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.JumlahKatasaatini

        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value
        //Menegaskan bahwa skor tetap tidak berubah setelah kata dilewati
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.skor)
        //Menegaskan bahwa jumlah kata bertambah 1 setelah kata dilewati
        assertEquals(lastWordCount + 1, currentGameUiState.JumlahKatasaatini)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.KataAcaksaatini)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.KataAcaksaatini)
            //Menegaskan bahwa setelah setiap jawaban benar, skor diperbarui dengan benar
            assertEquals(expectedScore, currentGameUiState.skor)
        }
        //Menegaskan bahwa setelah semua pertanyaan dijawab, jumlah kata saat ini adalah yang terbaru
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.JumlahKatasaatini)
        //Menegaskan bahwa setelah 10 pertanyaan terjawab, permainan selesai
        assertTrue(currentGameUiState.PermainanBerakhir)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}
