#!/usr/bin/env python3
"""
Debug quote issues in migration files
"""
import sys

def check_quotes(file_path):
    """Check for quote issues in file"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    lines = content.split('\n')
    in_string = False
    total_quotes = 0

    for i, line in enumerate(lines, 1):
        line_quotes = line.count("'")
        total_quotes += line_quotes

        if line_quotes % 2 != 0:
            print(f"Line {i}: ODD quotes ({line_quotes}) - {line.strip()}")

    print(f"Total quotes in file: {total_quotes}")
    if total_quotes % 2 != 0:
        print("ERROR: Total quotes is odd - unclosed string literal!")
    else:
        print("OK: Total quotes is even")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python debug_quotes.py <file>")
        sys.exit(1)

    check_quotes(sys.argv[1])

