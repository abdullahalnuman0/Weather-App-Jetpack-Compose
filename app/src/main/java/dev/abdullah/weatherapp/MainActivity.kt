package dev.abdullah.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import dev.abdullah.weatherapp.page.WeatherPage
import dev.abdullah.weatherapp.ui.theme.WeatherAppTheme
import dev.abdullah.weatherapp.viewmodels.WeatherViewModel

//TODO: Project Graph: https://app.eraser.io/workspace/8W2txoubTrQhdbnUdQKU?origin=share

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class]

        setContent {
            WeatherAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    WeatherPage(
                        modifier = Modifier.padding(innerPadding),
                        weatherViewModel
                    )
                }
            }
        }
    }
}
