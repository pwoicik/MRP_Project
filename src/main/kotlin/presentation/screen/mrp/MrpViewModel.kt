package presentation.screen.mrp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig
import presentation.navigation.ViewModel

class MrpViewModel(
    componentContext: ComponentContext,
    config: ScreenConfig.Mrp,
    private val navController: NavController
) : ViewModel(componentContext) {

    private val productId = config.productId

    private val _state = MutableStateFlow(MrpState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val product = repository.getProduct(productId).first()
            _state.update {
                it.copy(product = product)
            }
        }
    }

    @Composable
    override fun render() {
        MrpScreen(
            viewModel = this,
            navController = navController
        )
    }
}
