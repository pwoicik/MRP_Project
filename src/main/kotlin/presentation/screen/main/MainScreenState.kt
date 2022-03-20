package presentation.screen.main

import data.entity.ProductTree

data class MainScreenState(
    val products: List<ProductTree>? = null,
    val selectedProduct: ProductTree? = null
)
