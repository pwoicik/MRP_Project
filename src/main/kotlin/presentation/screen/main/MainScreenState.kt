package presentation.screen.main

import data.entity.ProductEntity

data class MainScreenState(

    val products: List<ProductEntity>? = null,
    val selectedProductId: Long? = null
) {

    val selectedProduct: ProductEntity?
        get() = products?.find { it.id == selectedProductId }
}
