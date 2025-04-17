package com.alextos.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

interface PickerElement {
    @get:Composable
    val pickerOption: String

    @get:Composable
    val pickerTitle: String
}

@Composable
fun <E: PickerElement>PickerDropdown(
    modifier: Modifier = Modifier,
    selected: E?,
    options: List<E>,
    onSelect: (E) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(vertical = 4.dp)) {
        CustomButton(selected?.pickerTitle ?: "") {
            expanded = true
        }

        if (options.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.pickerOption,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        onClick = {
                            onSelect(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
