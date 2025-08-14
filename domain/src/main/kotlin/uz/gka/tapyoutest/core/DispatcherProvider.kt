package uz.gka.tapyoutest.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction over [CoroutineDispatcher]s to allow easy testing and flexibility.
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}
