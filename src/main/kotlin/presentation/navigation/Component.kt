package presentation.navigation

import MrpApplication
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class Component(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    protected val componentScope = MainScope()

    @Composable
    abstract fun render()

    init {
        subscribeOnDestroy()
    }

    private fun subscribeOnDestroy() {
        lifecycle.subscribe(
            onDestroy = { componentScope.cancel() }
        )
    }

    companion object {
        val repository = MrpApplication().mrpRepository
    }
}
