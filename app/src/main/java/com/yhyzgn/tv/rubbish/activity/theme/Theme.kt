package com.yhyzgn.tv.rubbish.activity.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun TvAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = AppColors.accent,
            background = AppColors.background,
            surface = AppColors.cardNormal,
            onBackground = AppColors.textPrimary,
            onSurface = AppColors.textPrimary,
            onPrimary = AppColors.textPrimary
        ),
        typography = Typography(),
        content = content
    )
}
