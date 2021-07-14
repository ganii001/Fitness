import com.example.fitness.network.resource.Status


data class DataState<out T>(
    val status: Status,
    val data: T?,
    val message: String?,

    ) {
    companion object {
        fun <T> success(data: T?): DataState<T> {
            return DataState(Status.SUCCESS, data, null)
        }

        fun <T> error(data: T?, message: String): DataState<T> =
            DataState(Status.ERROR, data, message)

        fun <T> loading(data: T?): DataState<T> {
            return DataState(Status.LOADING, data, null)
        }
    }
}
