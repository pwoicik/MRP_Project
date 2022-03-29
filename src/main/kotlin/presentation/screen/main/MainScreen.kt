package presentation.screen.main

import androidx.compose.runtime.*
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
            onAddProductClick = { navController.navigate(ScreenConfig.CreateProduct()) },
            onProductClick = { viewModel.emit(MainScreenEvent.ProductSelected(it)) }
        )
        Divider(
            thickness = 4.dp,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        )
        val selectedProduct by derivedStateOf { state.selectedProduct }
        DetailsFragment(
            product = selectedProduct,
            onEditProduct = { navController.navigate(ScreenConfig.CreateProduct(it.id)) },
            onDeleteProduct = { viewModel.emit(MainScreenEvent.DeleteProduct(it)) },
            onAddComponent = { viewModel.emit(MainScreenEvent.AddComponent) },
            onRunMrp = { navController.navigate(ScreenConfig.Mrp(it.id)) },
            onDeleteComponent = { viewModel.emit(MainScreenEvent.DeleteComponent(it)) },
            onEditComponent = { viewModel.emit(MainScreenEvent.EditComponent(it)) },
            onAddSubcomponent = { viewModel.emit(MainScreenEvent.AddSubcomponent(it)) }
        )
    }
}
