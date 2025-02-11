package dev.abdullah.weatherapp.page

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.abdullah.weatherapp.api.model.WeatherModel
import dev.abdullah.weatherapp.api.response.NetworkResponse
import dev.abdullah.weatherapp.viewmodels.WeatherViewModel

@Composable
fun WeatherPage(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel
) {

    var city by remember { mutableStateOf("Sylhet") }

    val weatherResult = viewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = false) {
        viewModel.getData(city)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchSectionCompose(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            city = city,
            onTextChange = { city = it },
            onSearchIconClick = {
                viewModel.getData(city)
                keyboardController?.hide()
            }
        )

        when (val result = weatherResult.value) {

            is NetworkResponse.Error -> {
                Text(result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                WeatherSuccessDetailsCompose(data = result.data)
            }

            null -> {}
        }

    }

}

@Composable
fun SearchSectionCompose(
    modifier: Modifier = Modifier,
    city: String,
    onTextChange: (city: String) -> Unit,
    onSearchIconClick: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = city,
            onValueChange = {
                onTextChange(it)
            },
            maxLines = 2,
            label = {
                Text("Search for any location")
            }
        )
        IconButton(onClick = onSearchIconClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }

    }
}


@Composable
fun WeatherSuccessDetailsCompose(
    modifier: Modifier = Modifier,
    data: WeatherModel
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LocationShowComposable(
            locationName = data.location.name,
            countryName = data.location.country
        )
        Spacer(Modifier.height(18.dp))

        MainResultComposable(data = data)

        Spacer(Modifier.height(16.dp))

        OthersResultComposable(data = data)


    }

}

@Composable
fun LocationShowComposable(
    modifier: Modifier = Modifier,
    locationName: String,
    countryName: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location icon",
            modifier = Modifier
                .size(40.dp)
        )

        Text(
            text = locationName,
            fontSize = 30.sp
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = countryName,
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}


@Composable
fun MainResultComposable(modifier: Modifier = Modifier, data: WeatherModel) {

    Card(
        modifier = modifier
            .fillMaxWidth(),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${data.current.temp_c} °C",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            AsyncImage(
                modifier = Modifier.size(160.dp),
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = "Condition icon"
            )

            Text(
                text = data.current.condition.text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }

}


@Composable
fun OthersResultComposable(modifier: Modifier = Modifier, data: WeatherModel) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "Humidity", value = data.current.humidity.toString())
                WeatherKeyVal(
                    key = "Wind Speed (km/h)",
                    value = data.current.wind_kph.toString()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "UV", value = data.current.uv.toString())
                WeatherKeyVal(
                    key = "Participation (mm)",
                    value = data.current.precip_mm.toString()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "Local Time", value = data.location.localtime.split(" ")[1])
                WeatherKeyVal(key = "Local Date", value = data.location.localtime.split(" ")[0])
            }


        }
    }
}

@Composable
fun WeatherKeyVal(
    modifier: Modifier = Modifier,
    key: String, value: String
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = key,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}









