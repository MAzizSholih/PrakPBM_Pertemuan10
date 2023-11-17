package com.mazizs.susunkata.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazizs.susunkata.R
import com.mazizs.susunkata.ui.theme.SusunKataTheme

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi GameScreen yaitu untuk menampilkam layar permainan Susun Kata
@Composable
//Parameter gameViewModel merupakan instance dari kelas GameViewModel, fungsi tersebut menggunakan viewModel() untuk mendapatkan instance dari GameViewModel
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState() //Untuk melakukan perubahan UI pada ViewModel
    val mediumPadding = dimensionResource(R.dimen.padding_medium) //Untuk mndapatkan nilai mediumPadding dari R.dimensi

    Column( //Untuk mengeatur tampilan elemen tata letak bentuk kolom secara vertikal
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text( //Untuk menampilkan teks judul permainan Susun Kata
            text = stringResource(R.string.app_name),
            style = typography.titleLarge,
        )

        GameLayout( //GameLayout akan memanggil atau menggunakan Composable fun GameLayout
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) }, //Untuk menetapkan sebuah fungsi yang akan dipanggil ketika input pengguna berubah
            wordCount = gameUiState.JumlahKatasaatini, //Untuk menetapkan jumlah kata yang benar saat ini ke variabel wordCount
            userGuess = gameViewModel.userGuess, //Untuk nemberikan dan menampilkan nilai tebakan pengguna dari gameViewModel ke variabel userGuess
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            currentScrambledWord = gameUiState.KataAcaksaatini, //Untuk menetapkan dan menampilkan kata yang diacak saat ini dari gameUiState ke variabel currentScrambledWord
            isGuessWrong = gameUiState.TebakanKatayangSalah, //Umtuk menampilkan dan menetapkan nilai TebakanKatayangSalah dari gameUiState ke variabel isGuessWrong
            modifier = Modifier //Untuk modifikasi dan untuk mengatur lebar, tinggi, dan padding
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )

        Column( //Untuk mengeatur tampilan elemen tata letak bentuk kolom secara vertikal
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button( //Untuk mengklik tombol Submit
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.checkUserGuess() }
            ) {
                Text( //Untuk menampilkan teks tombol Submit
                    text = stringResource(R.string.submit),
                    fontSize = 16.sp
                )
            }

            OutlinedButton( //Untuk mmengklik tombol lewati
                onClick = { gameViewModel.skipWord() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text( //Untuk menampilkan teks tombol Lewati
                    text = stringResource(R.string.lewati),
                    fontSize = 16.sp
                )
            }
        }

        //Untuk menampilkan skor yang berasal dari state skor dalam gameUiState
        GameStatus(score = gameUiState.skor, modifier = Modifier.padding(20.dp))
        //Umtuk mengevaluasi kondisi apakah permainan sudah berakhir atau belum, jika PermainanBerakhir maka dalam gameUiState bernilai true dan blok berikutnya akan dieksekusi.
        if (gameUiState.PermainanBerakhir) {
            FinalScoreDialog( //Untuk menampilkan dialog skor akhir jika permainan sudah berakhir.
                score = gameUiState.skor,
                onPlayAgain = { gameViewModel.resetGame() } //Untuk menetapkan sebuah fungsi yang akan dijalankan ketika tombol Main Lagi diklik
            )
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi GameStatus yaitu untuk menampilkan status skor permainan
@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card( //Untuk membuat seperti bentuk kartu, berfungsi untuk menamplkan informasi skor
        modifier = modifier
    ) {
        Text( //Untuk menampilkan teks skor
            text = stringResource(R.string.skor, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi GameLayout yaitu untuk menampilkan tata letak permainan
@Composable
fun GameLayout(
    currentScrambledWord: String,
    wordCount: Int,
    isGuessWrong: Boolean,
    userGuess: String,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Mendapatkan nilai padding medium dari R.dimensi
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Card( //Untuk wadah utama dengan elevasi dan padding tertentu di dalam Card
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column( //Untuk mengatur elemen-elemen dalam tata letak secara vertikal
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text( //Untuk menampilkan teks jumlah kata yang benar saat ini
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = stringResource(R.string.jumlah_kata, wordCount),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )
            Text( //Untuk enampilkan teks kata yang diacak saat ini
                text = currentScrambledWord,
                style = typography.displayMedium
            )
            Text( //Untuk menampilkan teks instruksi permainan dengan
                text = stringResource(R.string.instructions),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            OutlinedTextField( //Untuk menampilkan OutlinedTextField
                value = userGuess,
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                onValueChange = onUserGuessChanged,
                label = { //Untuk menetapkan label inputan sesuai kondisi tebakan benar atau salah
                    if (isGuessWrong) {
                        Text(stringResource(R.string.tebakan_salah))
                    } else {
                        Text(stringResource(R.string.masukan_kata_anda))
                    }
                },
                isError = isGuessWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi FinalScoreDialog yaitu untuk menampilkan dialog dengan skor akhir
@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    //Mendapatkan instance Activity dari LocalContext
    val activity = (LocalContext.current as Activity)

    AlertDialog( //Untuk mrmbuat AlertDialog skor akhir
        onDismissRequest = { //Untuk menutup dialog ketika pengguna mengklik di luar dialog atau tombol kembali
        },
        title = { Text(text = stringResource(R.string.selamat)) },
        text = { Text(text = stringResource(R.string.skor_anda, score)) },
        modifier = modifier,
        dismissButton = { ///Tombol untuk menutup aplikasi saat diklik
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.keluar))
            }
        },
        confirmButton = { //Tombol untuk memulai permainan lagi saat diklik
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.main_lagi))
            }
        }
    )
}

//Fungsi di bawah ini adalah komponen Composable yang digunakan untuk menampilkan preview atau pratinjau
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    SusunKataTheme {
        GameScreen()
    }
}