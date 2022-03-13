import domain.repository.MrpRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MrpApplication : KoinComponent {

    val mrpRepository: MrpRepository by inject()
}
