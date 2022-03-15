package presentation.screen.products

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import presentation.navigation.ViewModel
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

class ProductsViewModel(
    componentContext: ComponentContext,
    private val navController: NavController,
) : ViewModel(componentContext) {

    private val products = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @Composable
    override fun render() {
        ProductsScreen(
            productsFlow = products,
            onGoToProduct = {
                navController.navigate(ScreenConfig.Product(it.id))
                            },
            onCreateProduct = {
                navController.navigate(ScreenConfig.CreateComponent())
                              },
            onDeleteProductClick = {
                viewModelScope.launch {
                    repository.deleteProduct(it.id)
                }
            },
            onEditProduct = {
                navController.navigate(ScreenConfig.CreateComponent(productId = it.id))
            }
        )
    }
}
