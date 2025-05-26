package uz.gka.tapyoutest.presentation.result

sealed class ResultEffect {

    data object Saved : ResultEffect()
    data class SavedIn(val savedPath: String) : ResultEffect()
    data object SaveError : ResultEffect()
    data object MemoryAccessError : ResultEffect()


}