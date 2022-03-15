package presentation.screen.product

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import presentation.navigation.ViewModel
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

class ProductViewModel(
    componentComponent: ComponentContext,
    config: ScreenConfig.Product,
    private val navController: NavController
) : ViewModel(componentComponent) {

    private val productId = config.productId

    private val product = repository.getProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private fun onDeleteProduct() {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            navController.navigateUp()
        }
    }

    @Composable
    override fun render() {
        ProductScreen(
            productFlow = product,
            onGoBack = navController::navigateUp,
            onDeleteProduct = ::onDeleteProduct,
            onEditProduct = { navController.navigate(ScreenConfig.CreateComponent(productId)) }
        )
    }
}
