package ubuntudroid.chimney.data

data class Resource<out T>(
        val status: Status,
        val data: T?
) {

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Success, data)
        fun <T> error(message: String): Resource<T> = Resource(Error(message), null)
        fun <T> loading(data: T? = null): Resource<T> = Resource(Loading, data)
    }
}

sealed class Status

object Success: Status()
data class Error(val message: String): Status()
object Loading: Status()

