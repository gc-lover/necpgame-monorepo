# Step-by-Step Backend Setup (Part 1: Setup & Database)

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:47  
**Часть:** 1 из 3

**api-readiness:** not-applicable

---

## Навигация

- **Part 1:** Setup & Database (этот файл)
- **Part 2:** [Backend Code](./step-by-step-part2-code.md)
- **Part 3:** [Testing & Deploy](./step-by-step-part3-testing.md)

---

## 🎯 PREREQUISITES

### Требования

- [x] Java 17+
- [x] Spring Boot 3.x
- [x] PostgreSQL 14+
- [x] Redis
- [x] Maven 3.8+

### Проверка

```bash
java -version  # Should be 17+
psql --version  # Should be 14+
redis-cli ping  # Should return PONG
mvn -version  # Should be 3.8+
```

---

## 📋 STEP 1: Подготовка окружения (10 минут)

### 1.1 PostgreSQL

```bash
createdb necpgame

# Или через psql
psql -U postgres
CREATE DATABASE necpgame;
\q
```

### 1.2 Redis

```bash
# Linux/Mac
redis-server

# Windows
docker run -d -p 6379:6379 redis:latest
```

### 1.3 Проверка

```bash
psql -d necpgame -c "SELECT version();"
redis-cli ping
```

**✅ Checkpoint:** БД и Redis работают

---

## 📋 STEP 2: SQL Миграции (15 минут)

### 2.1 Скопировать миграции

```bash
cp .BRAIN/04-narrative/narrative-coherence/phase4-database/migrations/*.sql \
   BACK-JAVA/src/main/resources/db/migration/narrative/
```

### 2.2 Настроить переменные

```bash
# Linux/Mac
export DB_NAME=necpgame
export DB_USER=postgres
export DB_PASSWORD=your_password
export DB_HOST=localhost
export DB_PORT=5432

# Windows
$env:DB_NAME = "necpgame"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_password"
```

### 2.3 Применить

```bash
# Auto-apply
./apply-all-migrations.sh  # Linux
.\apply-all-migrations.ps1  # Windows

# Или вручную
psql -d necpgame -U postgres -f 001-expand-quests-table.sql
psql -d necpgame -U postgres -f 002-create-quest-branches.sql
psql -d necpgame -U postgres -f 003-create-dialogue-system.sql
psql -d necpgame -U postgres -f 004-create-player-systems.sql
psql -d necpgame -U postgres -f 005-create-world-state-system.sql
```

### 2.4 Проверка

```bash
psql -d necpgame -U postgres

\dt quest*
\dt player*
\dt server*

# Должно быть 13 таблиц
\q
```

**✅ Checkpoint:** 13 таблиц созданы

---

## 📋 STEP 3: Export данных (20 минут)

### 3.1 Установить зависимости

```bash
pip install pyyaml
# ИЛИ
npm install js-yaml
```

### 3.2 Запустить

```bash
cd .BRAIN/04-narrative/narrative-coherence/phase3-event-matrix/export

python convert-quest-graph.py
# ИЛИ
node convert-quest-graph.js
```

### 3.3 Проверить

```bash
ls -lh export/

# Должны быть:
# side-quests-matrix.json (~50KB)
# quest-triggers.json (~30KB)
# quest-blockers.json (~40KB)
# quest-dependencies-full.json (~100KB)
```

### 3.4 Скопировать в backend

```bash
cp .BRAIN/04-narrative/narrative-coherence/phase3-event-matrix/export/*.json \
   BACK-JAVA/src/main/resources/data/narrative/
```

**✅ Checkpoint:** 4 JSON файла в resources

---

## 📋 STEP 4: Dependencies (5 минут)

### 4.1 Открыть pom.xml

```bash
cd BACK-JAVA
vi pom.xml
```

### 4.2 Добавить

```xml
<!-- Hibernate Types для JSONB -->
<dependency>
    <groupId>com.vladmihalcea</groupId>
    <artifactId>hibernate-types-55</artifactId>
    <version>2.21.1</version>
</dependency>

<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- WebSocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 4.3 Install

```bash
mvn clean install
```

**✅ Checkpoint:** Dependencies готовы

---

## 📋 STEP 5: Структура пакетов (5 минут)

```bash
cd BACK-JAVA/src/main/java/com/necpgame/

mkdir -p narrative/entity
mkdir -p narrative/repository
mkdir -p narrative/service
mkdir -p narrative/controller
mkdir -p narrative/dto
mkdir -p narrative/config
```

**Структура:**
```
com.necpgame.narrative/
├── entity/
├── repository/
├── service/
├── controller/
├── dto/
└── config/
```

**✅ Checkpoint:** Структура готова

---

## ➡️ Продолжение

**Следующий шаг:** [Part 2 - Backend Code](./step-by-step-part2-code.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:47) - Part 1 создан (Setup & Database)

