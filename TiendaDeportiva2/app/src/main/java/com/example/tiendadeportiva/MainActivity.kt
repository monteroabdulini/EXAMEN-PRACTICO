package com.example.tiendadeportiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tiendadeportiva.ui.TiendaDeportivaApp
import com.example.tiendadeportiva.ui.theme.TiendaDeportivaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaDeportivaTheme {
                TiendaDeportivaApp()
            }
        }
    }
}
