package com.keyboardtester.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyboardtester.app.model.KeyDefinition
import com.keyboardtester.app.model.KeyEventRecord
import com.keyboardtester.app.model.KeyboardLayouts
import com.keyboardtester.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardTesterScreen(
    pressedKeyCodes: Set<Int>,
    testedKeyCodes: Set<Int>,
    lastKeyEvent: KeyEventRecord?,
    eventHistory: List<KeyEventRecord>,
    soundEnabled: Boolean,
    vibrateEnabled: Boolean,
    interceptBack: Boolean,
    onToggleSound: () -> Unit,
    onToggleVibrate: () -> Unit,
    onToggleInterceptBack: () -> Unit,
    onReset: () -> Unit,
    onExit: () -> Unit
) {
    val totalConfigured = KeyboardLayouts.allConfiguredKeys.size
    val testedCount = testedKeyCodes.size
    val progressPercent = if (totalConfigured > 0) (testedCount * 100 / totalConfigured) else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Bluetooth Keyboard Tester",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (lastKeyEvent != null) "Устройство: ${lastKeyEvent.deviceName}" else "Подключите BT-клавиатуру и нажимайте клавиши",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleSound) {
                        Icon(
                            imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Звук",
                            tint = if (soundEnabled) AccentCyan else TextMuted
                        )
                    }
                    IconButton(onClick = onToggleVibrate) {
                        Icon(
                            imageVector = if (vibrateEnabled) Icons.Default.Vibration else Icons.Default.Smartphone,
                            contentDescription = "Вибрация",
                            tint = if (vibrateEnabled) AccentCyan else TextMuted
                        )
                    }
                    IconButton(onClick = onReset) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Сброс",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onExit) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Выход",
                            tint = WarningRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            // Stats & Telemetry HUD Card
            TelemetryCard(
                lastEvent = lastKeyEvent,
                testedCount = testedCount,
                totalConfigured = totalConfigured,
                progressPercent = progressPercent,
                activeCount = pressedKeyCodes.size
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Visual Keyboard (Horizontal Scrollable for mobile screens)
            Text(
                text = "ИНТЕРАКТИВНАЯ КЛАВИАТУРА",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                val hScrollState = rememberScrollState()
                Column(
                    modifier = Modifier.horizontalScroll(hScrollState),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    KeyboardRowView(KeyboardLayouts.functionRow, pressedKeyCodes, testedKeyCodes)
                    Spacer(modifier = Modifier.height(2.dp))
                    KeyboardRowView(KeyboardLayouts.row1, pressedKeyCodes, testedKeyCodes)
                    KeyboardRowView(KeyboardLayouts.row2, pressedKeyCodes, testedKeyCodes)
                    KeyboardRowView(KeyboardLayouts.row3, pressedKeyCodes, testedKeyCodes)
                    KeyboardRowView(KeyboardLayouts.row4, pressedKeyCodes, testedKeyCodes)
                    KeyboardRowView(KeyboardLayouts.row5, pressedKeyCodes, testedKeyCodes)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend / Status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LegendItem(color = KeyActiveBg, label = "Нажата сейчас")
                    LegendItem(color = KeyTestedBg, label = "Проверена (OK)")
                    LegendItem(color = KeyDefaultBg, label = "Не проверена", border = KeyDefaultBorder)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Перехват Back/Esc",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (interceptBack) AccentCyan else TextMuted
                    )
                    Switch(
                        checked = interceptBack,
                        onCheckedChange = { onToggleInterceptBack() },
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Event History Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ИСТОРИЯ НАЖАТИЙ (${eventHistory.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Event History List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardBackground)
                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (eventHistory.isEmpty()) {
                    item {
                        Text(
                            text = "Нажмите любую клавишу на Bluetooth-клавиатуре...",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(eventHistory, key = { it.id }) { item ->
                        HistoryRowItem(item)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TelemetryCard(
    lastEvent: KeyEventRecord?,
    testedCount: Int,
    totalConfigured: Int,
    progressPercent: Int,
    activeCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Row: Big Key Display + Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ПОСЛЕДНЯЯ КЛАВИША",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = lastEvent?.keyName ?: "—",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (lastEvent?.action == "DOWN") KeyActiveBg else Color.White
                    )
                }

                // Progress Block
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "ПРОГРЕСС",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = "$testedCount / $totalConfigured ($progressPercent%)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = KeyTestedBg
                    )
                    if (activeCount > 0) {
                        Text(
                            text = "Удерживается: $activeCount кл.",
                            style = MaterialTheme.typography.labelSmall,
                            color = KeyActiveBg,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoTag(label = "KeyCode", value = lastEvent?.keyCode?.toString() ?: "—")
                InfoTag(label = "ScanCode", value = lastEvent?.scanCode?.toString() ?: "—")
                InfoTag(label = "Символ", value = lastEvent?.char?.ifEmpty { "—" } ?: "—")
                InfoTag(label = "Действие", value = lastEvent?.action ?: "—", isAccent = lastEvent?.action == "DOWN")
            }

            if (lastEvent?.modifiers?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Модификаторы: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Text(
                        text = lastEvent.modifiers,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan
                    )
                }
            }

            // Chattering warning if detected
            if (lastEvent?.isChatter == true) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(WarningRed.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Chatter",
                        tint = WarningRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Внимание: обнаружен дребезг контактов (двойной клик за ${lastEvent.latencyMs}мс)!",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarningRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun InfoTag(label: String, value: String, isAccent: Boolean = false) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (isAccent) AccentCyan else Color.White
        )
    }
}

@Composable
fun KeyboardRowView(
    row: List<KeyDefinition>,
    pressedCodes: Set<Int>,
    testedCodes: Set<Int>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        row.forEach { keyDef ->
            val isPressed = pressedCodes.contains(keyDef.keyCode)
            val isTested = testedCodes.contains(keyDef.keyCode)

            val bgColor = when {
                isPressed -> KeyActiveBg
                isTested -> KeyTestedBg
                else -> KeyDefaultBg
            }

            val textColor = when {
                isPressed -> KeyActiveText
                isTested -> KeyTestedText
                else -> KeyDefaultText
            }

            val borderColor = when {
                isPressed -> KeyActiveBg
                isTested -> KeyTestedBg
                else -> KeyDefaultBorder
            }

            val baseWidth = 38.dp
            val keyWidth = baseWidth * keyDef.widthWeight

            Box(
                modifier = Modifier
                    .width(keyWidth)
                    .height(38.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = keyDef.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = if (keyDef.label.length > 4) 9.sp else 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, border: Color? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
                .then(
                    if (border != null) Modifier.border(1.dp, border, RoundedCornerShape(3.dp))
                    else Modifier
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}

private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

@Composable
fun HistoryRowItem(record: KeyEventRecord) {
    val timeStr = remember(record.timeMillis) { timeFormat.format(Date(record.timeMillis)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = timeStr,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
            )
            Text(
                text = record.keyName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "[код ${record.keyCode}]",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (record.isChatter) {
                Text(
                    text = "ДРЕБЕЗГ",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarningRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (record.action == "DOWN") AccentCyan.copy(alpha = 0.2f) else BorderColor)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = record.action,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (record.action == "DOWN") AccentCyan else TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp
                )
            }
        }
    }
}
