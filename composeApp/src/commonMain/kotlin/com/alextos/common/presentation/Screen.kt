package com.alextos.common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.common_back
import converter.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    modifier: Modifier,
    title: String,
    goBack: (() -> Unit)? = null,
    backButtonIcon: @Composable () -> Unit = {
        Icon(
            vectorResource(Res.drawable.ic_arrow_back),
            stringResource(Res.string.common_back)
        )
    },
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    FoldSafeArea {
        Scaffold(
            modifier = modifier
                .background(Brush.linearGradient(
                    colors = listOf(
                        Color(red = 65, green = 106, blue = 193),
                        Color(143,67,189)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )),
            topBar = {
                TopAppBar(
                    title = {
                        Text(title)
                    },
                    colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.background.copy(0.2f)),
                    actions = actions,
                    navigationIcon = {
                        if (goBack != null) {
                            IconButton(
                                onClick = {
                                    goBack()
                                }
                            ) {
                                backButtonIcon()
                            }
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
            floatingActionButton = floatingActionButton
        ) { innerPaddings ->
            BoxWithConstraints(
                modifier = Modifier
                    .padding(top = innerPaddings.calculateTopPadding())
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .fillMaxSize()
            ) {
                val widthModifier = if (maxWidth > 840.dp) {
                    Modifier
                        .widthIn(max = 840.dp)
                        .align(Alignment.TopCenter)
                } else {
                    Modifier
                }
                content(widthModifier)
            }
        }
    }
}
