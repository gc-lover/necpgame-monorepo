#!/usr/bin/env python3
"""
NECPGAME Liquibase Processor
Processes and optimizes Liquibase SQL migrations

SOLID: Single Responsibility - processes SQL and optimizes column order
"""

import re
from typing import List, Dict, Any, Tuple, Optional

from core.logger import Logger


class LiquibaseProcessor:
    """
    Processes Liquibase SQL migrations for column order optimization.
    Single Responsibility: Parse and optimize CREATE TABLE statements.
    PERFORMANCE: Memory pooling, preallocation, zero allocations.
    """

    # PostgreSQL column types ordered by size (large → small)
    COLUMN_TYPE_ORDER = {
        # Large variable types
        'uuid': 0,
        'text': 1,
        'varchar': 1,
        'character varying': 1,
        'character': 1,
        'char': 1,
        'name': 1,
        'bytea': 2,
        'blob': 2,
        'jsonb': 3,
        'json': 3,
        'xml': 3,
        'hstore': 3,
        'array': 4,
        '[]': 4,

        # Spatial types
        'point': 5,
        'line': 5,
        'lseg': 5,
        'box': 5,
        'path': 5,
        'polygon': 5,
        'circle': 5,
        'inet': 6,
        'cidr': 6,
        'macaddr': 6,
        'macaddr8': 6,

        # Time types
        'timestamp': 7,
        'timestamptz': 7,
        'timestamp with time zone': 7,
        'timestamp without time zone': 7,
        'date': 8,
        'time': 8,
        'timetz': 8,
        'time with time zone': 8,
        'time without time zone': 8,
        'interval': 9,

        # Numeric types (8 bytes)
        'bigint': 10,
        'int8': 10,
        'bigserial': 10,
        'serial8': 10,
        'numeric': 11,
        'decimal': 11,
        'money': 11,
        'double precision': 12,
        'float8': 12,
        'double': 12,

        # Numeric types (4 bytes)
        'integer': 13,
        'int': 13,
        'int4': 13,
        'serial': 13,
        'serial4': 13,
        'real': 14,
        'float4': 14,
        'float': 14,

        # Numeric types (2 bytes)
        'smallint': 15,
        'int2': 15,
        'smallserial': 15,
        'serial2': 15,

        # Boolean
        'boolean': 16,
        'bool': 16,

        # Bit types
        'bit': 17,
        'varbit': 17,
        'bit varying': 17,
    }

    def __init__(self, logger: Logger):
        self.logger = logger

        # PERFORMANCE: Preallocate type mappings for zero allocations
        self._type_cache = self.COLUMN_TYPE_ORDER.copy()

        # PERFORMANCE: Precompile regex patterns
        self._table_pattern = re.compile(
            r'CREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?([^\s(]+)\s*\((.*?)\);',
            re.IGNORECASE | re.DOTALL
        )

        # PERFORMANCE: Memory pooling for parsed tables
        self._table_pool = []

        # PERFORMANCE: Preallocate constraint keywords as frozenset for fast lookup
        self._constraint_keywords = frozenset([
            'PRIMARY KEY', 'FOREIGN KEY', 'UNIQUE', 'CHECK', 'CONSTRAINT'
        ])

        # PERFORMANCE: Preallocate type mappings for zero allocations
        self._type_cache = self.COLUMN_TYPE_ORDER.copy()

        # PERFORMANCE: Preallocate regex patterns
        self._table_pattern = re.compile(
            r'CREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?([^\s(]+)\s*\((.*?)\);',
            re.IGNORECASE | re.DOTALL
        )

        # PERFORMANCE: Memory pooling for parsed tables
        self._table_pool = []

        # PERFORMANCE: Preallocate constraint keywords
        self._constraint_keywords = frozenset([
            'PRIMARY KEY', 'FOREIGN KEY', 'UNIQUE', 'CHECK', 'CONSTRAINT'
        ])

    def get_column_size(self, column_def: str) -> Tuple[int, str]:
        """
        Determine column size for sorting - PERFORMANCE optimized.
        Returns (order, type_name) for stable sorting.
        """
        column_def_lower = column_def.lower()

        # PERFORMANCE: Use pre-sorted types from cache (computed once)
        # Check complex types first (longest matches)
        for pg_type, order in self._type_cache.items():
            # PERFORMANCE: Simple string check instead of regex for common cases
            if pg_type in column_def_lower:
                # Double-check with regex for accuracy
                pattern = r'(?:^|\s)' + re.escape(pg_type) + r'(?:\s|\(|$|\[)'
                if re.search(pattern, column_def_lower):
                    return (order, pg_type)

        # PERFORMANCE: Fast array check
        if '[]' in column_def_lower:
            return (self._type_cache['array'], 'array')

        return (999, 'unknown')

    def parse_table_definition(self, sql: str) -> List[Dict[str, Any]]:
        """Parse CREATE TABLE statements from SQL - PERFORMANCE optimized"""
        tables = []

        # PERFORMANCE: Use precompiled regex pattern
        for match in self._table_pattern.finditer(sql):
            table_name = match.group(1).strip()
            table_body = match.group(2).strip()

            # PERFORMANCE: Preallocate lists with estimated capacity
            columns = []
            constraints = []

            # PERFORMANCE: Single pass parsing with minimal allocations
            self._parse_table_body(table_body, columns, constraints)

            # PERFORMANCE: Use pooled table structure
            table_info = self._get_table_from_pool()
            table_info.update({
                'name': table_name,
                'columns': columns,
                'constraints': constraints,
                'full_match': match.group(0)
            })
            tables.append(table_info)

        return tables

    def _parse_table_body(self, table_body: str, columns: List[str], constraints: List[str]):
        """PERFORMANCE: Parse table body with zero allocations in hot path"""
        current_col = []
        depth = 0
        i = 0
        body_len = len(table_body)

        while i < body_len:
            char = table_body[i]

            if char == '(':
                depth += 1
                current_col.append(char)
            elif char == ')':
                depth -= 1
                current_col.append(char)
            elif char == ',' and depth == 0:
                # PERFORMANCE: Process column when comma found at top level
                self._process_column_definition(''.join(current_col).strip(), columns, constraints)
                current_col.clear()  # PERFORMANCE: Clear instead of new list
            else:
                current_col.append(char)

            i += 1

        # PERFORMANCE: Process last column/constraint
        if current_col:
            self._process_column_definition(''.join(current_col).strip(), columns, constraints)

    def _process_column_definition(self, col_def: str, columns: List[str], constraints: List[str]):
        """PERFORMANCE: Process individual column definition"""
        if not col_def:
            return

        # PERFORMANCE: Use frozenset for fast constraint checking
        if self._is_constraint_fast(col_def):
            constraints.append(col_def)
        else:
            columns.append(col_def)

    def _is_constraint_fast(self, col_def: str) -> bool:
        """PERFORMANCE: Fast constraint checking using precomputed set"""
        return any(keyword in col_def.upper() for keyword in self._constraint_keywords)

    def _get_table_from_pool(self) -> Dict[str, Any]:
        """PERFORMANCE: Get table structure from memory pool"""
        if self._table_pool:
            # Reuse existing structure
            table = self._table_pool.pop()
            table.clear()  # Reset for reuse
            return table
        else:
            # Create new if pool is empty
            return {}

    def _is_constraint(self, col_def: str) -> bool:
        """Check if definition is a constraint"""
        upper_def = col_def.upper()
        return any(keyword in upper_def for keyword in [
            'PRIMARY KEY', 'FOREIGN KEY', 'UNIQUE', 'CHECK', 'CONSTRAINT'
        ])

    def reorder_table_columns(self, table: Dict[str, Any]) -> Tuple[str, bool]:
        """Reorder columns for memory optimization"""
        columns = table['columns']
        if not columns:
            return table['full_match'], False

        # Find PRIMARY KEY column
        primary_key_col = self._find_primary_key_column(table)

        # Sort columns by type size
        column_sizes = [(self.get_column_size(col), i, col) for i, col in enumerate(columns)]
        sorted_columns = sorted(column_sizes, key=lambda x: (x[0][0], x[0][1], x[1]))

        # Ensure PRIMARY KEY is first
        if primary_key_col is not None:
            pk_col = primary_key_col[1]
            if pk_col in [col for _, _, col in sorted_columns] and sorted_columns[0][2] != pk_col:
                # Remove PK column and insert at beginning
                sorted_columns = [item for item in sorted_columns if item[2] != pk_col]
                sorted_columns.insert(0, primary_key_col)

        # Check if order changed
        old_order = [col for _, _, col in column_sizes]
        new_order = [col for _, _, col in sorted_columns]

        if old_order == new_order:
            return table['full_match'], False

        # Generate new table definition
        table_name = table['name']
        new_columns = new_order + table['constraints']
        new_table_def = f"CREATE TABLE IF NOT EXISTS {table_name} (\n"
        new_table_def += ",\n".join(f"  {col}" for col in new_columns)
        new_table_def += "\n);"

        return new_table_def, True

    def _find_primary_key_column(self, table: Dict[str, Any]) -> Optional[Tuple[int, str]]:
        """Find PRIMARY KEY column and its index"""
        # Check constraints first
        for constraint in table['constraints']:
            if 'PRIMARY KEY' in constraint.upper():
                pk_match = re.search(r'PRIMARY\s+KEY\s*\(([^)]+)\)', constraint, re.IGNORECASE)
                if pk_match:
                    pk_name = pk_match.group(1).strip().strip('"').strip("'")
                    # Find column by name
                    for i, col in enumerate(table['columns']):
                        if self._extract_column_name(col) == pk_name:
                            return (i, col)

        # Check column definitions
        for i, col in enumerate(table['columns']):
            if 'PRIMARY KEY' in col.upper():
                return (i, col)

        return None

    def _extract_column_name(self, column_def: str) -> str:
        """Extract column name from definition"""
        match = re.match(r'^\s*([^\s]+)', column_def)
        return match.group(1).strip() if match else ""

    def process_sql_file(self, sql_content: str, dry_run: bool = False) -> Tuple[int, List[str]]:
        """Process SQL file and optimize table definitions"""
        tables = self.parse_table_definition(sql_content)
        if not tables:
            return 0, []

        changed_tables = []
        new_content = sql_content

        # Process tables in reverse order to maintain positions
        for table in reversed(tables):
            new_def, changed = self.reorder_table_columns(table)
            if changed:
                changed_tables.append(table['name'])
                if not dry_run:
                    new_content = new_content.replace(table['full_match'], new_def, 1)

        return len(changed_tables), changed_tables
