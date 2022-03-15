package presentation.screen.createComponent

sealed class CreateComponentEvent {
    data class NameChanged(val name: String) : CreateComponentEvent()
    data class LeadTimeChanged(val leadTime: String) : CreateComponentEvent()
    data class BatchSizeChanged(val batchSize: String) : CreateComponentEvent()
    data class InStockChanged(val inStock: String) : CreateComponentEvent()

    object GoBack : CreateComponentEvent()
    object SaveComponent : CreateComponentEvent()
}
