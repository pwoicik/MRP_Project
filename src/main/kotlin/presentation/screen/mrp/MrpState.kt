package presentation.screen.mrp

import androidx.compose.runtime.Immutable
import data.entity.ProductEntity
import domain.model.GHP
import domain.model.MRP

@Immutable
data class MrpState(

    val product: ProductEntity? = null,
    val ghp: GHP? = null,
    val mrps: List<List<MRP>>? = null
)
