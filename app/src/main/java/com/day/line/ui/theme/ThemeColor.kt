package com.day.line.ui.theme

import androidx.compose.ui.graphics.Color

enum class ThemeColor(val colorName: String, val color: Color) {
    ORANGE("Orange", Color(0xFFFF8A00)),
    TEAL("Teal", Color(0xFF4DB6AC)),
    BLUE("Blue", Color(0xFF2979FF)),
    PURPLE("Purple", Color(0xFF9575CD)),
    PINK("Pink", Color(0xFFF06292)),
    GREEN("Green", Color(0xFF4CAF50)),
    RED("Red", Color(0xFFE57373)),
    AMBER("Amber", Color(0xFFFFD54F));

    companion object {
        fun fromName(name: String): ThemeColor {
            return values().find { it.colorName == name } ?: ORANGE
        }
    }
}
