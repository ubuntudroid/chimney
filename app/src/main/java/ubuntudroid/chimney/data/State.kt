package ubuntudroid.chimney.data

data class State<out T>(
        val value: T?,
        val error: String? = null // might become more sophisticated in the future
) {
    fun isSuccessful(): Boolean = error == null && value != null
}