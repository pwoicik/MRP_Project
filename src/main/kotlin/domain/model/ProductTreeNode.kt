package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MutableProductTreeNode(
    var name: String,
    var leadTime: Long,
    var inStock: Long,
    var batchSize: Long = 0,
    var bom: Long = 0,
    var requiredAmount: Long = 1,
    val components: MutableList<MutableProductTreeNode> = mutableListOf()
)
