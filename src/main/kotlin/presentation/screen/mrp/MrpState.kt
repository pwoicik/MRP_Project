package presentation.screen.mrp

import androidx.compose.runtime.Immutable
import data.entity.ProductEntity

@Immutable
data class MrpState(
    val product: ProductEntity? = null
)
