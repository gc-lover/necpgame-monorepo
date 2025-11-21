# MsQuic C++ Test Client

Простой тестовый клиент для проверки подключения к QUIC серверу через MsQuic.

## Требования

- Visual Studio с C++ компилятором (MSVC)
- MsQuic библиотека (входит в UE 5.7)

## Компиляция и запуск

### Вариант 1: Через Developer Command Prompt for VS

1. Откройте "Developer Command Prompt for VS" или "x64 Native Tools Command Prompt for VS"
2. Перейдите в директорию:
   ```cmd
   cd C:\NECPGAME\services\realtime-gateway-go
   ```
3. Запустите скрипт:
   ```cmd
   test_client_cpp.bat
   ```

### Вариант 2: Вручную

1. Откройте "Developer Command Prompt for VS"
2. Перейдите в директорию:
   ```cmd
   cd C:\NECPGAME\services\realtime-gateway-go
   ```
3. Скомпилируйте:
   ```cmd
   cl.exe /EHsc /I"C:\Program Files\Epic Games\UE_5.7\Engine\Source\ThirdParty\MsQuic\v220\win64\include" test_client_cpp.cpp /link /LIBPATH:"C:\Program Files\Epic Games\UE_5.7\Engine\Source\ThirdParty\MsQuic\v220\win64\lib" msquic.lib ws2_32.lib /OUT:test_client_cpp.exe
   ```
4. Запустите:
   ```cmd
   test_client_cpp.exe
   ```

## Ожидаемый результат

Если сервер запущен и доступен на `127.0.0.1:18080`, клиент должен:
- Успешно подключиться
- Вывести "✓ Successfully connected!"

Если подключение не удалось, будут показаны коды ошибок.



