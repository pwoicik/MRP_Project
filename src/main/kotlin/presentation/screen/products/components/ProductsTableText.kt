package presentation.screen.products.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.ProductsTableText(
    text: String,
    textAlign: TextAlign? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = 0.dp
) {
    Surface(
        color = containerColor,
        tonalElevation = tonalElevation,
        modifier = Modifier
            .weight(1f, fill = true)
            .fillMaxHeight()
            .border(
                width = Dp.Hairline,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
    ) {
        Text(
            text = text,
            textAlign = textAlign,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}
