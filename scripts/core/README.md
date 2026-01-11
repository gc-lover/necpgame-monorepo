# Core Framework

This directory contains the foundational components and utilities used across all NECPGAME automation scripts.

## Components

### Error Handling System (`error-handling/`)
Enterprise-grade error handling with structured logging, middleware, and response formatting.

**Key Features:**
- Structured error types with severity levels
- HTTP status code mapping
- Request ID tracing
- Zap-based structured logging
- JWT authentication middleware
- Rate limiting
- Graceful error responses

### Base Components
- **`base_script.py`** - Abstract base class for all scripts with common functionality
- **`command_runner.py`** - Safe command execution with timeout and logging
- **`config.py`** - Centralized configuration management with validation
- **`file_manager.py`** - YAML/JSON file operations with validation
- **`logger.py`** - Script logging framework

## Usage

```python
from core.base_script import BaseScript
from core.config import ConfigManager
from core.logger import Logger

class MyScript(BaseScript):
    def __init__(self):
        super().__init__("my_script", "Description of my script")

    def run(self):
        # Script logic here
        pass
```

## Architecture

Follows SOLID principles with dependency injection for testability and maintainability.