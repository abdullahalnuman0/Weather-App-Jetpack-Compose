package dev.abdullah.weatherapp.api.response

///T refers to Weather Model
/// This System '<T>' any request usable
sealed class NetworkResponse<out T> {

    data class Success<out T>(val data: T) : NetworkResponse<T>()

    data class Error(val message: String) : NetworkResponse<Nothing>()

    object Loading : NetworkResponse<Nothing>()

}