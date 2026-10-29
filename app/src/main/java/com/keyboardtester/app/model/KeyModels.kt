package com.keyboardtester.app.model

import android.view.KeyEvent

data class KeyDefinition(
    val keyCode: Int,
    val label: String,
    val widthWeight: Float = 1.0f,
    val subLabel: String? = null
)

data class KeyEventRecord(
    val id: Long = System.nanoTime(),
    val timeMillis: Long = System.currentTimeMillis(),
    val keyName: String,
    val keyCode: Int,
    val scanCode: Int,
    val action: String, // "DOWN" or "UP"
    val char: String,
    val modifiers: String,
    val deviceName: String,
    val isChatter: Boolean = false,
    val latencyMs: Long? = null
)

object KeyboardLayouts {
    // 80% / TKL Layout definitions
    val functionRow = listOf(
        KeyDefinition(KeyEvent.KEYCODE_ESCAPE, "ESC", 1.0f),
        KeyDefinition(KeyEvent.KEYCODE_F1, "F1"),
        KeyDefinition(KeyEvent.KEYCODE_F2, "F2"),
        KeyDefinition(KeyEvent.KEYCODE_F3, "F3"),
        KeyDefinition(KeyEvent.KEYCODE_F4, "F4"),
        KeyDefinition(KeyEvent.KEYCODE_F5, "F5"),
        KeyDefinition(KeyEvent.KEYCODE_F6, "F6"),
        KeyDefinition(KeyEvent.KEYCODE_F7, "F7"),
        KeyDefinition(KeyEvent.KEYCODE_F8, "F8"),
        KeyDefinition(KeyEvent.KEYCODE_F9, "F9"),
        KeyDefinition(KeyEvent.KEYCODE_F10, "F10"),
        KeyDefinition(KeyEvent.KEYCODE_F11, "F11"),
        KeyDefinition(KeyEvent.KEYCODE_F12, "F12"),
        KeyDefinition(KeyEvent.KEYCODE_SYSRQ, "PRT"),
        KeyDefinition(KeyEvent.KEYCODE_SCROLL_LOCK, "SCRL"),
        KeyDefinition(KeyEvent.KEYCODE_BREAK, "PAUSE")
    )

    val row1 = listOf(
        KeyDefinition(KeyEvent.KEYCODE_GRAVE, "` ~"),
        KeyDefinition(KeyEvent.KEYCODE_1, "1 !"),
        KeyDefinition(KeyEvent.KEYCODE_2, "2 @"),
        KeyDefinition(KeyEvent.KEYCODE_3, "3 #"),
        KeyDefinition(KeyEvent.KEYCODE_4, "4 $"),
        KeyDefinition(KeyEvent.KEYCODE_5, "5 %"),
        KeyDefinition(KeyEvent.KEYCODE_6, "6 ^"),
        KeyDefinition(KeyEvent.KEYCODE_7, "7 &"),
        KeyDefinition(KeyEvent.KEYCODE_8, "8 *"),
        KeyDefinition(KeyEvent.KEYCODE_9, "9 ("),
        KeyDefinition(KeyEvent.KEYCODE_0, "0 )"),
        KeyDefinition(KeyEvent.KEYCODE_MINUS, "- _"),
        KeyDefinition(KeyEvent.KEYCODE_EQUALS, "= +"),
        KeyDefinition(KeyEvent.KEYCODE_DEL, "BACK", 1.8f),
        KeyDefinition(KeyEvent.KEYCODE_INSERT, "INS"),
        KeyDefinition(KeyEvent.KEYCODE_MOVE_HOME, "HOME"),
        KeyDefinition(KeyEvent.KEYCODE_PAGE_UP, "PGUP")
    )

    val row2 = listOf(
        KeyDefinition(KeyEvent.KEYCODE_TAB, "TAB", 1.5f),
        KeyDefinition(KeyEvent.KEYCODE_Q, "Q"),
        KeyDefinition(KeyEvent.KEYCODE_W, "W"),
        KeyDefinition(KeyEvent.KEYCODE_E, "E"),
        KeyDefinition(KeyEvent.KEYCODE_R, "R"),
        KeyDefinition(KeyEvent.KEYCODE_T, "T"),
        KeyDefinition(KeyEvent.KEYCODE_Y, "Y"),
        KeyDefinition(KeyEvent.KEYCODE_U, "U"),
        KeyDefinition(KeyEvent.KEYCODE_I, "I"),
        KeyDefinition(KeyEvent.KEYCODE_O, "O"),
        KeyDefinition(KeyEvent.KEYCODE_P, "P"),
        KeyDefinition(KeyEvent.KEYCODE_LEFT_BRACKET, "[ {"),
        KeyDefinition(KeyEvent.KEYCODE_RIGHT_BRACKET, "] }"),
        KeyDefinition(KeyEvent.KEYCODE_BACKSLASH, "\\ |", 1.3f),
        KeyDefinition(KeyEvent.KEYCODE_FORWARD_DEL, "DEL"),
        KeyDefinition(KeyEvent.KEYCODE_MOVE_END, "END"),
        KeyDefinition(KeyEvent.KEYCODE_PAGE_DOWN, "PGDN")
    )

    val row3 = listOf(
        KeyDefinition(KeyEvent.KEYCODE_CAPS_LOCK, "CAPS", 1.8f),
        KeyDefinition(KeyEvent.KEYCODE_A, "A"),
        KeyDefinition(KeyEvent.KEYCODE_S, "S"),
        KeyDefinition(KeyEvent.KEYCODE_D, "D"),
        KeyDefinition(KeyEvent.KEYCODE_F, "F"),
        KeyDefinition(KeyEvent.KEYCODE_G, "G"),
        KeyDefinition(KeyEvent.KEYCODE_H, "H"),
        KeyDefinition(KeyEvent.KEYCODE_J, "J"),
        KeyDefinition(KeyEvent.KEYCODE_K, "K"),
        KeyDefinition(KeyEvent.KEYCODE_L, "L"),
        KeyDefinition(KeyEvent.KEYCODE_SEMICOLON, "; :"),
        KeyDefinition(KeyEvent.KEYCODE_APOSTROPHE, "' \""),
        KeyDefinition(KeyEvent.KEYCODE_ENTER, "ENTER", 2.2f)
    )

    val row4 = listOf(
        KeyDefinition(KeyEvent.KEYCODE_SHIFT_LEFT, "SHIFT", 2.3f),
        KeyDefinition(KeyEvent.KEYCODE_Z, "Z"),
        KeyDefinition(KeyEvent.KEYCODE_X, "X"),
        KeyDefinition(KeyEvent.KEYCODE_C, "C"),
        KeyDefinition(KeyEvent.KEYCODE_V, "V"),
        KeyDefinition(KeyEvent.KEYCODE_B, "B"),
        KeyDefinition(KeyEvent.KEYCODE_N, "N"),
        KeyDefinition(KeyEvent.KEYCODE_M, "M"),
        KeyDefinition(KeyEvent.KEYCODE_COMMA, ", <"),
        KeyDefinition(KeyEvent.KEYCODE_PERIOD, ". >"),
        KeyDefinition(KeyEvent.KEYCODE_SLASH, "/ ?"),
        KeyDefinition(KeyEvent.KEYCODE_SHIFT_RIGHT, "SHIFT", 2.3f),
        KeyDefinition(KeyEvent.KEYCODE_DPAD_UP, "▲")
    )

    val row5 = listOf(
        KeyDefinition(KeyEvent.KEYCODE_CTRL_LEFT, "CTRL", 1.3f),
        KeyDefinition(KeyEvent.KEYCODE_META_LEFT, "WIN", 1.2f),
        KeyDefinition(KeyEvent.KEYCODE_ALT_LEFT, "ALT", 1.2f),
        KeyDefinition(KeyEvent.KEYCODE_SPACE, "SPACE", 5.5f),
        KeyDefinition(KeyEvent.KEYCODE_ALT_RIGHT, "ALT", 1.2f),
        KeyDefinition(KeyEvent.KEYCODE_META_RIGHT, "WIN", 1.2f),
        KeyDefinition(KeyEvent.KEYCODE_MENU, "MENU", 1.2f),
        KeyDefinition(KeyEvent.KEYCODE_CTRL_RIGHT, "CTRL", 1.3f),
        KeyDefinition(KeyEvent.KEYCODE_DPAD_LEFT, "◄"),
        KeyDefinition(KeyEvent.KEYCODE_DPAD_DOWN, "▼"),
        KeyDefinition(KeyEvent.KEYCODE_DPAD_RIGHT, "►")
    )

    val allConfiguredKeys: Set<Int> by lazy {
        (functionRow + row1 + row2 + row3 + row4 + row5).map { it.keyCode }.toSet()
    }
}
