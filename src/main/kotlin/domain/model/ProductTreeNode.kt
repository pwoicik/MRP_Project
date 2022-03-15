package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MutableProductTreeNode(
    var name: String,
    var leadTime: Int,
    var batchSize: Int,
    var inStock: Int,
    var bom: Int = 0,
    val components: MutableList<MutableProductTreeNode> = mutableListOf()
)
