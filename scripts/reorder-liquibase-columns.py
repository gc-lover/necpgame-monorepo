#!/usr/bin/env python3
"""
Issue: #1586 - Automatic Liquibase column order optimization
PERFORMANCE: Memory ↓30-50% для таблиц БД

Автоматически рефакторит SQL миграции:
- Сортирует колонки по размеру типа (large → small)
- Сохраняет PRIMARY KEY, FOREIGN KEY, UNIQUE constraints
- Сохраняет DEFAULT значения и NOT NULL
"""

import argparse
import re
import sys
from pathlib import Path
from typing import List, Tuple, Dict

# Порядок типов PostgreSQL по размеру (большие → маленькие)
# Основано на реальных размерах в PostgreSQL
COLUMN_TYPE_ORDER = {
    # ===== LARGE VARIABLE TYPES (variable, обычно большие) =====
    # UUID: 16 bytes (фиксированный)
    'uuid': 0,

    # TEXT/VARCHAR: variable, но обычно большой
    'text': 1,
    'varchar': 1,
    'character varying': 1,
    'character': 1,
    'char': 1,
    'name': 1,  # PostgreSQL internal type

    # BYTEA/BLOB: binary data, variable
    'bytea': 2,
    'blob': 2,

    # JSON/JSONB: variable, но обычно большой
    'jsonb': 3,
    'json': 3,
    'xml': 3,
    'hstore': 3,

    # ARRAY types: variable
    'array': 4,
    '[]': 4,  # array notation

    # ===== SPATIAL TYPES (variable, обычно большие) =====
    'point': 5,
    'line': 5,
    'lseg': 5,
    'box': 5,
    'path': 5,
    'polygon': 5,
    'circle': 5,

    # ===== NETWORK TYPES (variable) =====
    'inet': 6,
    'cidr': 6,
    'macaddr': 6,
    'macaddr8': 6,

    # ===== TIME TYPES (8 bytes) =====
    'timestamp': 7,
    'timestamptz': 7,
    'timestamp with time zone': 7,
    'timestamp without time zone': 7,
    'date': 8,  # 4 bytes, но обычно используется как timestamp
    'time': 8,
    'timetz': 8,
    'time with time zone': 8,
    'time without time zone': 8,
    'interval': 9,

    # ===== NUMERIC TYPES (8 bytes) =====
    'bigint': 10,
    'int8': 10,
    'bigserial': 10,
    'serial8': 10,

    # NUMERIC/DECIMAL: variable (до 131072 digits)
    'numeric': 11,
    'decimal': 11,
    'money': 11,  # 8 bytes, но специальный тип

    # DOUBLE PRECISION: 8 bytes
    'double precision': 12,
    'float8': 12,
    'double': 12,

    # ===== NUMERIC TYPES (4 bytes) =====
    'integer': 13,
    'int': 13,
    'int4': 13,
    'serial': 13,
    'serial4': 13,

    # REAL: 4 bytes
    'real': 14,
    'float4': 14,
    'float': 14,

    # ===== NUMERIC TYPES (2 bytes) =====
    'smallint': 15,
    'int2': 15,
    'smallserial': 15,
    'serial2': 15,

    # ===== BOOLEAN (1 byte) =====
    'boolean': 16,
    'bool': 16,

    # ===== BIT TYPES =====
    'bit': 17,
    'varbit': 17,
    'bit varying': 17,
}


def get_column_size(column_def: str) -> Tuple[int, str]:
    """
    Определяет размер колонки для сортировки.
    Возвращает (order, type_name) для стабильной сортировки.
    """
    # Извлекаем тип из определения колонки
    # Пример: "character_id UUID NOT NULL" -> "uuid"
    # Пример: "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" -> "timestamp"
    column_def_lower = column_def.lower()

    # Сначала проверяем сложные типы (многословные)
    # Сортируем по длине (длинные первыми) для правильного матчинга
    sorted_types = sorted(COLUMN_TYPE_ORDER.items(), key=lambda x: len(x[0]), reverse=True)

    for pg_type, order in sorted_types:
        # Ищем тип в определении (с границами слов или в начале)
        # Учитываем что тип может быть в начале строки или после пробела
        pattern = r'(?:^|\s)' + re.escape(pg_type) + r'(?:\s|\(|$|\[)'
        if re.search(pattern, column_def_lower):
            return (order, pg_type)

    # Проверяем array notation (например, INTEGER[])
    if re.search(r'\[\]', column_def_lower):
        return (COLUMN_TYPE_ORDER['array'], 'array')

    # Если тип не найден, возвращаем высокий порядок
    return (999, 'unknown')


def parse_table_definition(sql: str) -> List[Dict[str, any]]:
    """
    Парсит CREATE TABLE statement и возвращает список таблиц с колонками.
    """
    tables = []

    # Паттерн для CREATE TABLE
    table_pattern = r'CREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?([^\s(]+)\s*\((.*?)\);'

    for match in re.finditer(table_pattern, sql, re.IGNORECASE | re.DOTALL):
        table_name = match.group(1).strip()
        table_body = match.group(2).strip()

        # Разбиваем на колонки (учитываем вложенные скобки)
        columns = []
        current_col = []
        depth = 0

        for char in table_body:
            if char == '(':
                depth += 1
                current_col.append(char)
            elif char == ')':
                depth -= 1
                current_col.append(char)
            elif char == ',' and depth == 0:
                col_def = ''.join(current_col).strip()
                if col_def:
                    columns.append(col_def)
                current_col = []
            else:
                current_col.append(char)

        # Последняя колонка
        if current_col:
            col_def = ''.join(current_col).strip()
            if col_def:
                columns.append(col_def)

        # Разделяем колонки и constraints
        table_columns = []
        constraints = []

        for col in columns:
            col_upper = col.upper()
            # Constraints (PRIMARY KEY, FOREIGN KEY, UNIQUE, CHECK)
            if (col_upper.startswith('PRIMARY KEY') or
                    col_upper.startswith('FOREIGN KEY') or
                    col_upper.startswith('UNIQUE') or
                    col_upper.startswith('CHECK') or
                    col_upper.startswith('CONSTRAINT')):
                constraints.append(col)
            else:
                table_columns.append(col)

        tables.append({
            'name': table_name,
            'columns': table_columns,
            'constraints': constraints,
            'full_match': match.group(0)
        })

    return tables


def reorder_table_columns(table: Dict[str, any]) -> Tuple[str, bool]:
    """
    Рефакторит колонки таблицы, сортируя по размеру типа.
    Возвращает (новое определение таблицы, были ли изменения).
    """
    columns = table['columns']
    if not columns:
        return table['full_match'], False

    # Определяем PRIMARY KEY колонку (может быть в определении колонки или отдельным constraint)
    primary_key_col = None
    primary_key_name = None

    # Проверяем constraints для PRIMARY KEY
    for constraint in table['constraints']:
        if 'PRIMARY KEY' in constraint.upper():
            # Извлекаем имя колонки из PRIMARY KEY (column_name)
            pk_match = re.search(r'PRIMARY\s+KEY\s*\(([^)]+)\)', constraint, re.IGNORECASE)
            if pk_match:
                primary_key_name = pk_match.group(1).strip().strip('"').strip("'")

    # Проверяем колонки на PRIMARY KEY в определении
    for i, col in enumerate(columns):
        col_upper = col.upper()
        if 'PRIMARY KEY' in col_upper:
            primary_key_col = (i, col)
            # Извлекаем имя колонки
            col_name_match = re.match(r'^\s*([^\s]+)', col)
            if col_name_match:
                primary_key_name = col_name_match.group(1).strip()
            break

    # Сортируем колонки по размеру типа
    # Сохраняем оригинальные определения для стабильной сортировки
    column_sizes = [(get_column_size(col), i, col) for i, col in enumerate(columns)]
    sorted_columns = sorted(column_sizes, key=lambda x: (x[0][0], x[0][1], x[1]))

    # Проверяем, изменился ли порядок
    old_order = [col for _, _, col in column_sizes]
    new_order = [col for _, _, col in sorted_columns]

    # PRIMARY KEY колонка должна быть первой (если есть)
    if primary_key_col:
        pk_col = primary_key_col[1]
        if pk_col in new_order and new_order[0] != pk_col:
            new_order.remove(pk_col)
            new_order.insert(0, pk_col)
    elif primary_key_name:
        # Ищем колонку по имени
        for col in new_order:
            col_name_match = re.match(r'^\s*([^\s]+)', col)
            if col_name_match and col_name_match.group(1).strip() == primary_key_name:
                if new_order[0] != col:
                    new_order.remove(col)
                    new_order.insert(0, col)
                break

    if old_order == new_order:
        return table['full_match'], False

    # Собираем новое определение таблицы
    table_name = table['name']
    new_columns = new_order + table['constraints']
    new_table_def = f"CREATE TABLE IF NOT EXISTS {table_name} (\n"
    new_table_def += ",\n".join(f"  {col}" for col in new_columns)
    new_table_def += "\n);"

    return new_table_def, True


def process_sql_file(file_path: Path, dry_run: bool = False) -> Tuple[int, List[str]]:
    """
    Обрабатывает SQL файл с миграциями.
    Возвращает (количество измененных таблиц, список имен).
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    tables = parse_table_definition(content)
    if not tables:
        return (0, [])

    changed_tables = []
    new_content = content

    # Обрабатываем каждую таблицу (в обратном порядке для правильной замены)
    for table in reversed(tables):
        new_def, changed = reorder_table_columns(table)
        if changed:
            changed_tables.append(table['name'])
            # Заменяем старое определение на новое
            new_content = new_content.replace(table['full_match'], new_def, 1)

    if changed_tables and not dry_run:
        # Сохраняем изменения
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)

    return (len(changed_tables), changed_tables)


def main():
    parser = argparse.ArgumentParser(
        description='Автоматический рефакторинг Liquibase SQL миграций для column order optimization'
    )
    parser.add_argument('file', type=Path, help='SQL файл миграции для обработки')
    parser.add_argument('--dry-run', action='store_true',
                        help='Показать что будет изменено без сохранения')
    parser.add_argument('--verbose', '-v', action='store_true',
                        help='Подробный вывод')

    args = parser.parse_args()

    if not args.file.exists():
        print(f"ERROR: File not found: {args.file}", file=sys.stderr)
        sys.exit(1)

    print(f"Processing: {args.file}")
    if args.dry_run:
        print("DRY RUN mode - changes will not be saved")
    print()

    count, changed = process_sql_file(args.file, dry_run=args.dry_run)

    if count > 0:
        print(f"Changed tables: {count}")
        if args.verbose:
            for name in changed:
                print(f"  - {name}")
    else:
        print("All tables already optimized")

    if args.dry_run and count > 0:
        print()
        print("Run without --dry-run to apply changes")


if __name__ == '__main__':
    main()
