package presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    onClickLabel: String? = null,
    size: Dp = 24.dp,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = onClickLabel,
            tint = tint,
            modifier = Modifier
                .size(size)
                .pointerHoverIcon(PointerIconDefaults.Hand)
        )
    }
}
