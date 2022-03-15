package presentation.screen.products

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import data.entity.ProductTree
import kotlinx.coroutines.flow.StateFlow
import presentation.screen.products.components.ProductsScaffold
import presentation.screen.products.components.ProductsTable

@Composable
fun ProductsScreen(
    productsFlow: StateFlow<List<ProductTree>>,
    onCreateProduct: () -> Unit,
    onEditProduct: (ProductTree) -> Unit,
    onDeleteProductClick: (ProductTree) -> Unit,
    onGoToProduct: (ProductTree) -> Unit
) {
    val products by productsFlow.collectAsState()

    ProductsScaffold(onFabClick = onCreateProduct) { innerPadding ->
        ProductsTable(
            products = products,
            onEditProductClick = onEditProduct,
            onDeleteProductClick = onDeleteProductClick,
            onGoToProductClick = onGoToProduct,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
