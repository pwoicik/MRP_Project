package presentation.screen.product

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import presentation.navigation.Component
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

class ProductComponent(
    componentComponent: ComponentContext,
    config: ScreenConfig.Product,
    private val navController: NavController
) : Component(componentComponent) {

    private val productId = config.productId

    private val productWithComponents = repository.getProductWithComponents(productId)
        .stateIn(componentScope, SharingStarted.WhileSubscribed(), null)

    private fun onDeleteProduct() {
        componentScope.launch {
            repository.deleteProduct(productId)
            navController.navigateUp()
        }
    }

    @Composable
    override fun render() {
        ProductScreen(
            productWithComponentsStateFlow = productWithComponents,
            onGoBack = navController::navigateUp,
            onDeleteProduct = ::onDeleteProduct,
            onEditProduct = { navController.navigate(ScreenConfig.CreateComponent(productId = productId)) }
        )
    }
}
