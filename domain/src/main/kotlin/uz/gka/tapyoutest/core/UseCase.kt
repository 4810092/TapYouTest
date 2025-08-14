package uz.gka.tapyoutest.core

import kotlinx.coroutines.withContext

/**
 * Base class for use cases. It ensures that [run] is executed on the IO dispatcher
 * and provides an operator function for concise invocation.
 */
abstract class UseCase<in P, R>(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(param: P): R = execute(param)

    protected abstract suspend fun run(param: P): R

    private suspend fun execute(param: P): R =
        withContext(dispatcherProvider.io) {
            run(param)
        }
}
