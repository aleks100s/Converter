package com.alextos.converter.presentation.scenes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.CustomButton
import com.alextos.common.presentation.CustomLabel
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.common.presentation.PickerDropdown
import com.alextos.common.presentation.Screen
import com.alextos.converter.domain.camera.CameraProps
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.camera
import converter.composeapp.generated.resources.camera_button_title
import converter.composeapp.generated.resources.camera_title
import converter.composeapp.generated.resources.ic_camera
import converter.composeapp.generated.resources.converter_clear
import converter.composeapp.generated.resources.converter_quick_select
import converter.composeapp.generated.resources.converter_reload
import converter.composeapp.generated.resources.converter_swap
import converter.composeapp.generated.resources.converter_title
import converter.composeapp.generated.resources.copy
import converter.composeapp.generated.resources.data_is_actual
import converter.composeapp.generated.resources.ic_swap
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_title),
        floatingActionButton = {
            val props = CameraProps(
                title = stringResource(Res.string.camera_title, state.topCurrency?.code ?: "", state.bottomCurrency?.code ?: ""),
                button = stringResource(Res.string.camera_button_title)
            )
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                    viewModel.onAction(MainAction.ShowCamera(props))
                }
            ) {
                CustomLabel(
                    title = props.title,
                    imageVector = vectorResource(Res.drawable.ic_camera)
                )
            }
        },
        actions = {
            RefreshButton {
                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                viewModel.onAction(MainAction.ReloadRates)
            }
        }
    ) { modifier ->
        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CurrencyEditor(
                    currency = state.topCurrency,
                    value = state.topText,
                    onValueChanged = { text ->
                        viewModel.onAction(MainAction.TopTextChanged(text))
                    },
                    currencies = state.rates,
                    onCurrencySelected = { currency ->
                        viewModel.onAction(MainAction.TopCurrencySelected(currency))
                    },
                    onCopy = { text, label ->
                        viewModel.onAction(MainAction.CopyButtonTapped(text, label))
                    }
                )

                SwapButton {
                    haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                    viewModel.onAction(MainAction.SwapCurrencies)
                }

                CurrencyEditor(
                    currency = state.bottomCurrency,
                    value = state.bottomText,
                    onValueChanged = { text ->
                        viewModel.onAction(MainAction.BottomTextChanged(text))
                    },
                    currencies = state.rates,
                    onCurrencySelected = { currency ->
                        viewModel.onAction(MainAction.BottomCurrencySelected(currency))
                    },
                    onCopy = { text, label ->
                        viewModel.onAction(MainAction.CopyButtonTapped(text, label))
                    }
                )

                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = stringResource(Res.string.data_is_actual),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun RefreshButton(onClick: () -> Unit) {
    var isRotating by remember { mutableStateOf(false) }
    val rotate = remember { Animatable(0f) }
    val target = 360f
    if (isRotating) {
        LaunchedEffect(Unit) {
            val remaining = (target - rotate.value) / target
            rotate.animateTo(target, animationSpec = tween((300 * remaining).toInt(), easing = EaseInOutBounce))
            rotate.snapTo(0f)
            isRotating = false
        }
    }

    IconButton(
        onClick = {
            isRotating = true
            onClick()
        },
        modifier = Modifier.run { rotate(rotate.value) }
    ) {
        Icon(
            Icons.Default.Refresh,
            stringResource(Res.string.converter_reload),
        )
    }
}

@Composable
fun SwapButton(onClick: () -> Unit) {
    var isRotating by remember { mutableStateOf(false) }
    val rotate = remember { Animatable(0f) }
    val target = 180f
    if (isRotating) {
        LaunchedEffect(Unit) {
            val remaining = (target - rotate.value) / target
            rotate.animateTo(target, animationSpec = tween((300 * remaining).toInt(), easing = EaseInOutBounce))
            rotate.snapTo(0f)
            isRotating = false
        }
    }

    IconButton(
        onClick = {
            isRotating = true
            onClick()
        },
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .size(32.dp)
            .run { rotate(rotate.value) }
    ) {
        Icon(
            vectorResource(Res.drawable.ic_swap),
            stringResource(Res.string.converter_swap),
        )
    }
}

@Composable
fun CurrencyEditor(
    currency: CurrencyRate?,
    value: String,
    onValueChanged: (String) -> Unit,
    currencies: List<CurrencyRate>,
    onCurrencySelected: (CurrencyRate) -> Unit,
    onCopy: (String, String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PickerDropdown(
                selected = currency,
                options = currencies,
                onSelect = onCurrencySelected
            )

            var isFocused by remember { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                value = TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                ),
                onValueChange = { value ->
                    onValueChanged(value.text)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                leadingIcon = {
                    Text(text = currency?.flag ?: "")
                },
                suffix = {
                    Text(text = currency?.sign ?: "")
                },
                trailingIcon = if (value != "0" && isFocused) {
                    {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                                onValueChanged("0")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(Res.string.converter_clear),
                            )
                        }
                    }
                } else {
                    {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onCopy(value, currency?.sign ?: "")
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.copy),
                                contentDescription = stringResource(Res.string.converter_clear),
                            )
                        }
                    }
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.alpha(0.7f),
                text = stringResource(Res.string.converter_quick_select),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            listOf("1", "10", "100").forEach { title ->
                CustomButton(
                    title = "$title${currency?.sign ?: ""}",
                    onTap = {
                        haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                        onValueChanged(title)
                    }
                )
            }
        }
    }
}