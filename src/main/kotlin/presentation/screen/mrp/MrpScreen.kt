package presentation.screen.mrp

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import presentation.components.TopBar
import presentation.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MrpScreen(
    viewModel: MrpViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = null
                        )
                    }
                    Text("akurwachuj")
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) {

    }
}
