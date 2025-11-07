package com.yhyzgn.tv.rubbish.activity.theme

import androidx.compose.ui.graphics.Color

object AppColors {
    // Core
    val background = Color(0xFF0B0B0B) // app background (near black)
    val surface = Color(0xFF0F0F0F) // surface panels
    val surfaceVariant = Color(0xFF131313) // slightly lighter surface for elevated panels
    val backgroundElevated = Color(0xFF0D0D0D) // for cards sitting above the background

    // Brand / accent (Netflix red)
    val accent = Color(0xFFE50914)
    val textAccent = accent

    // Text
    val textPrimary = Color(0xFFFFFFFF) // main readable text (white)
    val textSecondary = Color(0xFFB3B3B3) // secondary text (muted gray)
    val textGray = Color(0xFF8F9598) // even more muted (used for captions / metadata)
    val textDisabled = Color(0x66FFFFFF) // disabled text (semi-transparent white)
    val disabledLabel = textDisabled

    // Cards / panels
    val cardNormal = Color(0xFF1B1B1B)
    val cardFocused = Color(0xFF262626)
    val cardSelected = Color(0xFF2B2B2B)
    val selectedBackground = cardSelected

    // Interaction states (Netflix-like: deep dark UI with red accents)
    // focusGlow: subtle translucent red used for glow effects
    val focusGlow = Color(0x33E50914) // slightly stronger than before (alpha 0x33)
    // focusRing: a stronger translucent border/ring when needed
    val focusRing = Color(0x66E50914)
    // focusBorder: used for 1-2dp outlines
    val focusBorder = Color(0x33E50914)

    // Press/active
    val pressedBackground = Color(0x33E50914) // pressed overlay (red, low alpha)
    val pressedText = accent // pressed text color (keep as brand red for emphasis)

    // Disabled / overlays
    // disabledBackground should be a subtle dark overlay (not white)
    val disabledBackground = Color(0x1F000000)
    val overlay = Color(0x66000000) // general dim overlay
    val overlayDark = Color(0x99000000) // stronger dim

    // Dividers
    val divider = Color(0x1AFFFFFF) // subtle light divider on dark background
    val dividerStrong = Color(0x33FFFFFF)

    // Shadows / extras
    val shadow = Color(0x55000000)

    // Semantic extras
    val success = Color(0xFF2ECC71)
    val error = Color(0xFFFF3B30)
}
