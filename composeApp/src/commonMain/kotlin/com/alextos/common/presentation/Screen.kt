package com.alextos.common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.common_back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    modifier: Modifier,
    title: String,
    goBack: (() -> Unit)? = null,
    backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bannerView: @Composable (() -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
) {
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
                            Icon(
                                backButtonIcon,
                                stringResource(Res.string.common_back),
                            )
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
        floatingActionButton = floatingActionButton,
        bottomBar = {
            if (bannerView != null) {
                bannerView()
            }
        }
    ) { innerPaddings ->
        content(Modifier.padding(
            top = innerPaddings.calculateTopPadding(),
            bottom = if (bannerView != null) innerPaddings.calculateBottomPadding() else 0.dp
        ))
    }
}