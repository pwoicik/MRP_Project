package presentation.screen.createProduct

import androidx.compose.runtime.Immutable

@Immutable
data class CreateProductState(
    val name: String = "",
    val leadTime: String = "",
    val inStock: String = ""
) {

    val isInputValid: Boolean
        get() = name.isNotBlank() && leadTime.isNotBlank() && inStock.isNotBlank()
}
