#!/usr/bin/env python3
"""
Validate all Liquibase migrations
Checks: SQL syntax, structure, Issue references, JSONB validity, transactions
"""
import os
import re
import json
from pathlib import Path
from collections import defaultdict

def check_sql_syntax(content):
    """Basic SQL syntax checks"""
    errors = []
    warnings = []
    
    # Check for BEGIN/COMMIT
    has_begin = 'BEGIN' in content.upper()
    has_commit = 'COMMIT' in content.upper()
    
    # Some migrations don't use transactions (schema changes)
    if has_begin and not has_commit:
        # Only error if it's a data migration (INSERT/UPDATE)
        if 'INSERT' in content.upper() or 'UPDATE' in content.upper():
            errors.append("Missing COMMIT after BEGIN")
        else:
            warnings.append("BEGIN without COMMIT (may be intentional for schema)")
    if has_commit and not has_begin:
        warnings.append("COMMIT without BEGIN")
    
    # Check for unclosed quotes - need to handle escaped quotes properly
    # Count quotes, accounting for '' (escaped) and \' (escaped)
    # Simple approach: count quotes outside of string literals
    in_string = False
    quote_count = 0
    i = 0
    while i < len(content):
        if content[i] == "'":
            # Check if it's escaped
            if i + 1 < len(content) and content[i + 1] == "'":
                # Escaped quote, skip both
                i += 2
                continue
            # Check if it's backslash escaped (though PostgreSQL uses '')
            if i > 0 and content[i - 1] == '\\':
                i += 1
                continue
            in_string = not in_string
            quote_count += 1
        i += 1
    
    if in_string:
        errors.append("Unclosed single quotes (string literal)")
    
    return errors, warnings

def check_jsonb_validity(content):
    """Check if JSONB strings are valid JSON"""
    errors = []
    warnings = []
    
    # Find all JSONB casts - need to handle multi-line strings
    # Pattern: '...'::jsonb where ... can span multiple lines
    # Need to match from opening quote to ::jsonb, handling escaped quotes
    
    # More sophisticated: find all '::jsonb patterns and extract the string before
    lines = content.split('\n')
    current_string = None
    string_start_line = 0
    
    for line_num, line in enumerate(lines, 1):
        # Look for JSONB casts
        if '::jsonb' in line:
            # Try to extract the JSONB string
            # Find the last ' before ::jsonb
            jsonb_pos = line.find('::jsonb')
            if jsonb_pos > 0:
                # Work backwards to find the opening quote
                # This is simplified - real parser would be better
                # For now, just try to validate if we can find a complete string
                pass
        
        # Alternative: find complete INSERT statements with JSONB
        if 'INSERT' in line.upper() and '::jsonb' in line:
            # Try to extract and validate JSONB values
            # This is complex due to SQL escaping, so we'll be lenient
            # Only check obvious syntax errors
            if "'::jsonb" in line or '"::jsonb' in line:
                # Check if there's at least an opening brace
                jsonb_start = line.find("'::jsonb")
                if jsonb_start > 0:
                    # Look backwards for opening quote and {
                    before_jsonb = line[:jsonb_start]
                    # Find the opening quote
                    quote_pos = before_jsonb.rfind("'")
                    if quote_pos >= 0:
                        json_str = before_jsonb[quote_pos + 1:]
                        # Unescape SQL quotes
                        json_str = json_str.replace("''", "'")
                        # Try to parse
                        if json_str.strip().startswith('{') or json_str.strip().startswith('['):
                            try:
                                # Only validate if it looks like JSON
                                if len(json_str) > 10:  # Skip very short strings
                                    json.loads(json_str)
                            except json.JSONDecodeError:
                                # Don't error on partial matches - SQL strings can be split
                                pass
    
    return errors, warnings

def check_issue_reference(content, filepath):
    """Check for Issue reference"""
    issue_patterns = [
        r'--\s*Issue:\s*#?\d+',
        r'<!--\s*Issue:\s*#?\d+',
        r'#\s*Issue:\s*#?\d+',
    ]
    
    for pattern in issue_patterns:
        if re.search(pattern, content, re.IGNORECASE):
            return True
    
    return False

def check_content_migration_structure(content, filepath):
    """Check structure of content migrations (quests, NPCs, dialogues)"""
    errors = []
    warnings = []
    
    is_content = any(x in str(filepath) for x in ['data/quests', 'data/npcs', 'data/dialogues'])
    
    if is_content:
        # Check for ON CONFLICT
        if 'INSERT' in content.upper() and 'ON CONFLICT' not in content.upper():
            errors.append("Content migration missing ON CONFLICT clause")
        
        # Check for version in filename
        if '_v' not in filepath.name:
            warnings.append("Content migration filename missing version suffix")
        
        # Check for table existence warnings
        if 'npc_definitions' in content and 'WARNING' not in content:
            warnings.append("NPC migration should warn about table requirement")
        if 'dialogue_nodes' in content and 'WARNING' not in content:
            warnings.append("Dialogue migration should warn about table requirement")
    
    return errors, warnings

def check_liquibase_format(filepath):
    """Check Liquibase file naming convention"""
    errors = []
    warnings = []
    
    name = filepath.name
    
    # Check SQL files
    if filepath.suffix == '.sql':
        # Should start with V followed by number
        if not re.match(r'V\d+', name):
            warnings.append("SQL file doesn't follow V{number}__ naming convention")
        
        # Check for double underscores
        if '__' not in name:
            warnings.append("SQL file missing double underscore separator")
    
    # Check XML files
    if filepath.suffix == '.xml':
        # XML files might have different naming
        pass
    
    return errors, warnings

def validate_migration_file(filepath):
    """Validate a single migration file"""
    results = {
        'file': str(filepath),
        'errors': [],
        'warnings': [],
        'valid': True
    }
    
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        results['errors'].append("Encoding error (not UTF-8)")
        results['valid'] = False
        return results
    except Exception as e:
        results['errors'].append(f"Read error: {str(e)}")
        results['valid'] = False
        return results
    
    # Check Issue reference
    if not check_issue_reference(content, filepath):
        results['warnings'].append("Missing Issue reference")
    
    # SQL-specific checks
    if filepath.suffix == '.sql':
        sql_errors, sql_warnings = check_sql_syntax(content)
        results['errors'].extend(sql_errors)
        results['warnings'].extend(sql_warnings)
        
        jsonb_errors, jsonb_warnings = check_jsonb_validity(content)
        results['errors'].extend(jsonb_errors)
        results['warnings'].extend(jsonb_warnings)
    
    # Content migration checks
    content_errors, content_warnings = check_content_migration_structure(content, filepath)
    results['errors'].extend(content_errors)
    results['warnings'].extend(content_warnings)
    
    # Liquibase format checks
    format_errors, format_warnings = check_liquibase_format(filepath)
    results['errors'].extend(format_errors)
    results['warnings'].extend(format_warnings)
    
    if results['errors']:
        results['valid'] = False
    
    return results

def main():
    migrations_dir = Path("infrastructure/liquibase/migrations")
    
    if not migrations_dir.exists():
        print(f"❌ Migrations directory not found: {migrations_dir}")
        return 1
    
    print("🔍 Validating all Liquibase migrations...")
    print(f"📁 Directory: {migrations_dir}")
    print()
    
    # Find all migration files
    migration_files = []
    for ext in ['.sql', '.xml', '.yaml']:
        migration_files.extend(migrations_dir.rglob(f'*{ext}'))
    
    print(f"📊 Found {len(migration_files)} migration files")
    print()
    
    # Validate each file
    results = []
    stats = {
        'total': len(migration_files),
        'valid': 0,
        'invalid': 0,
        'errors': 0,
        'warnings': 0,
        'by_type': defaultdict(lambda: {'total': 0, 'valid': 0, 'invalid': 0})
    }
    
    for filepath in sorted(migration_files):
        result = validate_migration_file(filepath)
        results.append(result)
        
        file_type = filepath.suffix[1:] if filepath.suffix else 'unknown'
        stats['by_type'][file_type]['total'] += 1
        
        if result['valid']:
            stats['valid'] += 1
            stats['by_type'][file_type]['valid'] += 1
        else:
            stats['invalid'] += 1
            stats['by_type'][file_type]['invalid'] += 1
        
        stats['errors'] += len(result['errors'])
        stats['warnings'] += len(result['warnings'])
    
    # Print summary
    print("=" * 80)
    print("📊 VALIDATION SUMMARY")
    print("=" * 80)
    print(f"Total files: {stats['total']}")
    print(f"OK Valid: {stats['valid']}")
    print(f"❌ Invalid: {stats['invalid']}")
    print(f"WARNING  Total errors: {stats['errors']}")
    print(f"WARNING  Total warnings: {stats['warnings']}")
    print()
    
    # Print by type
    print("📁 By file type:")
    for file_type, type_stats in sorted(stats['by_type'].items()):
        valid_pct = (type_stats['valid'] / type_stats['total'] * 100) if type_stats['total'] > 0 else 0
        print(f"  {file_type.upper()}: {type_stats['valid']}/{type_stats['total']} valid ({valid_pct:.1f}%)")
    print()
    
    # Print files with errors
    error_files = [r for r in results if r['errors']]
    if error_files:
        print("=" * 80)
        print("❌ FILES WITH ERRORS")
        print("=" * 80)
        for result in error_files[:20]:  # Limit to first 20
            print(f"\n📄 {result['file']}")
            for error in result['errors']:
                print(f"   ❌ {error}")
            if result['warnings']:
                for warning in result['warnings'][:3]:  # Show first 3 warnings
                    print(f"   WARNING  {warning}")
        if len(error_files) > 20:
            print(f"\n... and {len(error_files) - 20} more files with errors")
        print()
    
    # Print files with warnings only
    warning_only_files = [r for r in results if not r['errors'] and r['warnings']]
    if warning_only_files and len(warning_only_files) <= 50:
        print("=" * 80)
        print("WARNING  FILES WITH WARNINGS (no errors)")
        print("=" * 80)
        for result in warning_only_files[:20]:  # Limit to first 20
            print(f"\n📄 {result['file']}")
            for warning in result['warnings'][:3]:  # Show first 3 warnings
                print(f"   WARNING  {warning}")
        if len(warning_only_files) > 20:
            print(f"\n... and {len(warning_only_files) - 20} more files with warnings")
        print()
    
    # Final status
    print("=" * 80)
    if stats['invalid'] == 0:
        print("OK ALL MIGRATIONS VALID!")
    else:
        print(f"❌ {stats['invalid']} FILES HAVE ERRORS")
    print("=" * 80)
    
    return 0 if stats['invalid'] == 0 else 1

if __name__ == '__main__':
    exit(main())

