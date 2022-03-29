import domain.model.Component
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
                name = "Krzesło",
                leadTime = 2,
                inStock = 48
            )
            mrpRepository.insertProduct(
                name = "Stół",
                leadTime = 3,
                inStock = 21
            )
            mrpRepository.insertProduct(
                name = "Szafka",
                leadTime = 5,
                inStock = 17
            )
            mrpRepository.insertProduct(
                name = "Drzwi",
                leadTime = 2,
                inStock = 48,
                components = listOf(
                    Component(
                        name = "Panel drzwiowy",
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 1
                    ),
                    Component(
                        name = "Płyta drewniana",
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 2
                    ),
                    Component(
                        name = "Klamka",
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 2
                    ),
                    Component(
                        name = "Szyba",
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 2
                    ),
                    Component(
                        name = "Framuga",
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 1
                    ),
                    Component(
                        name = "Listwa",
                        requiredAmount = 3,
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 2
                    ),
                    Component(
                        name = "Zawiasy",
                        requiredAmount = 2,
                        leadTime = 1,
                        batchSize = 1,
                        inStock = 1,
                        bom = 1
                    )
                )
            )
        }
    }
}
