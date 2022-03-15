package presentation.screen.createComponent

data class CreateComponentState(
    val name: String = "",
    val leadTime: String = "",
    val batchSize: String = "",
    val inStock: String = ""
) {

    val isInputValid: Boolean
        get() = name.isNotBlank() && leadTime.isNotBlank() && batchSize.isNotBlank() && inStock.isNotBlank()
}
