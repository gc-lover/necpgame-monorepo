"""
JSON utilities for migration generators.
"""

from datetime import datetime, date
from typing import Any


class JsonSerializer:
    """Utility class for JSON serialization."""

    @staticmethod
    def json_serializer(obj: Any) -> str:
        """JSON serializer for datetime and date objects."""
        if isinstance(obj, datetime):
            return obj.isoformat()
        elif isinstance(obj, date):
            return obj.isoformat()
        raise TypeError(f"Object of type {type(obj)} is not JSON serializable")
