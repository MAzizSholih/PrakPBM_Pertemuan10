package com.mazizs.susunkata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mazizs.susunkata.ui.GameScreen
import com.mazizs.susunkata.ui.theme.SusunKataTheme

//Fungsi onCreate ini digunakan untuk mengedit tampilan aktivitas utama dengan menggunakan komponen UI yang telah didefinisikan dalam GameScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SusunKataTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}
