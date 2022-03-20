package presentation.screen.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.navigation.NavController
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
            is MainScreenEvent.ProductSelected -> {
                _state.update { state ->
                    state.copy(selectedProduct = event.product)
                }
            }
            is MainScreenEvent.DeleteProduct -> viewModelScope.launch {
                if (event.product == state.value.selectedProduct) {
                    _state.update { state ->
                        state.copy(selectedProduct = null)
                    }
                }
                repository.deleteProduct(event.product.id)
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
