package presentation.screen.mrp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import data.entity.ProductEntity
import domain.model.GHP
import domain.model.GHPEntry
import domain.model.MRP
import domain.model.MRPEntry
import kotlinx.coroutines.Dispatchers
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
import java.util.Stack
import kotlin.math.ceil
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
        viewModelScope.launch(Dispatchers.Default) {
            val product = repository.getProduct(productId).first()
            _state.update {
                val ghp = produceGHP(product)
                it.copy(
                    product = product,
                    ghp = ghp,
                    mrps = produceMRPs(product, ghp)
                )
            }
        }
    }


    //region MRP
    private fun produceMRPs(product: ProductEntity, ghp: GHP): List<MRP> {
        val mrps = product.components.map { component ->
            MRP(
                name = component.name,
                leadTime = component.leadTime.toInt(),
                onHand = component.inStock.toInt(),
                batchSize = component.batchSize.toInt(),
                requiredAmount = component.requiredAmount.toInt(),
                bom = component.bom.toInt(),
                entries = List(10 - ghp.leadTime) { MRPEntry(predictedOnHand = component.inStock.toInt()) }
            )
        }

        return calculateMRPs(mrps, ghp)
    }

    private fun calculateMRPs(mrps: List<MRP>, ghp: GHP): List<MRP> {
        if (mrps.isEmpty()) return emptyList()

        @Suppress("NAME_SHADOWING")
        val mrps = mrps.toMutableList()

        val parentStack = Stack<MRP>()
        for (i in 0..mrps.lastIndex) {
            val mrp = mrps[i]

            if (mrp.bom == 1) {
                mrps[i] = calculateMRP(mrp, ghp)
                parentStack.push(mrp)
            } else {
                val previous = mrps[i - 1]
                if (mrp.bom < previous.bom) parentStack.pop()

                mrps[i] = calculateMRP(mrp, previous)

                mrps.getOrNull(i + 1)?.let { next ->
                    if (next.bom > mrp.bom) parentStack.push(mrp)
                }
            }
        }

        return mrps
    }

    private fun calculateMRP(mrp: MRP, ghp: GHP): MRP {
        val entries = mrp.entries.toMutableList()

        // Gross requirements
        run {
            for ((i, entry) in ghp.entries.withIndex()) {
                if (entry.production == 0) continue

                val entyIdx = i - ghp.leadTime
                if (entyIdx < 0) continue
                entries[entyIdx] = entries[entyIdx].copy(
                    grossRequirements = entry.production * mrp.requiredAmount
                )
            }
        }

        entries.forEachIndexed { index, entry ->
            var predictedOnHand = 0
            var netRequirements = 0
            var plannedOrderReceipts = 0

            val prev = entries.getOrElse(index - 1) {
                MRPEntry(
                    predictedOnHand = mrp.onHand -
                            parent.entries.subList(0, parent.leadTime).sumOf(GHPEntry::production)
                )
            }

            predictedOnHand = prev.predictedOnHand - entry.grossRequirements + entry.scheduledReceipts
            netRequirements = (entry.grossRequirements - prev.predictedOnHand).coerceAtLeast(0)
            plannedOrderReceipts = (mrp.batchSize * ceil(netRequirements.toDouble() / mrp.batchSize).toInt())
                .also {
                    predictedOnHand += it

                    val i = index - mrp.leadTime
                    if (i >= 0) entries[i] = entries[i].copy(plannedOrderReleases = it)
                }

            entries[index] = entry.copy(
                predictedOnHand = predictedOnHand,
                netRequirements = netRequirements,
                plannedOrderReceipts = plannedOrderReceipts
            )
        }

        return mrp.copy(entries = entries)
    }

    private fun calculateMRP(mrp: MRP, parent: MRP): MRP {
        val entries = mrp.entries.toMutableList()

        // Gross requirements
        run {
            for ((i, entry) in parent.entries.withIndex()) {
                if (entry.netRequirements == 0) continue

                val entyIdx = i - parent.leadTime
                if (entyIdx < 0) continue
                entries[entyIdx] = entries[entyIdx].copy(
                    grossRequirements = entry.netRequirements * mrp.requiredAmount
                )
            }
        }

        // Predicted on hand
        run {
            val debt = parent.entries.subList(0, parent.leadTime).sumOf(MRPEntry::netRequirements)
            val a = entries.subList(0, parent.leadTime)
            for (i in 0..a.lastIndex) {
                a[i] = a[i].copy(predictedOnHand = a[i].predictedOnHand - debt)
            }
        }

        return mrp.copy(entries = entries)
    }
    //endregion


    //region GHP
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
        ghpCalcJob = viewModelScope.launch(Dispatchers.Default) {
            delay(0.5.seconds)
            _state.update {
                it.copy(ghp = calculateGHP(it.ghp!!))
            }
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
    //endregion


    //region events
    fun scheduledReceiptsChanged(
        mrpIdx: Int,
        entryIdx: Int,
        newValue: String
    ) {
        if (!newValue.isNumber()) return
        _state.update {
            if (it.mrps == null) return@update it

            val mrps = it.mrps.toMutableList()
            val mrp = mrps[mrpIdx]

            val number = if (newValue.isEmpty()) 0 else newValue.toInt()

            val entries = mrp.entries.toMutableList()
            entries[entryIdx] = entries[entryIdx].copy(scheduledReceipts = number)

            mrps[mrpIdx] = mrp.copy(entries = entries)

            it.copy(mrps = mrps)
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
    //endregion


    @Composable
    override fun render() {
        MrpScreen(
            viewModel = this,
            navController = navController
        )
    }
}

fun String.isNumber(): Boolean {
    return matches("""\d*""".toRegex()) && try {
        ifBlank { "0" }.toInt(); true
    } catch (_: Exception) {
        false
    }
}
