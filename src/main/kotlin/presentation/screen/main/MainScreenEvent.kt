package presentation.screen.main

import data.entity.ProductEntity
import domain.model.Component

sealed class MainScreenEvent {

    data class ProductSelected(val product: ProductEntity) : MainScreenEvent()
    data class DeleteProduct(val product: ProductEntity) : MainScreenEvent()
    object AddComponent : MainScreenEvent()

    data class DeleteComponent(val component: Component) : MainScreenEvent()
    data class EditComponent(val component: Component) : MainScreenEvent()
    data class AddSubcomponent(val parent: Component) : MainScreenEvent()
}
