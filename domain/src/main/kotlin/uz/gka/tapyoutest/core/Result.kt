package uz.gka.tapyoutest.core

/**
 * Represents a value with two possible types of results: success or failure.
 */
sealed interface Result<out T> {
    /** Successful result containing [data]. */
    data class Success<T>(val data: T) : Result<T>

    /** Failed result containing the [throwable] that caused it. */
    data class Error(val throwable: Throwable) : Result<Nothing>
}
