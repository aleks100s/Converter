package com.alextos.common.presentation

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
import androidx.compose.ui.window.PopupProperties

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
    val pairedItems = options.map { it.pickerOption to it }
    val options = remember { pairedItems.sortedBy { it.first }.map { it.second } }

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
