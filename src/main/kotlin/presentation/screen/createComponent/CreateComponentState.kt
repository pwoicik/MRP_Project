package presentation.screen.createComponent

import androidx.compose.runtime.Immutable

@Immutable
data class CreateComponentState(
    val name: String = "",
    val leadTime: String = "",
    val inStock: String = "",
    val batchSize: String = "",
    val requiredAmount: String = ""
) {

    val isInputValid: Boolean
        get() = name.isNotBlank()
                && leadTime.isNotBlank()
                && inStock.isNotBlank()
                && batchSize.isNotBlank()
                && requiredAmount.isNotBlank()
}
