package presentation

import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.mrpModule
import org.koin.core.context.startKoin
import presentation.navigation.NavHostViewModel
import presentation.theme.MrpProjectTheme

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
                ).let(::NavHostViewModel)
            }.render()
        }
    }
}
