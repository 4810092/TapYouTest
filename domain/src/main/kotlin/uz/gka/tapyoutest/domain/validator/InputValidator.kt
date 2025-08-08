package uz.gka.tapyoutest.domain.validator

import javax.inject.Inject

class InputValidator @Inject constructor() {
    fun validateCountInput(input: String?): ValidationResult {
        val value = input?.toIntOrNull()
        return when {
            value == null -> ValidationResult.Invalid
            value <= 0 -> ValidationResult.Invalid
            else -> ValidationResult.Valid(value)
        }
    }

    sealed class ValidationResult {
        data class Valid(val value: Int) : ValidationResult()
        data object Invalid : ValidationResult()
    }
}
