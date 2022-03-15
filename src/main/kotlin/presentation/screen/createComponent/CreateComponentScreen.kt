package presentation.screen.createComponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import presentation.components.EditComponentTextField
import presentation.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateComponentScreen(
    stateFlow: StateFlow<CreateComponentState>,
    emit: (CreateComponentEvent) -> Unit,
    navController: NavController
) {
    val state by stateFlow.collectAsState()

    val onSubmit = {
        emit(CreateComponentEvent.SaveComponent)
        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create a product") },
                actions = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save") },
                icon = { Icon(imageVector = Icons.Default.Save, contentDescription = "Save product") },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = onSubmit
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
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

                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                EditComponentTextField(
                    value = state.name,
                    isError = state.name.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.NameChanged(it)) },
                    onSubmit = onSubmit,
                    label = "name",
                    modifier = Modifier.focusRequester(focusRequester)
                )
                EditComponentTextField(
                    value = state.leadTime,
                    isError = state.leadTime.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.LeadTimeChanged(it)) },
                    onSubmit = onSubmit,
                    label = "lead time",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.batchSize,
                    isError = state.batchSize.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.BatchSizeChanged(it)) },
                    onSubmit = onSubmit,
                    label = "batch size",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.inStock,
                    isError = state.inStock.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.InStockChanged(it)) },
                    onSubmit = onSubmit,
                    label = "in stock",
                    isNumeric = true
                )
            }
        }
    }
}
