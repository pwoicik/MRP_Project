package presentation.screen.main.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.MainFragment(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.weight(1f),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}
