package presentation.screen.products.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import domain.model.Product

@Composable
fun ProductsTableItem(
    product: Product,
    onEditProductClick: (Product) -> Unit,
    onAddComponentClick: (Product) -> Unit,
    onGoToProductClick: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .border(width = Dp.Hairline, color = MaterialTheme.colorScheme.outline.copy(0.7f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            ProductsTableText(product.name, tonalElevation = 1.dp)
            ProductsTableText(product.leadTime.toString())
            ProductsTableText(product.batchSize.toString(), tonalElevation = 1.dp)
            ProductsTableText(product.inStock.toString())

            TableItemActions(
                onEditProductClick = { onEditProductClick(product) },
                onAddComponentClick = { onAddComponentClick(product) },
                onGoToProductClick = { onGoToProductClick(product) }
            )
        }
    }
}

@Composable
fun RowScope.TableItemActions(
    onEditProductClick: () -> Unit,
    onAddComponentClick: () -> Unit,
    onGoToProductClick: () -> Unit
) {
    Surface(
        tonalElevation = 1.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        ),
        modifier = Modifier.weight(1f, fill = true)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            ProductsTableActionButton(
                onClick = onEditProductClick,
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit product"
            )
            ProductsTableActionButton(
                onClick = onAddComponentClick,
                imageVector = Icons.Default.Add,
                contentDescription = "Add component"
            )
            ProductsTableActionButton(
                onClick = onGoToProductClick,
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Go to product page"
            )
        }
    }
}
