import domain.model.MutableProductTreeNode
import domain.repository.MrpRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MrpApplication : KoinComponent {

    val mrpRepository: MrpRepository by inject()

    init {
        populateDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun populateDatabase() {
        GlobalScope.launch {
            mrpRepository.insertProduct(
                MutableProductTreeNode(
                    name = "Krzesło",
                    leadTime = 2,
                    inStock = 48
                )
            )
            mrpRepository.insertProduct(
                MutableProductTreeNode(
                    name = "Stół",
                    leadTime = 3,
                    inStock = 21
                )
            )
            mrpRepository.insertProduct(
                MutableProductTreeNode(
                    name = "Szafka",
                    leadTime = 5,
                    inStock = 17
                )
            )
            /* TODO replace placeholder values */
            mrpRepository.insertProduct(
                MutableProductTreeNode(
                    name = "Drzwi",
                    leadTime = 1,
                    batchSize = 1,
                    inStock = 1,
                    bom = 0,
                    components = mutableListOf(
                        MutableProductTreeNode(
                            name = "Panel drzwiowy",
                            leadTime = 1,
                            batchSize = 1,
                            inStock = 1,
                            bom = 1,
                            components = mutableListOf(
                                MutableProductTreeNode(
                                    name = "Płyta drewniana",
                                    leadTime = 1,
                                    batchSize = 1,
                                    inStock = 1,
                                    bom = 2,
                                    components = mutableListOf()
                                ),
                                MutableProductTreeNode(
                                    name = "Klamka",
                                    leadTime = 1,
                                    batchSize = 1,
                                    inStock = 1,
                                    bom = 2,
                                    components = mutableListOf()
                                ),
                                MutableProductTreeNode(
                                    name = "Szyba",
                                    leadTime = 1,
                                    batchSize = 1,
                                    inStock = 1,
                                    bom = 2,
                                    components = mutableListOf()
                                )
                            )
                        ),
                        MutableProductTreeNode(
                            name = "Framuga",
                            leadTime = 1,
                            batchSize = 1,
                            inStock = 1,
                            bom = 1,
                            components = mutableListOf(
                                MutableProductTreeNode(
                                    name = "Listwa",
                                    requiredAmount = 3,
                                    leadTime = 1,
                                    batchSize = 1,
                                    inStock = 1,
                                    bom = 2,
                                    components = mutableListOf()
                                )
                            )
                        ),
                        MutableProductTreeNode(
                            name = "Zawiasy",
                            requiredAmount = 2,
                            leadTime = 1,
                            batchSize = 1,
                            inStock = 1,
                            bom = 1,
                            components = mutableListOf()
                        )
                    )
                )
            )
        }
    }
}
