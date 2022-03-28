package presentation.screen.createProduct

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import presentation.components.EditComponentTextField
import presentation.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    viewModel: CreateProductViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    val onSubmit = {
        viewModel.emit(CreateProductEvent.SaveProduct)
        navController.navigateUp()
    }

    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 36.dp)
                    .widthIn(max = 500.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(if (viewModel.isEditMode) "Edytuj produkt" else "Stwórz produkt") },
                    actions = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                )

                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                EditComponentTextField(
                    value = state.name,
                    isError = state.name.isBlank(),
                    onValueChange = { viewModel.emit(CreateProductEvent.NameChanged(it)) },
                    onSubmit = onSubmit,
                    label = "nazwa",
                    modifier = Modifier.focusRequester(focusRequester)
                )
                EditComponentTextField(
                    value = state.leadTime,
                    isError = state.leadTime.isBlank(),
                    onValueChange = { viewModel.emit(CreateProductEvent.LeadTimeChanged(it)) },
                    onSubmit = onSubmit,
                    label = "czas produkcji",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.batchSize,
                    isError = state.batchSize.isBlank(),
                    onValueChange = { viewModel.emit(CreateProductEvent.BatchSizeChanged(it)) },
                    onSubmit = onSubmit,
                    label = "rozmiar partii",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.inStock,
                    isError = state.inStock.isBlank(),
                    onValueChange = { viewModel.emit(CreateProductEvent.InStockChanged(it)) },
                    onSubmit = onSubmit,
                    label = "na stanie",
                    isNumeric = true
                )

                Button(
                    onClick = onSubmit,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Zapisz")
                }
            }
        }
    }
}
