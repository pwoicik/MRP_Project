package presentation.screen.createComponent

sealed class CreateComponentEvent {
    data class NameChanged(val name: String) : CreateComponentEvent()
    data class LeadTimeChanged(val leadTime: String) : CreateComponentEvent()
    data class InStockChanged(val inStock: String) : CreateComponentEvent()
    data class BatchSizeChanged(val batchSize: String) : CreateComponentEvent()
    data class RequiredAmountChanged(val requiredAmount: String) : CreateComponentEvent()

    object SaveComponent : CreateComponentEvent()
}
