package presentation.navigation

import com.arkivanov.essenty.parcelable.Parcelable

sealed class ScreenConfig : Parcelable {

    object Products : ScreenConfig()

    data class Product(val productId: Long) : ScreenConfig()

    data class CreateComponent(val productId: Long? = null) : ScreenConfig()
}
