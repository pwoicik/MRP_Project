package domain.model

data class Component(
    val id: Long,
    val name: String,
    val leadTime: Int,
    val batchSize: Int,
    val inStock: Int
)
