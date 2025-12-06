#!/usr/bin/env python3
"""Remove BOM from go.mod file"""
import sys

file_path = sys.argv[1]
with open(file_path, 'rb') as f:
    content = f.read()

# Remove BOM if present
if content.startswith(b'\xef\xbb\xbf'):
    content = content[3:]

with open(file_path, 'wb') as f:
    f.write(content)

print(f"BOM removed from {file_path}")

