package presentation.screen.main

import data.entity.ProductTree

sealed class MainScreenEvent {

    data class ProductSelected(val product: ProductTree) : MainScreenEvent()
    data class DeleteProduct(val product: ProductTree) : MainScreenEvent()
}
