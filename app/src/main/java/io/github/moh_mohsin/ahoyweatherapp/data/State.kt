package io.github.moh_mohsin.ahoyweatherapp.data


sealed class State<out T> {
    data class Data<T>(val data: T) : State<T>()
    data class Error(val message: Message) : State<Nothing>()
    object Loading : State<Nothing>()
    object Idle : State<Nothing>()
}

fun <T, R> State<List<T>>.map(transform: (T) -> R): State<List<R>> {
    return when (this) {
        is State.Data -> State.Data(data.map(transform))
        is State.Error -> State.Error(message)
        State.Idle -> State.Idle
        State.Loading -> State.Loading
    }
}