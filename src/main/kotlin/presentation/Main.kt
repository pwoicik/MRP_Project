package presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import di.mrpModule
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.context.startKoin
import presentation.screen.product.ProductsScreen
import presentation.theme.MrpProjectTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    ProductsScreen()
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    startKoin {
        modules(mrpModule)
    }

    application {
        val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
        Window(
            state = windowState,
            title = "MRP Project",
            onCloseRequest = ::exitApplication
        ) {
            MrpProjectTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    App()
                }
            }
        }
    }
}
