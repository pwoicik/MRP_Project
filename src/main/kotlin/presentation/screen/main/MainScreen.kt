package presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import presentation.components.Divider
import presentation.navigation.NavController
import presentation.navigation.ScreenConfig
import presentation.screen.main.components.MainScaffold
import presentation.screen.main.fragments.details.DetailsFragment
import presentation.screen.main.fragments.products.ProductsFragment

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    MainScaffold {
        ProductsFragment(
            products = state.products,
            onAddProductClick = { navController.navigate(ScreenConfig.CreateComponent()) },
            onProductClick = { viewModel.emit(MainScreenEvent.ProductSelected(it)) }
        )
        Divider(
            thickness = 4.dp,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        )
        DetailsFragment(
            product = state.selectedProduct,
            onEditProduct = { navController.navigate(ScreenConfig.CreateComponent(it.id)) },
            onDeleteProduct = { viewModel.emit(MainScreenEvent.DeleteProduct(it)) },
            onRunMrp = { navController.navigate(ScreenConfig.Mrp(it.id)) }
        )
    }
}
