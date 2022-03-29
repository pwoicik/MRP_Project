package presentation.screen.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig
import presentation.navigation.ViewModel

class MainViewModel(
    componentContext: ComponentContext,
    private val navController: NavController
) : ViewModel(componentContext) {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        repository.getAllProducts().onEach { products ->
            _state.update { state ->
                state.copy(products = products)
            }
        }.launchIn(viewModelScope)
    }

    fun emit(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.ProductSelected -> _state.update {
                it.copy(selectedProductId = event.product.id)
            }
            is MainScreenEvent.DeleteProduct -> viewModelScope.launch {
                if (event.product.id == state.value.selectedProductId) {
                    _state.update { it.copy(selectedProductId = null) }
                }
                repository.deleteProduct(event.product.id)
            }
            is MainScreenEvent.AddComponent -> {
                navController.navigate(ScreenConfig.CreateComponent(productId = state.value.selectedProductId!!))
            }
            is MainScreenEvent.DeleteComponent -> viewModelScope.launch {
                val selectedProduct = state.value.selectedProduct!!
                val components = selectedProduct.components.toMutableList()
                val i = components.indexOf(event.component)
                components.removeAt(i)
                while (components.isNotEmpty()) {
                    if (
                        i > components.lastIndex
                        || components[i].bom <= event.component.bom
                    ) break
                    components.removeAt(i)
                }
                repository.updateProduct(selectedProduct.copy(components = components))
            }
            is MainScreenEvent.EditComponent -> {
                val productId = state.value.selectedProductId!!
                val product = state.value.selectedProduct!!
                navController.navigate(
                    ScreenConfig.CreateComponent(
                        productId = productId,
                        atIndex = product.components.indexOf(event.component),
                        editMode = true
                    )
                )
            }
            is MainScreenEvent.AddSubcomponent -> {
                val productId = state.value.selectedProductId!!
                val product = state.value.selectedProduct!!
                navController.navigate(
                    ScreenConfig.CreateComponent(
                        productId = productId,
                        atIndex = product.components.indexOf(event.parent)
                    )
                )
            }
        }
    }

    @Composable
    override fun render() {
        MainScreen(
            viewModel = this,
            navController = navController
        )
    }
}
