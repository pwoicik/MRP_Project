package presentation.screen.mrp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import data.entity.ProductEntity
import domain.model.GHP
import domain.model.GHPEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig
import presentation.navigation.ViewModel
import kotlin.time.Duration.Companion.seconds

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
                it.copy(
                    product = product,
                    ghp = produceGHP(product)
                )
            }
        }
    }

    private fun produceGHP(product: ProductEntity): GHP {
        val demand = { week: Int -> if (week < 7) 10 else 15 }
        val production = { week: Int ->
            when (week) {
                4, 8, 10 -> 30
                else -> 0
            }
        }

        val ghp = GHP(
            name = product.name,
            leadTime = product.leadTime.toInt(),
            onHand = product.inStock.toInt(),
            entries = (1..10).map {
                GHPEntry(
                    demand = demand(it),
                    production = production(it),
                    availableAmount = 0
                )
            }
        )

        return calculateGHP(ghp)
    }

    private fun calculateGHP(ghp: GHP): GHP {
        val entries = ghp.entries.toMutableList()

        entries[0] = entries[0].copy(availableAmount = ghp.onHand - entries[0].demand + entries[0].production)

        for (i in 1..ghp.entries.lastIndex) {
            entries[i] = entries[i].copy(
                availableAmount = entries[i - 1].availableAmount - entries[i].demand + entries[i].production
            )
        }

        return ghp.copy(entries = entries)
    }

    private var ghpCalcJob: Job? = null
    private fun calculateGHP() {
        ghpCalcJob?.cancel()
        if (state.value.ghp == null) return
        ghpCalcJob = viewModelScope.launch {
            delay(0.5.seconds)
            _state.update {
                it.copy(ghp = calculateGHP(it.ghp!!))
            }
        }
    }

    fun demandChanged(index: Int, newValue: String) {
        updateGHP(index, newValue) { entry, newNumber ->
            entry.copy(demand = newNumber)
        }
    }

    fun productionChanged(index: Int, newValue: String) {
        updateGHP(index, newValue) { entry, newNumber ->
            entry.copy(production = newNumber)
        }
    }

    private fun updateGHP(
        index: Int,
        newValue: String,
        updateFunction: (GHPEntry, Int) -> GHPEntry
    ) {
        if (!newValue.isNumber()) return
        _state.update {
            if (it.ghp == null) return@update it
            val number = if (newValue.isEmpty()) 0 else newValue.toInt()
            val entries = it.ghp.entries.toMutableList()
            entries[index] = updateFunction(entries[index], number)
            it.copy(ghp = it.ghp.copy(entries = entries))
        }
        calculateGHP()
    }

    @Composable
    override fun render() {
        MrpScreen(
            viewModel = this,
            navController = navController
        )
    }
}

fun String.isNumber(): Boolean {
    return matches("""\d*""".toRegex())
}
