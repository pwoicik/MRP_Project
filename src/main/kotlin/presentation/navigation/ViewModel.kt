package presentation.navigation

import MrpApplication
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class ViewModel(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    protected val viewModelScope = MainScope()

    @Composable
    abstract fun render()

    init {
        subscribeOnDestroy()
    }

    private fun subscribeOnDestroy() {
        lifecycle.subscribe(
            onDestroy = { viewModelScope.cancel() }
        )
    }

    companion object {
        val repository = MrpApplication().mrpRepository
    }
}
