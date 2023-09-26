package ru.droid.tech.base.res

import androidx.compose.ui.graphics.Color
import ru.droid.tech.base.theme.ColorsSchemeApp

val AquaMarine = Color(0xFF29B8CC) // AquaMarine Primary
val RaisinBlack = Color(0xFF415174) // Raisin Black Dark Grey textDark
val BlueBell = Color(0xFF919CB4) // Blue Bell Light Gray textLite
val Whisper = Color(0xFFF7F8FA) // Whisper Background
val Zircon = Color(0xFFEDEFF2) // Zircon Text field
val RaisinBlackTranslucent = Color(0x4D415174) // Raisin Black Translucent Transparent Gray
val LightBlue = Color(0xFFD6EEF3) // Light Blue Light
val ElectricGreen = Color(0xFF29CC39) // Electric Green
val FireEngineRed = Color(0xFFB3261E) // Fire Engine Red
val WhiteApp = Color.White // White

val LightPaletteApp = ColorsSchemeApp(
    primary = AquaMarine,
    onPrimary = WhiteApp,

    background = Whisper,
    onBackground = RaisinBlack,

    backgroundVariant = WhiteApp,
    onBackgroundVariant = RaisinBlack,

    container = Zircon,
    onContainer = RaisinBlack,

    containerVariant = LightBlue,
    onContainerVariant = RaisinBlack,

    borderLight = BlueBell,
    borderDark = RaisinBlack,

    textLight = BlueBell,
    textTransparent = RaisinBlackTranslucent,
    textDark = RaisinBlack,

    attentionContent = FireEngineRed,
    goodContent = ElectricGreen
)
