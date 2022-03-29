package presentation.navigation

import com.arkivanov.essenty.parcelable.Parcelable

sealed class ScreenConfig : Parcelable {

    object Main : ScreenConfig()

    data class CreateProduct(val productId: Long? = null) : ScreenConfig()

    data class CreateComponent(
        val productId: Long,
        val atIndex: Int? = null,
        val editMode: Boolean = false
    ) : ScreenConfig()

    data class Mrp(val productId: Long) : ScreenConfig()
}
