package com.mazizs.susunkata.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mazizs.susunkata.data.MAX_NO_OF_WORDS
import com.mazizs.susunkata.data.SCORE_INCREASE
import com.mazizs.susunkata.data.semuaKata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//Pada ViewModel berisikan data aplikasi dan metode untuk memproses data
class GameViewModel : ViewModel() {

    //Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    //Kumpulan kata-kata yang digunakan dalam permainan
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    //Inisialisasi ulang data game untuk memulai ulang game
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(KataAcaksaatini = pickRandomWordAndShuffle())
    }

    //Memperbarui tebakan pengguna
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    //Mengecek apakah tebakan pengguna benar
    //Meningkatkan skor yang sesuai
    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            //Tebakan pengguna benar, maka skornya akan bertambah dan memanggil updateGameState() untuk mempersiapkan game untuk putaran berikutnya
            val updatedScore = _uiState.value.skor.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            //Tebakan pengguna salah, maka akan menampilkan kesalahan
            _uiState.update { currentState ->
                currentState.copy(TebakanKatayangSalah = true)
            }
        }
        //Setel ulang tebakan pengguna
        updateUserGuess("")
    }

    //Lewati ke kata berikutnya jika tidak bisa menjawab
    fun skipWord() {
        updateGameState(_uiState.value.skor)
        //Setel ulang tebakan pengguna
        updateUserGuess("")
    }

    //Memilih JumlahKatasaatini dan KataAcaksaatini baru dan memperbarui UiState sesuai dengan status game saat ini.
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS){
            //Putaran terakhir dalam game
            _uiState.update { currentState ->
                currentState.copy(
                    TebakanKatayangSalah = false,
                    skor = updatedScore,
                    PermainanBerakhir = true
                )
            }
        } else{
            //Putaran normal dalam permainan
            _uiState.update { currentState ->
                currentState.copy(
                    TebakanKatayangSalah = false,
                    KataAcaksaatini = pickRandomWordAndShuffle(),
                    JumlahKatasaatini = currentState.JumlahKatasaatini.inc(),
                    skor = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // engacak kata
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        //Melanjutkan mengambil kata acak baru hingga mendapatkan kata yang belum pernah digunakan sebelumnya
        currentWord = semuaKata.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }
}