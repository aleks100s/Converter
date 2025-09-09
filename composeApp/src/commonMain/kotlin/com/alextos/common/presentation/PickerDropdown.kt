package com.alextos.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.alextos.common.UiText
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.ic_heart
import converter.composeapp.generated.resources.ic_heart_fill
import converter.composeapp.generated.resources.ic_star
import org.jetbrains.compose.resources.vectorResource

interface PickerElement {
    enum class Icon {
        Star
    }

    val pickerOption: UiText

    val pickerTitle: String

    val trailingIcon: PickerElement.Icon?
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
                                text = it.pickerOption.asString(),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        trailingIcon = {
                            IconView(it.trailingIcon)
                        },
                        colors = MenuDefaults.itemColors(trailingIconColor = Color.Yellow),
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

@Composable
private fun IconView(icon: PickerElement.Icon?) {
    when (icon) {
        PickerElement.Icon.Star -> Icon(
            imageVector = vectorResource(Res.drawable.ic_star),
            contentDescription = null,
            tint = Color.Yellow,
            modifier = Modifier.size(24.dp)
        )
        null -> {}
    }
}
