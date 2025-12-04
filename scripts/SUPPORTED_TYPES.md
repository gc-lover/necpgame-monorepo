# Поддерживаемые типы данных

## 📄 OpenAPI 3.0 типы

### Основные типы:
- ✅ `string` - строка текста
- ✅ `integer` - целое число
- ✅ `number` - число с плавающей точкой
- ✅ `boolean` - логический тип
- ✅ `array` - массив
- ✅ `object` - объект
- ✅ `null` - null значение

### String форматы:
- ✅ `uuid` - UUID (16 bytes)
- ✅ `date-time` - ISO 8601 дата и время
- ✅ `date` - дата (YYYY-MM-DD)
- ✅ `time` - время
- ✅ `duration` - длительность
- ✅ `email` - email адрес
- ✅ `uri` / `url` - URI/URL
- ✅ `hostname` - имя хоста
- ✅ `ipv4` - IPv4 адрес
- ✅ `ipv6` - IPv6 адрес
- ✅ `password` - пароль
- ✅ `binary` - бинарные данные (base64)
- ✅ `byte` - байты (base64)

### Integer форматы:
- ✅ `int64` - 64-битное целое (8 bytes)
- ✅ `int32` - 32-битное целое (4 bytes) - default
- ✅ `int16` - 16-битное целое (2 bytes)
- ✅ `int8` - 8-битное целое (1 byte)

### Number форматы:
- ✅ `double` / `float64` - двойная точность (8 bytes) - default
- ✅ `float` / `float32` - одинарная точность (4 bytes)

### Специальные:
- ✅ `$ref` - ссылка на другой schema
- ✅ `enum` - перечисление (обрабатывается как string)

## 🗄️ PostgreSQL типы

### UUID:
- ✅ `UUID` - универсальный уникальный идентификатор (16 bytes)

### Текстовые типы:
- ✅ `TEXT` - текст переменной длины
- ✅ `VARCHAR(n)` / `CHARACTER VARYING(n)` - строка переменной длины
- ✅ `CHAR(n)` / `CHARACTER(n)` - строка фиксированной длины
- ✅ `NAME` - внутренний тип PostgreSQL

### Бинарные типы:
- ✅ `BYTEA` - бинарные данные
- ✅ `BLOB` - большой бинарный объект

### JSON типы:
- ✅ `JSONB` - бинарный JSON (оптимизированный)
- ✅ `JSON` - текстовый JSON
- ✅ `XML` - XML данные
- ✅ `HSTORE` - key-value хранилище

### Временные типы:
- ✅ `TIMESTAMP` / `TIMESTAMP WITHOUT TIME ZONE` - временная метка (8 bytes)
- ✅ `TIMESTAMPTZ` / `TIMESTAMP WITH TIME ZONE` - временная метка с TZ (8 bytes)
- ✅ `DATE` - дата (4 bytes)
- ✅ `TIME` / `TIME WITHOUT TIME ZONE` - время
- ✅ `TIMETZ` / `TIME WITH TIME ZONE` - время с TZ
- ✅ `INTERVAL` - интервал времени

### Числовые типы (8 bytes):
- ✅ `BIGINT` / `INT8` - большое целое
- ✅ `BIGSERIAL` / `SERIAL8` - автоинкремент bigint
- ✅ `DOUBLE PRECISION` / `FLOAT8` / `DOUBLE` - двойная точность
- ✅ `NUMERIC(p,s)` / `DECIMAL(p,s)` - точное число (variable)
- ✅ `MONEY` - денежный тип (8 bytes)

### Числовые типы (4 bytes):
- ✅ `INTEGER` / `INT` / `INT4` - целое число
- ✅ `SERIAL` / `SERIAL4` - автоинкремент integer
- ✅ `REAL` / `FLOAT4` / `FLOAT` - одинарная точность

### Числовые типы (2 bytes):
- ✅ `SMALLINT` / `INT2` - малое целое
- ✅ `SMALLSERIAL` / `SERIAL2` - автоинкремент smallint

### Логические типы:
- ✅ `BOOLEAN` / `BOOL` - логический тип (1 byte)

### Битовые типы:
- ✅ `BIT(n)` - битовая строка фиксированной длины
- ✅ `VARBIT(n)` / `BIT VARYING(n)` - битовая строка переменной длины

### Массивы:
- ✅ `ARRAY` - массив любого типа (например, `INTEGER[]`)

### Пространственные типы:
- ✅ `POINT` - точка
- ✅ `LINE` - линия
- ✅ `LSEG` - отрезок
- ✅ `BOX` - прямоугольник
- ✅ `PATH` - путь
- ✅ `POLYGON` - многоугольник
- ✅ `CIRCLE` - круг

### Сетевые типы:
- ✅ `INET` - IP адрес
- ✅ `CIDR` - сеть CIDR
- ✅ `MACADDR` - MAC адрес (6 bytes)
- ✅ `MACADDR8` - MAC адрес (8 bytes)

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

