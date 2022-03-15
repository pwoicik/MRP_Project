package presentation

import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.mrpModule
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.context.startKoin
import presentation.navigation.NavHostComponent
import presentation.theme.MrpProjectTheme

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    startKoin {
        modules(mrpModule)
    }

    singleWindowApplication(
        state = WindowState(placement = WindowPlacement.Maximized),
        title = "MRP Project"
    ) {
        MrpProjectTheme {
            remember {
                DefaultComponentContext(
                    LifecycleRegistry()
                ).let(::NavHostComponent)
            }.render()
        }
    }
}
