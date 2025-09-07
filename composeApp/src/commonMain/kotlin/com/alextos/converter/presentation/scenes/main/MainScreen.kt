package com.alextos.converter.presentation.scenes.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.CustomButton
import com.alextos.common.presentation.CustomLabel
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.common.presentation.PickerDropdown
import com.alextos.common.presentation.Screen
import com.alextos.converter.domain.camera.CameraProps
import com.alextos.converter.presentation.scenes.settings.SettingsSheet
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.camera_button_title
import converter.composeapp.generated.resources.camera_title
import converter.composeapp.generated.resources.common_next
import converter.composeapp.generated.resources.ic_camera
import converter.composeapp.generated.resources.converter_clear
import converter.composeapp.generated.resources.converter_quick_select
import converter.composeapp.generated.resources.converter_reload
import converter.composeapp.generated.resources.converter_retry
import converter.composeapp.generated.resources.converter_settings
import converter.composeapp.generated.resources.converter_swap
import converter.composeapp.generated.resources.converter_title
import converter.composeapp.generated.resources.copy
import converter.composeapp.generated.resources.data_is_actual
import converter.composeapp.generated.resources.ic_settings
import converter.composeapp.generated.resources.ic_swap
import converter.composeapp.generated.resources.onboarding_bottom_editor_text
import converter.composeapp.generated.resources.onboarding_camera_button_text
import converter.composeapp.generated.resources.onboarding_initial_text
import converter.composeapp.generated.resources.onboarding_refresh_button_text
import converter.composeapp.generated.resources.onboarding_settings_button_text
import converter.composeapp.generated.resources.onboarding_swap_button_text
import converter.composeapp.generated.resources.onboarding_top_editor_text
import converter.composeapp.generated.resources.onboarding_try
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    if (state.isSettingsSheetShown) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.onAction(MainAction.DismissSettingsSheet)
            }
        ) {
            SettingsSheet()
        }
    }

    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_title),
        floatingActionButton = {
            if (state.contentState == ContentState.Success) {
                val props = CameraProps(
                    title = stringResource(
                        Res.string.camera_title,
                        state.topCurrency?.code ?: "",
                        state.bottomCurrency?.code ?: ""
                    ),
                    button = stringResource(Res.string.camera_button_title)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AnimatedVisibility(state.onboardingState.isCameraButtonTextVisible) {
                        Text(
                            stringResource(Res.string.onboarding_camera_button_text),
                            modifier = Modifier.width(200.dp),
                            textAlign = TextAlign.End,
                        )
                    }

                    AnimatedVisibility(state.onboardingState.isNextOnboardingButtonVisible) {
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                                viewModel.onAction(MainAction.NextOnboardingStepButtonTapped)
                            },
                        ) {
                            val title =
                                if (state.onboardingState.step == OnboardingStep.CameraButton) {
                                    stringResource(Res.string.onboarding_try)
                                } else {
                                    stringResource(Res.string.common_next)
                                }
                            Text(title, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    AnimatedVisibility(!state.onboardingState.isNextOnboardingButtonVisible) {
                        Box {
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
                        }
                    }
                }
            }
        },
        actions = {
            SettingsButton(
                modifier = Modifier
                    .alpha(state.onboardingState.settingsButtonAlpha)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = state.onboardingState.settingsButtonBackgroundAlpha)),
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                viewModel.onAction(MainAction.SettingsButtonTapped)
            }

            RefreshButton(
                modifier = Modifier
                    .alpha(state.onboardingState.refreshButtonAlpha)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = state.onboardingState.refreshButtonBackgroundAlpha)),
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                viewModel.onAction(MainAction.ReloadRates)
            }
        }
    ) { modifier ->
        when (state.contentState) {
            is ContentState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ContentState.Error -> {
                ErrorView(
                    modifier = modifier,
                    error = (state.contentState as ContentState.Error).message.asString(),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                        viewModel.onAction(MainAction.ReloadRates)
                    }
                )
            }
            is ContentState.Success -> {
                ContentView(
                    modifier = modifier,
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}

@Composable
private fun ErrorView(
    modifier: Modifier,
    error: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = onClick
            ) {
                Text(
                    stringResource(Res.string.converter_retry),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ContentView(
    modifier: Modifier,
    state: MainState,
    onAction: (MainAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    AnimatedVisibility(
        state.onboardingState.step == OnboardingStep.Initial,
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.onboarding_initial_text),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }
    }

    AnimatedVisibility(
        state.onboardingState.step != OnboardingStep.Initial,
        enter = fadeIn()
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(state.onboardingState.isRefreshButtonTextVisible) {
                Text(
                    stringResource(Res.string.onboarding_refresh_button_text),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }

            AnimatedVisibility(state.onboardingState.isSettingsButtonTextVisible) {
                Text(
                    stringResource(Res.string.onboarding_settings_button_text),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }

            CurrencyEditor(
                currency = state.topCurrency,
                value = state.topText,
                onValueChanged = { text ->
                    onAction(MainAction.TopTextChanged(text))
                },
                currencies = state.rates,
                onCurrencySelected = { currency ->
                    onAction(MainAction.TopCurrencySelected(currency))
                },
                onCopy = { text, label ->
                    onAction(MainAction.CopyButtonTapped(text, label))
                },
                modifier = Modifier
                    .alpha(state.onboardingState.topEditorAlpha)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = state.onboardingState.topEditorBackgroundAlpha))
            )

            AnimatedVisibility(
                visible = state.onboardingState.isTopEditorTextVisible,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
            ) {
                Text(stringResource(Res.string.onboarding_top_editor_text))
            }

            SwapButton(
                Modifier
                    .alpha(state.onboardingState.swapButtonAlpha)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = state.onboardingState.swapButtonBackgroundAlpha)),
                isTextVisible = state.onboardingState.isSwapButtonTextVisible,
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                onAction(MainAction.SwapCurrencies)
            }

            CurrencyEditor(
                currency = state.bottomCurrency,
                value = state.bottomText,
                onValueChanged = { text ->
                    onAction(MainAction.BottomTextChanged(text))
                },
                currencies = state.rates,
                onCurrencySelected = { currency ->
                    onAction(MainAction.BottomCurrencySelected(currency))
                },
                onCopy = { text, label ->
                    onAction(MainAction.CopyButtonTapped(text, label))
                },
                modifier = Modifier
                    .alpha(state.onboardingState.bottomEditorAlpha)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = state.onboardingState.bottomEditorBackgroundAlpha))
            )

            AnimatedVisibility(
                visible = state.onboardingState.isBottomEditorTextVisible,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
            ) {
                Text(stringResource(Res.string.onboarding_bottom_editor_text))
            }

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .alpha(state.onboardingState.hintAlpha),
                text = stringResource(Res.string.data_is_actual),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RefreshButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
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
        modifier = modifier.run { rotate(rotate.value) }
    ) {
        Icon(
            Icons.Default.Refresh,
            stringResource(Res.string.converter_reload),
        )
    }
}

@Composable
private fun SettingsButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            vectorResource(Res.drawable.ic_settings),
            stringResource(Res.string.converter_settings),
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
private fun SwapButton(
    modifier: Modifier,
    isTextVisible: Boolean,
    onClick: () -> Unit
) {
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

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                isRotating = true
                onClick()
            },
            modifier = modifier
                .minimumInteractiveComponentSize()
                .size(32.dp)
                .run { rotate(rotate.value) }
        ) {
            Icon(
                vectorResource(Res.drawable.ic_swap),
                stringResource(Res.string.converter_swap),
            )
        }

        AnimatedVisibility(isTextVisible) {
            Text(stringResource(Res.string.onboarding_swap_button_text))
        }
    }
}

@Composable
private fun CurrencyEditor(
    modifier: Modifier,
    currency: CurrencyRate?,
    value: String,
    onValueChanged: (String) -> Unit,
    currencies: List<CurrencyRate>,
    onCurrencySelected: (CurrencyRate) -> Unit,
    onCopy: (String, String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier.padding(4.dp)) {
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