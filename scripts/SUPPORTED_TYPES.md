# Поддерживаемые типы данных

## 📄 OpenAPI 3.0 типы

### Основные типы:
- OK `string` - строка текста
- OK `integer` - целое число
- OK `number` - число с плавающей точкой
- OK `boolean` - логический тип
- OK `array` - массив
- OK `object` - объект
- OK `null` - null значение

### String форматы:
- OK `uuid` - UUID (16 bytes)
- OK `date-time` - ISO 8601 дата и время
- OK `date` - дата (YYYY-MM-DD)
- OK `time` - время
- OK `duration` - длительность
- OK `email` - email адрес
- OK `uri` / `url` - URI/URL
- OK `hostname` - имя хоста
- OK `ipv4` - IPv4 адрес
- OK `ipv6` - IPv6 адрес
- OK `password` - пароль
- OK `binary` - бинарные данные (base64)
- OK `byte` - байты (base64)

### Integer форматы:
- OK `int64` - 64-битное целое (8 bytes)
- OK `int32` - 32-битное целое (4 bytes) - default
- OK `int16` - 16-битное целое (2 bytes)
- OK `int8` - 8-битное целое (1 byte)

### Number форматы:
- OK `double` / `float64` - двойная точность (8 bytes) - default
- OK `float` / `float32` - одинарная точность (4 bytes)

### Специальные:
- OK `$ref` - ссылка на другой schema
- OK `enum` - перечисление (обрабатывается как string)

## 🗄️ PostgreSQL типы

### UUID:
- OK `UUID` - универсальный уникальный идентификатор (16 bytes)

### Текстовые типы:
- OK `TEXT` - текст переменной длины
- OK `VARCHAR(n)` / `CHARACTER VARYING(n)` - строка переменной длины
- OK `CHAR(n)` / `CHARACTER(n)` - строка фиксированной длины
- OK `NAME` - внутренний тип PostgreSQL

### Бинарные типы:
- OK `BYTEA` - бинарные данные
- OK `BLOB` - большой бинарный объект

### JSON типы:
- OK `JSONB` - бинарный JSON (оптимизированный)
- OK `JSON` - текстовый JSON
- OK `XML` - XML данные
- OK `HSTORE` - key-value хранилище

### Временные типы:
- OK `TIMESTAMP` / `TIMESTAMP WITHOUT TIME ZONE` - временная метка (8 bytes)
- OK `TIMESTAMPTZ` / `TIMESTAMP WITH TIME ZONE` - временная метка с TZ (8 bytes)
- OK `DATE` - дата (4 bytes)
- OK `TIME` / `TIME WITHOUT TIME ZONE` - время
- OK `TIMETZ` / `TIME WITH TIME ZONE` - время с TZ
- OK `INTERVAL` - интервал времени

### Числовые типы (8 bytes):
- OK `BIGINT` / `INT8` - большое целое
- OK `BIGSERIAL` / `SERIAL8` - автоинкремент bigint
- OK `DOUBLE PRECISION` / `FLOAT8` / `DOUBLE` - двойная точность
- OK `NUMERIC(p,s)` / `DECIMAL(p,s)` - точное число (variable)
- OK `MONEY` - денежный тип (8 bytes)

### Числовые типы (4 bytes):
- OK `INTEGER` / `INT` / `INT4` - целое число
- OK `SERIAL` / `SERIAL4` - автоинкремент integer
- OK `REAL` / `FLOAT4` / `FLOAT` - одинарная точность

### Числовые типы (2 bytes):
- OK `SMALLINT` / `INT2` - малое целое
- OK `SMALLSERIAL` / `SERIAL2` - автоинкремент smallint

### Логические типы:
- OK `BOOLEAN` / `BOOL` - логический тип (1 byte)

### Битовые типы:
- OK `BIT(n)` - битовая строка фиксированной длины
- OK `VARBIT(n)` / `BIT VARYING(n)` - битовая строка переменной длины

### Массивы:
- OK `ARRAY` - массив любого типа (например, `INTEGER[]`)

### Пространственные типы:
- OK `POINT` - точка
- OK `LINE` - линия
- OK `LSEG` - отрезок
- OK `BOX` - прямоугольник
- OK `PATH` - путь
- OK `POLYGON` - многоугольник
- OK `CIRCLE` - круг

### Сетевые типы:
- OK `INET` - IP адрес
- OK `CIDR` - сеть CIDR
- OK `MACADDR` - MAC адрес (6 bytes)
- OK `MACADDR8` - MAC адрес (8 bytes)

## 📊 Порядок сортировки

### OpenAPI (по размеру в Go):
1. **UUID** (16 bytes)
2. **Binary/Byte** (variable, большой)
3. **String** (16+ bytes)
4. **String formats** (email, uri, etc.)
5. **Date-time formats** (date-time, date, time)
6. **$ref** (8 bytes pointer)
7. **Object** (8-24 bytes)
8. **Array** (24 bytes slice header)
9. **int64/float64** (8 bytes)
10. **int32/float32** (4 bytes)
11. **int16** (2 bytes)
12. **int8** (1 byte)
13. **Boolean** (1 byte)
14. **Null**

### PostgreSQL (по размеру):
1. **UUID** (16 bytes)
2. **TEXT/VARCHAR** (variable)
3. **BYTEA/BLOB** (variable)
4. **JSONB/JSON** (variable)
5. **ARRAY** (variable)
6. **Spatial types** (variable)
7. **Network types** (variable)
8. **TIMESTAMP** (8 bytes)
9. **DATE/TIME** (4-8 bytes)
10. **BIGINT** (8 bytes)
11. **NUMERIC/DECIMAL** (variable)
12. **DOUBLE PRECISION** (8 bytes)
13. **INTEGER** (4 bytes)
14. **REAL** (4 bytes)
15. **SMALLINT** (2 bytes)
16. **BOOLEAN** (1 byte)
17. **BIT** (variable)

## 🔧 Примечания

- Все типы обрабатываются автоматически
- Неизвестные типы получают порядок 999 (в конце)
- PRIMARY KEY всегда остается первым в таблицах
- Constraints (FOREIGN KEY, UNIQUE, CHECK) сохраняются

