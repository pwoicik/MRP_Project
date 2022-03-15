package presentation.screen.products

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import presentation.navigation.Component
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

class ProductsComponent(
    componentContext: ComponentContext,
    private val navController: NavController,
) : Component(componentContext) {

    private val products = repository.getAllTopLevelProducts()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(), emptyList())

    @Composable
    override fun render() {
        ProductsScreen(
            products = products,
            onGoToProduct = { navController.navigate(ScreenConfig.Product(it.id)) },
            onCreateProduct = { navController.navigate(ScreenConfig.CreateComponent()) },
            onEditProduct = { navController.navigate(ScreenConfig.CreateComponent(productId = it.id)) }
        )
    }
}
