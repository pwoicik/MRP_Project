package presentation.screen.products.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.model.Product

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsTable(
    products: List<Product>,
    onEditProductClick: (Product) -> Unit,
    onAddComponentClick: (Product) -> Unit,
    onGoToProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 36.dp),
        modifier = modifier
    ) {
        stickyHeader { ProductsTableHeader() }
        items (products) { product ->
            ProductsTableItem(
                product = product,
                onEditProductClick = onEditProductClick,
                onAddComponentClick = onAddComponentClick,
                onGoToProductClick = onGoToProductClick
            )
        }
    }
}

@Composable
fun ProductsTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        ProvideTextStyle(MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)) {
            ProductsTableText(
                text = "NAME",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ProductsTableText(
                text = "LEAD TIME",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ProductsTableText(
                text = "BATCH SIZE",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ProductsTableText(
                text = "IN STOCK",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ProductsTableText(
                text = "ACTIONS", textAlign = TextAlign.Center,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
