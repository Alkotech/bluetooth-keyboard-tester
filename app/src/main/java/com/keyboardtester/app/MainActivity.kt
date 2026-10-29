package com.keyboardtester.app

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.keyboardtester.app.model.KeyEventRecord
import com.keyboardtester.app.ui.KeyboardTesterScreen
import com.keyboardtester.app.ui.theme.BluetoothKeyboardTesterTheme

class MainActivity : ComponentActivity() {

    private var pressedKeyCodes = mutableStateOf(setOf<Int>())
    private var testedKeyCodes = mutableStateOf(setOf<Int>())
    private var lastKeyEventState = mutableStateOf<KeyEventRecord?>(null)
    private val eventHistory = mutableStateListOf<KeyEventRecord>()

    private var soundEnabled = mutableStateOf(true)
    private var vibrateEnabled = mutableStateOf(true)
    private var interceptBack = mutableStateOf(true)

    private var toneGenerator: ToneGenerator? = null
    private val lastReleaseTimes = mutableMapOf<Int, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
        } catch (_: Exception) {}

        setContent {
            BluetoothKeyboardTesterTheme {
                KeyboardTesterScreen(
                    pressedKeyCodes = pressedKeyCodes.value,
                    testedKeyCodes = testedKeyCodes.value,
                    lastKeyEvent = lastKeyEventState.value,
                    eventHistory = eventHistory,
                    soundEnabled = soundEnabled.value,
                    vibrateEnabled = vibrateEnabled.value,
                    interceptBack = interceptBack.value,
                    onToggleSound = { soundEnabled.value = !soundEnabled.value },
                    onToggleVibrate = { vibrateEnabled.value = !vibrateEnabled.value },
                    onToggleInterceptBack = { interceptBack.value = !interceptBack.value },
                    onReset = {
                        pressedKeyCodes.value = emptySet()
                        testedKeyCodes.value = emptySet()
                        lastKeyEventState.value = null
                        eventHistory.clear()
                    },
                    onExit = {
                        finish()
                    }
                )
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode
        val action = when (event.action) {
            KeyEvent.ACTION_DOWN -> "DOWN"
            KeyEvent.ACTION_UP -> "UP"
            else -> "OTHER"
        }

        val now = System.currentTimeMillis()
        var isChatter = false
        var chatterLatency: Long? = null

        if (event.action == KeyEvent.ACTION_DOWN) {
            pressedKeyCodes.value = pressedKeyCodes.value + keyCode
            testedKeyCodes.value = testedKeyCodes.value + keyCode

            // Detect Switch Chatter / Bouncing (pressed again within 45ms of last release)
            val lastReleased = lastReleaseTimes[keyCode]
            if (lastReleased != null) {
                val diff = now - lastReleased
                if (diff in 1..45 && event.repeatCount == 0) {
                    isChatter = true
                    chatterLatency = diff
                }
            }

            // Sound and haptic feedback
            if (event.repeatCount == 0) {
                triggerAudioFeedback()
                triggerHapticFeedback()
            }
        } else if (event.action == KeyEvent.ACTION_UP) {
            pressedKeyCodes.value = pressedKeyCodes.value - keyCode
            lastReleaseTimes[keyCode] = now
        }

        // Modifiers formatting
        val modsList = mutableListOf<String>()
        if (event.isCtrlPressed) modsList.add("CTRL")
        if (event.isAltPressed) modsList.add("ALT")
        if (event.isShiftPressed) modsList.add("SHIFT")
        if (event.isMetaPressed) modsList.add("META/WIN")
        if (event.isCapsLockOn) modsList.add("CAPS")
        if (event.isNumLockOn) modsList.add("NUM")

        val keyName = KeyEvent.keyCodeToString(keyCode).removePrefix("KEYCODE_")
        val charValue = if (event.unicodeChar != 0) {
            event.unicodeChar.toChar().toString()
        } else {
            ""
        }

        val deviceName = event.device?.name ?: "Bluetooth/HID Keyboard"

        val record = KeyEventRecord(
            keyName = keyName,
            keyCode = keyCode,
            scanCode = event.scanCode,
            action = action,
            char = charValue,
            modifiers = modsList.joinToString(" + "),
            deviceName = deviceName,
            isChatter = isChatter,
            latencyMs = chatterLatency
        )

        lastKeyEventState.value = record
        eventHistory.add(0, record)
        if (eventHistory.size > 80) {
            eventHistory.removeLastOrNull()
        }

        // If interceptBack is true, absorb BACK and ESC so the app won't close while testing them
        if (interceptBack.value && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)) {
            return true
        }

        // Return true to consume the key so standard system behavior (like scrolling or volume) doesn't interfere
        return true
    }

    private fun triggerAudioFeedback() {
        if (!soundEnabled.value) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 15)
        } catch (_: Exception) {}
    }

    private fun triggerHapticFeedback() {
        if (!vibrateEnabled.value) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(15)
            }
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        toneGenerator?.release()
        toneGenerator = null
    }
}
