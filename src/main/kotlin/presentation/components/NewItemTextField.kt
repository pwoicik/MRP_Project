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
fun EditComponentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isNumeric: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        isError = isError,
        onValueChange = {
            onValueChange(
                if (isNumeric) it.replace("""\D""".toRegex(), "")
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
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onKeyEvent { ev ->
                if (ev.type != KeyEventType.KeyDown) return@onKeyEvent true
                when (ev.key) {
                    Key.Tab -> {
                        focusManager.moveFocus(
                            if (ev.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
                        )
                    }
                    Key.Enter -> {
                        onSubmit()
                    }
                }
                true
            }.then(modifier)
    )
}
