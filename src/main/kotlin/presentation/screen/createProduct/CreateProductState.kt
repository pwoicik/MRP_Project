package presentation.screen.createProduct

data class CreateProductState(
    val name: String = "",
    val leadTime: String = "",
    val batchSize: String = "",
    val inStock: String = ""
) {

    val isInputValid: Boolean
        get() = name.isNotBlank() && leadTime.isNotBlank() && batchSize.isNotBlank() && inStock.isNotBlank()
}
