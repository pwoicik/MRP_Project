package presentation.screen.createComponent

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import data.entity.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.navigation.Component
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig

// TODO: Updating an existing product doesn't work, instead it creates a new one
class CreateComponentComponent(
    componentContext: ComponentContext,
    private val config: ScreenConfig.CreateComponent,
    private val navController: NavController
) : Component(componentContext) {

    private val _state = MutableStateFlow(CreateComponentState())
    private val state = _state.asStateFlow()

    init {
        if (config.productId != null) {
            componentScope.launch {
                val product = repository.getProduct(config.productId)
                _state.update {
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

    private fun emit(event: CreateComponentEvent) {
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
            CreateComponentEvent.GoBack -> { navController.navigateUp() }
            CreateComponentEvent.SaveComponent -> componentScope.launch {
                state.value.run {
                    if (isInputValid) {
                        if (config.productId == null) {
                            repository.insertProduct(
                                name = name,
                                leadTime = leadTime.toLong(),
                                batchSize = batchSize.toLong(),
                                inStock = inStock.toLong()
                            )
                        } else {
                            repository.insertProduct(
                                ProductEntity(
                                    id = config.productId,
                                    name = name,
                                    leadTime = leadTime.toLong(),
                                    batchSize = batchSize.toLong(),
                                    inStock = inStock.toLong(),
                                    bom = config.bom.toLong(),
                                    parent = config.parent
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun String.toDigitsOnly() = replace("""\D""".toRegex(), "")

    @Composable
    override fun render() {
        CreateComponentScreen(
            stateFlow = state,
            emit = ::emit
        )
    }
}
