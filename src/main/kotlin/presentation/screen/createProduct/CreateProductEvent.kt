package presentation.screen.createProduct

sealed class CreateProductEvent {
    data class NameChanged(val name: String) : CreateProductEvent()
    data class LeadTimeChanged(val leadTime: String) : CreateProductEvent()
    data class InStockChanged(val inStock: String) : CreateProductEvent()

    object SaveProduct : CreateProductEvent()
}
