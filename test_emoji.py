#!/usr/bin/env python3
from framework.core.validation import EmojiValidator
from pathlib import Path
import tempfile

# Create a test file with emoji
with tempfile.NamedTemporaryFile(mode='w', suffix='.txt', delete=False, encoding='utf-8') as f:
    f.write('Hello world 🚫\nThis is a test OK\nAnother line with WARNING warning')
    test_file = f.name

try:
    validator = EmojiValidator()
    result = validator.validate([Path(test_file)])
    print('Validation completed. Found', len(result.messages), 'messages')
    for i, msg in enumerate(result.messages, 1):
        print()
        print('--- Message', i, '---')
        print('Severity:', msg.severity.value)
        print('Code:', msg.code)
        print('Message:', msg.message)
        print('File:', msg.file_path)
        print('Line:', msg.line_number)
        print('Suggestion:', msg.suggestion)
        print('Context keys:', list(msg.context.keys()))
        for key, value in msg.context.items():
            if key == 'line_content_preview':
                print(' ', key, ':', repr(value))
            else:
                print(' ', key, ':', value)
finally:
    import os
    os.unlink(test_file)
