@file:Suppress("UNUSED")

package presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.Divider(
    thickness: Dp = 1.dp,
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.surface
) {
    Surface(
        color = color,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = Modifier
            .fillMaxHeight()
            .width(thickness)
    ) { }
}

@Composable
fun ColumnScope.Divider(
    thickness: Dp = 1.dp,
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.surface
) {
    Surface(
        color = color,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
    ) { }
}

@Composable
fun LazyItemScope.Divider(
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.surface
) {
    Surface(
        color = color,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .padding(start = startIndent)
    ) { }
}
