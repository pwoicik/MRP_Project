package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Component(
    val name: String,
    val leadTime: Long,
    val inStock: Long,
    val batchSize: Long = 0,
    val requiredAmount: Long = 1,
    val bom: Long
)
