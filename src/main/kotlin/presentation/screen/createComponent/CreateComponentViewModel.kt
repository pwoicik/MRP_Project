package presentation.screen.createComponent

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import data.entity.ProductEntity
import domain.model.Component
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig
import presentation.navigation.ViewModel
import util.toDigitsOnly

class CreateComponentViewModel(
    componentContext: ComponentContext,
    private val config: ScreenConfig.CreateComponent,
    private val navController: NavController
) : ViewModel(componentContext) {

    val isEditMode = config.editMode

    private val _state = MutableStateFlow(CreateComponentState())
    val state = _state.asStateFlow()

    private lateinit var product: ProductEntity

    init {
        viewModelScope.launch {
            product = repository.getProduct(config.productId).first()
            if (!isEditMode || config.atIndex == null) return@launch
            val (name, leadTime, inStock, batchSize, requiredAmount) = product.components[config.atIndex]
            _state.update {
                it.copy(
                    name = name,
                    leadTime = leadTime.toString(),
                    inStock = inStock.toString(),
                    batchSize = batchSize.toString(),
                    requiredAmount = requiredAmount.toString()
                )
            }
        }
    }

    fun emit(event: CreateComponentEvent) {
        when (event) {
            is CreateComponentEvent.BatchSizeChanged -> _state.update {
                it.copy(batchSize = event.batchSize.toDigitsOnly())
            }
            is CreateComponentEvent.InStockChanged -> _state.update {
                it.copy(inStock = event.inStock.toDigitsOnly())
            }
            is CreateComponentEvent.LeadTimeChanged -> _state.update {
                it.copy(leadTime = event.leadTime.toDigitsOnly())
            }
            is CreateComponentEvent.NameChanged -> _state.update {
                it.copy(name = event.name)
            }
            is CreateComponentEvent.RequiredAmountChanged -> _state.update {
                it.copy(requiredAmount = event.requiredAmount.toDigitsOnly())
            }
            CreateComponentEvent.SaveComponent -> viewModelScope.launch {
                state.value.let {
                    if (!it.isInputValid) return@launch

                    val components = product.components.toMutableList()
                    if (config.atIndex == null) {
                        val newComponent = Component(
                            name = it.name,
                            leadTime = it.leadTime.toLong(),
                            inStock = it.inStock.toLong(),
                            batchSize = it.batchSize.toLong(),
                            requiredAmount = it.requiredAmount.toLong(),
                            bom = 1
                        )
                        components.add(0, newComponent)
                    } else {
                        val componentAtIndex = product.components[config.atIndex]
                        if (isEditMode) {
                            components[config.atIndex] = componentAtIndex.copy(
                                name = it.name,
                                leadTime = it.leadTime.toLong(),
                                inStock = it.inStock.toLong(),
                                batchSize = it.batchSize.toLong(),
                                requiredAmount = it.requiredAmount.toLong()
                            )
                        } else {
                            val newComponent = Component(
                                name = it.name,
                                leadTime = it.leadTime.toLong(),
                                inStock = it.inStock.toLong(),
                                batchSize = it.batchSize.toLong(),
                                requiredAmount = it.requiredAmount.toLong(),
                                bom = componentAtIndex.bom + 1
                            )
                            components.add(config.atIndex + 1, newComponent)
                        }
                    }
                    repository.updateProduct(
                        product.copy(
                            components = components
                        )
                    )
                }
            }
        }
    }

    @Composable
    override fun render() {
        CreateComponentScreen(this, navController)
    }
}
