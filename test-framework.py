#!/usr/bin/env python3
import sys
from pathlib import Path
sys.path.insert(0, '.')

from framework.core.validation import ValidationResult, FileSizeValidator, ProjectStructureValidator, EmojiValidator
from framework.config import get_config

def test_validation():
    print("=== TESTING FRAMEWORK VALIDATION ===")

    # Test files
    test_files = [Path('test-emoji.py')]
    print(f"Testing files: {[str(f) for f in test_files]}")

    result = ValidationResult()

    # 1. File size validation
    print("1. File size validation...")
    file_validator = FileSizeValidator()
    file_results = file_validator.validate(test_files)
    result.messages.extend(file_results.messages)
    print(f"   File validation: {len(file_results.messages)} messages")

    # 2. Project structure validation
    print("2. Project structure validation...")
    struct_validator = ProjectStructureValidator()
    struct_results = struct_validator.validate(Path('.'))
    result.messages.extend(struct_results.messages)
    print(f"   Structure validation: {len(struct_results.messages)} messages")

    # 3. Emoji validation
    print("3. Emoji validation...")
    emoji_validator = EmojiValidator()
    emoji_results = emoji_validator.validate(test_files)
    result.messages.extend(emoji_results.messages)
    print(f"   Emoji validation: {len(emoji_results.messages)} messages")

    # Report results
    print("\n=== VALIDATION RESULTS ===")
    print(f"Total messages: {len(result.messages)}")
    print(f"Has errors: {result.has_errors()}")

    if result.has_errors():
        error_messages = [msg for msg in result.messages if msg.severity.name in ['CRITICAL', 'ERROR']]
        print(f"Errors found: {len(error_messages)}")

        for msg in error_messages:
            print(f"\nERROR: {msg.code}")
            print(f"File: {msg.file_path}")
            print(f"Message: {msg.message}")
            if hasattr(msg, 'line_number') and msg.line_number:
                print(f"Line: {msg.line_number}")
            if hasattr(msg, 'suggestion') and msg.suggestion:
                print(f"Suggestion: {msg.suggestion}")

    return result.has_errors()

if __name__ == "__main__":
    has_errors = test_validation()
    sys.exit(1 if has_errors else 0)
