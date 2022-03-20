package presentation.screen.createComponent

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import data.entity.ProductTree
import domain.model.MutableProductTreeNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.navigation.ViewModel
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

class CreateComponentViewModel(
    componentContext: ComponentContext,
    private val config: ScreenConfig.CreateComponent,
    private val navController: NavController
) : ViewModel(componentContext) {

    private val _state = MutableStateFlow(CreateComponentState())
    val state = _state.asStateFlow()

    private lateinit var product: ProductTree

    init {
        if (config.productId != null) {
            viewModelScope.launch {
                product = repository.getProduct(config.productId).first()
                _state.update {
                    val product = product.node
                    CreateComponentState(
                        name = product.name,
                        leadTime = product.leadTime.toString(),
                        batchSize = product.batchSize.toString(),
                        inStock = product.inStock.toString()
                    )
                }
            }
        }
    }

    fun emit(event: CreateComponentEvent) {
        when (event) {
            is CreateComponentEvent.BatchSizeChanged -> {
                _state.update { state ->
                    state.copy(batchSize = event.batchSize.toDigitsOnly())
                }
            }
            is CreateComponentEvent.InStockChanged -> {
                _state.update { state ->
                    state.copy(inStock = event.inStock.toDigitsOnly())
                }
            }
            is CreateComponentEvent.LeadTimeChanged -> {
                _state.update { state ->
                    state.copy(leadTime = event.leadTime.toDigitsOnly())
                }
            }
            is CreateComponentEvent.NameChanged -> {
                _state.update { state ->
                    state.copy(name = event.name)
                }
            }
            CreateComponentEvent.SaveComponent -> viewModelScope.launch {
                state.value.let {
                    if (!it.isInputValid) return@launch
                    if (config.productId == null) {
                        repository.insertProduct(
                            MutableProductTreeNode(
                                name = it.name,
                                leadTime = it.leadTime.toLong(),
                                batchSize = it.batchSize.toLong(),
                                inStock = it.inStock.toLong()
                            )
                        )
                    } else {
                        product.node.apply {
                            name = it.name
                            leadTime = it.leadTime.toLong()
                            batchSize = it.batchSize.toLong()
                            inStock = it.inStock.toLong()
                        }
                        repository.updateProduct(product)
                    }
                }
            }
        }
    }

    private fun String.toDigitsOnly() = replace("""\D""".toRegex(), "")

    @Composable
    override fun render() {
        CreateComponentScreen(
            viewModel = this,
            navController = navController
        )
    }
}
