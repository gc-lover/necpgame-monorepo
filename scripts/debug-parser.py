#!/usr/bin/env python3
"""
Debug the OpenAPI parser input
"""

import sys
import re

# Read all input
input_data = sys.stdin.read()
print("RAW INPUT RECEIVED:")
print("=" * 50)
print(repr(input_data))
print("=" * 50)
print("FORMATTED INPUT:")
print(input_data)

# Try to parse line by line
print("LINE BY LINE:")
lines = input_data.split('\n')
for i, line in enumerate(lines):
    print("2d")
