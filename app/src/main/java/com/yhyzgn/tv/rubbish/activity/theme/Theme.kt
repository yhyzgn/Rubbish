package com.yhyzgn.tv.rubbish.activity.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TvAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = AppColors.accent,
            background = AppColors.background,
            surface = AppColors.cardNormal,
            onBackground = Color.White
        ),
        typography = Typography(),
        content = content
    )
}
