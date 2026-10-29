# Bluetooth Keyboard Tester для Android 14

Полнофункциональное приложение для проверки работоспособности аппаратных Bluetooth-клавиатур на Android 14 (API 34).

## 🚀 Возможности
- **Перехват всех клавиш на уровне системы**: Перехватывает `dispatchKeyEvent` в `MainActivity`, благодаря чему клавиши `Back`, `Esc`, `Tab`, `Space`, `Volume` не закрывают приложение и не вызывают системных действий во время тестирования.
- **Интерактивная виртуальная клавиатура**: ANSI/TKL раскладка с подсветкой:
  - 🟡 **Желтый/Оранжевый**: клавиша зажата прямо сейчас.
  - 🟢 **Изумрудный зеленый**: клавиша проверена и работает (OK).
  - ⚪ **Серый**: клавиша еще не нажималась.
- **Детектор дребезга контактов (Key Chatter / Double-click)**: Фиксирует повторные нажатия с задержкой менее 45 мс, помогая выявить окислившиеся или неисправные механические/мембранные свитчи.
- **Телеметрия в реальном времени**:
  - Android KeyCode (числовой код Android)
  - Hardware ScanCode (скан-код железа от HID контроллера клавиатуры)
  - Символ Unicode
  - Имя подключенного Bluetooth-устройства (например, "Logitech K380", "Keychron")
  - Модификаторы: `Ctrl`, `Alt`, `Shift`, `Meta (Win/Cmd)`, `CapsLock`, `NumLock`
- **Звуковой и тактильный отклик**: Звуковой щелчок и виброотклик (с использованием `VibratorManager` Android 14).
- **Счетчик и история нажатий**: Журнал событий с точным временем в миллисекундах.

---

## 📁 Структура проекта
```
bluetooth_keyboard_tester/
├── app/
│   ├── src/main/java/com/keyboardtester/app/
│   │   ├── MainActivity.kt               # Перехват KeyEvent, Bluetooth HID, звук, вибро
│   │   ├── model/KeyModels.kt            # Раскладка ANSI/TKL, модель событий
│   │   └── ui/
│   │       ├── KeyboardTesterScreen.kt   # Интерфейс на Jetpack Compose + Material 3
│   │       └── theme/                    # Тема оформления
│   ├── src/main/AndroidManifest.xml
│   └── build.gradle.kts                  # targetSdk = 34 (Android 14)
├── web_tester/
│   └── index.html                        # Мгновенная версия для Chrome на Android
└── .github/workflows/build-apk.yml        # Автоматическая сборка APK через GitHub Actions
```

---

## 🛠 Способы сборки и запуска

### Вариант 1: Через Android Studio
1. Откройте папку `bluetooth_keyboard_tester` в **Android Studio**.
2. Подключите Android 14 устройство по USB или Wi-Fi (режим отладки).
3. Нажмите кнопку **Run (Shift + F10)**.

### Вариант 2: Бесплатная автосборка APK через GitHub Actions (без Android Studio)
В проект уже включен скрипт `.github/workflows/build-apk.yml`:
1. Загрузите проект в репозиторий на GitHub.
2. Во вкладке **Actions** запустится сборка.
3. Через 2 минуты скачайте готовый `app-debug.apk` и установите на телефон!

### Вариант 3: Мгновенная проверка через Chrome на телефоне (PWA)
В Android 14 браузер Chrome имеет полный доступ к событиям подключенной Bluetooth-клавиатуры:
1. Откройте файл `web_tester/index.html` на телефоне или запустите локальный сервер (например: `python -m http.server 8080` в папке `web_tester`).
2. В Chrome на телефоне нажмите **Меню -> Добавить на главный экран** — приложение установится как полноценный PWA и будет работать автономно!
