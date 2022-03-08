package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import presentation.theme.MrpProjectTheme

@Composable
fun App() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var text by remember { mutableStateOf("Hello, World!") }
        Button(onClick = { text = "Hello, Desktop!" }) {
            Text(text)
        }
    }
}

fun main() = application {
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
