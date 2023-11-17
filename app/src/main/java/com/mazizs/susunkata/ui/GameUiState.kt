package com.mazizs.susunkata.ui

//Kelas data yang mewakili status UI game
data class GameUiState(
    val KataAcaksaatini: String = "",
    val JumlahKatasaatini: Int = 1,
    val skor: Int = 0,
    val TebakanKatayangSalah: Boolean = false,
    val PermainanBerakhir: Boolean = false
)