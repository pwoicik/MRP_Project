package presentation.screen.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import data.entity.ProductEntity

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductCard(
    product: ProductEntity,
    onClick: (() -> Unit)? = null
) {
    val surface = @Composable { content: @Composable () -> Unit ->
        if (onClick != null) {
            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                onClick = onClick,
                modifier = Modifier
                    .width(400.dp)
                    .pointerHoverIcon(PointerIconDefaults.Hand),
                content = content
            )
        } else {
            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(400.dp),
                content = content
            )
        }
    }
    surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(top = 18.dp, bottom = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 18.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (onClick != null) {
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                ComponentDetail(
                    detail = "czas produkcji",
                    value = product.leadTime
                )
                ComponentDetail(
                    detail = "na stanie",
                    value = product.inStock
                )
            }
        }
    }
}

@Composable
fun ComponentDetail(
    detail: String,
    value: Long
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyMedium,
            LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        ) {
            Text(detail)
            Text(value.toString())
        }
    }
}
