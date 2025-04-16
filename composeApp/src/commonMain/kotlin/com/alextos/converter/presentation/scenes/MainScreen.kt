package com.alextos.converter.presentation.scenes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.converter.presentation.extensions.localization
import com.alextos.core.presentation.Screen
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_title),
    ) {
        LazyColumn(
            modifier = it
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            items(state.rates) { rate ->
                Text(
                    text = rate.code.localization.asString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}