package presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewItemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isNumeric: Boolean = false,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(
                if (isNumeric)
                    it.replace("""\d""".toRegex(), "")
                else it
            )
        },
        singleLine = true,
        label = { Text(label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onKeyEvent { ev ->
                if (ev.key == Key.Tab && ev.type == KeyEventType.KeyDown) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
                true
            }.then(modifier)
    )
}
