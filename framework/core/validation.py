"""
NECPGAME Validation Framework
Provides comprehensive validation with clear error messages and documentation links for AI agents.
"""
from abc import ABC, abstractmethod
from typing import List, Dict, Any, Optional, Tuple
from pathlib import Path
from dataclasses import dataclass
from enum import Enum

from framework.config import get_config


class ValidationSeverity(Enum):
    """Validation result severity levels."""
    INFO = "info"
    WARNING = "warning"
    ERROR = "error"
    CRITICAL = "critical"


@dataclass
class ValidationMessage:
    """Structured validation message with documentation links."""
    severity: ValidationSeverity
    code: str
    message: str
    file_path: Optional[Path] = None
    line_number: Optional[int] = None
    documentation_url: Optional[str] = None
    suggestion: Optional[str] = None
    context: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        """Convert to dictionary for serialization."""
        return {
            "severity": self.severity.value,
            "code": self.code,
            "message": self.message,
            "file_path": str(self.file_path) if self.file_path else None,
            "line_number": self.line_number,
            "documentation_url": self.documentation_url,
            "suggestion": self.suggestion,
            "context": self.context or {}
        }

    def format_for_agent(self) -> str:
        """Format message specifically for AI agents with clear action items."""
        icon_map = {
            ValidationSeverity.INFO: "[INFO]",
            ValidationSeverity.WARNING: "[WARNING]",
            ValidationSeverity.ERROR: "[ERROR]",
            ValidationSeverity.CRITICAL: "[CRITICAL]"
        }

        icon = icon_map[self.severity]

        result = f"{icon} **{self.code}**\n"
        result += f"**{self.message}**\n"

        if self.file_path:
            result += f"File: `{self.file_path}`"
            if self.line_number:
                result += f":{self.line_number}"
            result += "\n"

        if self.documentation_url:
            result += f"Docs: {self.documentation_url}\n"

        if self.suggestion:
            result += f"Action Required: {self.suggestion}\n"

        if self.context:
            result += "\n**Context:**\n"
            for key, value in self.context.items():
                result += f"  • {key}: {value}\n"

        return result.strip()


class ValidationResult:
    """Container for validation results with summary statistics."""

    def __init__(self):
        self.messages: List[ValidationMessage] = []
        self._cache = None

    def add_message(self, message: ValidationMessage):
        """Add a validation message."""
        self.messages.append(message)
        self._cache = None  # Invalidate cache

    def add_error(self, code: str, message: str, **kwargs):
        """Add an error message."""
        self.add_message(ValidationMessage(
            severity=ValidationSeverity.ERROR,
            code=code,
            message=message,
            **kwargs
        ))

    def add_warning(self, code: str, message: str, **kwargs):
        """Add a warning message."""
        self.add_message(ValidationMessage(
            severity=ValidationSeverity.WARNING,
            code=code,
            message=message,
            **kwargs
        ))

    def add_info(self, code: str, message: str, **kwargs):
        """Add an info message."""
        self.add_message(ValidationMessage(
            severity=ValidationSeverity.INFO,
            code=code,
            message=message,
            **kwargs
        ))

    def add_critical(self, code: str, message: str, **kwargs):
        """Add a critical message."""
        self.add_message(ValidationMessage(
            severity=ValidationSeverity.CRITICAL,
            code=code,
            message=message,
            **kwargs
        ))

    @property
    def stats(self) -> Dict[str, int]:
        """Get statistics about validation results."""
        if self._cache is None:
            self._cache = {
                "total": len(self.messages),
                "errors": len([m for m in self.messages if m.severity == ValidationSeverity.ERROR]),
                "warnings": len([m for m in self.messages if m.severity == ValidationSeverity.WARNING]),
                "critical": len([m for m in self.messages if m.severity == ValidationSeverity.CRITICAL]),
                "info": len([m for m in self.messages if m.severity == ValidationSeverity.INFO])
            }
        return self._cache

    def has_errors(self) -> bool:
        """Check if there are any errors or critical issues."""
        return self.stats["errors"] > 0 or self.stats["critical"] > 0

    def has_warnings(self) -> bool:
        """Check if there are any warnings."""
        return self.stats["warnings"] > 0

    def get_error_count(self) -> int:
        """Get the number of errors."""
        return self.stats["errors"]

    def get_warning_count(self) -> int:
        """Get the number of warnings."""
        return self.stats["warnings"]

    def format_summary(self) -> str:
        """Format a summary of validation results."""
        stats = self.stats

        if stats["total"] == 0:
            return "VALIDATION PASSED: No issues found"

        result = f"VALIDATION RESULTS\n"
        result += f"Total checks: {stats['total']}\n"

        if stats["critical"] > 0:
            result += f"Critical: {stats['critical']}\n"
        if stats["errors"] > 0:
            result += f"Errors: {stats['errors']}\n"
        if stats["warnings"] > 0:
            result += f"Warnings: {stats['warnings']}\n"
        if stats["info"] > 0:
            result += f"Info: {stats['info']}\n"

        if self.has_errors():
            result += "\nVALIDATION FAILED - Fix critical errors and errors before proceeding"
        else:
            result += "\nVALIDATION PASSED"

        return result

    def get_stats(self) -> Dict[str, int]:
        """Get validation statistics."""
        stats = {
            'total': len(self.messages),
            'critical': 0,
            'errors': 0,
            'warnings': 0,
            'info': 0
        }

        for msg in self.messages:
            severity = msg.severity.value.lower()
            if severity == 'critical':
                stats['critical'] += 1
            elif severity == 'error':
                stats['errors'] += 1
            elif severity == 'warning':
                stats['warnings'] += 1
            elif severity == 'info':
                stats['info'] += 1

        return stats

    def format_detailed_report(self) -> str:
        """Format a detailed report with all messages."""
        if not self.messages:
            return "No validation messages"

        result = self.format_summary() + "\n\n"

        # Group messages by severity
        grouped = {}
        for msg in self.messages:
            severity = msg.severity.value
            if severity not in grouped:
                grouped[severity] = []
            grouped[severity].append(msg)

        # Sort by severity (critical first)
        severity_order = ["critical", "error", "warning", "info"]
        for severity in severity_order:
            if severity in grouped:
                result += f"**{severity.upper()}** ({len(grouped[severity])}):\n"
                for msg in grouped[severity]:
                    result += msg.format_for_agent() + "\n\n"

        return result.strip()


class Validator(ABC):
    """Base class for all validators."""

    def __init__(self):
        self.config = get_config()

    @abstractmethod
    def validate(self, target: Any, **kwargs) -> ValidationResult:
        """Perform validation and return results."""
        pass

    def create_message(self, severity: ValidationSeverity, code: str, message: str, **kwargs) -> ValidationMessage:
        """Create a validation message with documentation links."""
        # Add documentation URL based on code
        doc_url = self._get_documentation_url(code)

        return ValidationMessage(
            severity=severity,
            code=code,
            message=message,
            documentation_url=doc_url,
            **kwargs
        )

    def _get_documentation_url(self, code: str) -> Optional[str]:
        """Get documentation URL for error code."""
        # Map error codes to documentation
        doc_map = {
            "FILE_SIZE_EXCEEDED": ".cursor/project-config.yaml#file-size-limits",
            "MISSING_REQUIRED_FILE": ".cursor/rules/always.mdc#required-files",
            "INVALID_OPENAPI_VERSION": ".cursor/project-config.yaml#openapi-supported-versions",
            "MISSING_OPERATION_ID": ".cursor/rules/agent-api-designer.mdc#operationid-requirement",
            "STRUCT_ALIGNMENT_VIOLATION": ".cursor/BACKEND_OPTIMIZATION_CHECKLIST.md#struct-field-alignment",
            "MISSING_CONTEXT_TIMEOUT": ".cursor/BACKEND_OPTIMIZATION_CHECKLIST.md#context-timeouts",
            "DOMAIN_NOT_ENTERPRISE": ".cursor/DOMAIN_REFERENCE.md#enterprise-domains",
            "GIT_BRANCH_POLICY_VIOLATION": ".githooks/post-checkout",
            "GIT_NO_VERIFY_BYPASS": ".githooks/pre-commit",
        }

        return doc_map.get(code)


class FileSizeValidator(Validator):
    """Validator for file sizes using shared utilities."""

    def validate(self, file_paths: List[Path], **kwargs) -> ValidationResult:
        result = ValidationResult()

        if not file_paths:
            return result

        # Load configuration
        config = self.config

        # Validate each file using shared utilities
        for file_path in file_paths:
            try:
                # Use shared validation function
                from framework.utils.file_utils import count_file_lines
                from framework.config.config import FileLimits

                file_limits = config.file_limits

                # Check if file is exempt
                file_str = str(file_path)
                is_exempt = any(exempt in file_str for exempt in file_limits.exempt_files)

                if is_exempt:
                    result.add_info(
                        "FILE_EXEMPT",
                        f"File is exempt from size limits: {file_path.name}",
                        file_path=file_path
                    )
                    continue

                # Count lines
                line_count = count_file_lines(file_path)

                # Check against limit
                limit = file_limits.get_limit_for_file(file_path)

                if line_count > limit:
                    result.add_error(
                        "FILE_SIZE_EXCEEDED",
                        f"File exceeds size limit: {line_count} lines (max: {limit})",
                        file_path=file_path,
                        suggestion="Split file into smaller modules or refactor to reduce size",
                        context={"current_lines": line_count, "limit": limit}
                    )
                else:
                    result.add_info(
                        "FILE_SIZE_OK",
                        f"File size OK: {line_count} lines",
                        file_path=file_path
                    )

            except Exception as e:
                result.add_warning(
                    "FILE_READ_ERROR",
                    f"Could not read file for size check: {e}",
                    file_path=file_path
                )

        return result


class ProjectStructureValidator(Validator):
    """Validator for project structure using shared utilities."""

    def validate(self, project_root: Path, **kwargs) -> ValidationResult:
        result = ValidationResult()

        # Check required directories
        required_dirs = [
            ("proto/openapi", "OpenAPI specifications directory"),
            ("knowledge", "Game content and documentation"),
            ("services", "Backend microservices"),
            ("infrastructure", "Infrastructure as Code"),
            ("scripts", "Automation scripts"),
            (".cursor", "Agent rules and configuration"),
            (".githooks", "Git hooks")
        ]

        for dir_path, description in required_dirs:
            full_path = project_root / dir_path
            if not full_path.exists():
                result.add_error(
                    "MISSING_REQUIRED_DIRECTORY",
                    f"Missing required directory: {dir_path}",
                    suggestion=f"Create directory: mkdir -p {dir_path}",
                    context={"description": description}
                )
            else:
                result.add_info(
                    "DIRECTORY_EXISTS",
                    f"Required directory exists: {dir_path}",
                    file_path=full_path
                )

        # Check required files
        required_files = [
            (".cursor/project-config.yaml", "Project configuration"),
            (".cursor/rules/always.mdc", "Global agent rules"),
            (".githooks/pre-commit", "Pre-commit validation hook")
        ]

        for file_path, description in required_files:
            full_path = project_root / file_path
            if not full_path.exists():
                result.add_error(
                    "MISSING_REQUIRED_FILE",
                    f"Missing required file: {file_path}",
                    suggestion=f"Create or restore file: {file_path}",
                    context={"description": description}
                )
            else:
                result.add_info(
                    "FILE_EXISTS",
                    f"Required file exists: {file_path}",
                    file_path=full_path
                )

        return result


class EmojiValidator(Validator):
    """Validator for detecting and blocking emoji and special Unicode characters."""

    def get_character_description(self, char: str) -> str:
        """Get human-readable description of a Unicode character."""
        descriptions = {
            # Common emojis
            '[FORBIDDEN]': 'prohibited sign',
            '[OK]': 'check mark',
            '[WARNING]': 'warning sign',
            '[SYMBOL]': 'folder',
            '[ALERT]': 'rotating light',
            '[ROBOT]': 'robot',
            '[SEARCH]': 'magnifying glass',
            '[SYMBOL]': 'clipboard',
            '[FAST]': 'high voltage',

            # Card suits
            '[SYMBOL]': 'spade suit',
            '[SYMBOL]': 'heart suit',
            '[SYMBOL]': 'diamond suit',
            '[SYMBOL]': 'club suit',

            # Special symbols
            '©': 'copyright sign',
            '®': 'registered sign',
            '™': 'trade mark sign',
            '°': 'degree sign',
            '§': 'section sign',
            '¶': 'pilcrow sign',

            # Math symbols
            '∑': 'summation',
            '∏': 'product',
            '∆': 'delta',
            '∞': 'infinity',
            '≤': 'less than or equal',
            '≥': 'greater than or equal',
            '≠': 'not equal',
            '≈': 'approximately equal',

            # Arrows
            '←': 'left arrow',
            '→': 'right arrow',
            '↑': 'up arrow',
            '↓': 'down arrow',
            '↔': 'left right arrow',
        }

        return descriptions.get(char, f"Unicode character U+{ord(char):04X}")

    def validate(self, file_paths: List[Path], **kwargs) -> ValidationResult:
        """Validate files for emoji and special Unicode characters."""
        result = ValidationResult()

        if not file_paths:
            return result

        # Characters that are strictly forbidden
        forbidden_chars = [
            # Emojis and Unicode symbols
            '\u00a9', '\u00ae', '\u2000', '\u2001', '\u2002', '\u2003', '\u2004', '\u2005', '\u2006', '\u2007', '\u2008', '\u2009', '\u200a', '\u2028', '\u2029', '\u202f', '\ufeff',  # Whitespace
            '\u2600', '\u2601', '\u2602', '\u2603', '\u2604', '\u2605', '\u2606', '\u2607', '\u2608', '\u2609', '\u260a', '\u260b', '\u260c', '\u260d', '\u260e', '\u260f',  # Misc symbols
            '\u2610', '\u2611', '\u2612', '\u2613', '\u2614', '\u2615', '\u2616', '\u2617', '\u2618', '\u2619', '\u261a', '\u261b', '\u261c', '\u261d',  # Misc symbols
            '\u2620', '\u2621', '\u2622', '\u2623', '\u2624', '\u2625', '\u2626', '\u2627', '\u2628', '\u2629', '\u262a', '\u262b', '\u262c', '\u262d', '\u262e', '\u262f',  # Misc symbols
            '\u2630', '\u2631', '\u2632', '\u2633', '\u2634', '\u2635', '\u2636', '\u2637', '\u2638', '\u2639', '\u263a', '\u263b', '\u263c', '\u263d', '\u263e', '\u263f',  # Misc symbols
            '\u2640', '\u2641', '\u2642', '\u2643', '\u2644', '\u2645', '\u2646', '\u2647', '\u2648', '\u2649', '\u264a', '\u264b', '\u264c', '\u264d', '\u264e', '\u264f',  # Zodiac
            '\u2650', '\u2651', '\u2652', '\u2653', '\u2654', '\u2655', '\u2656', '\u2657', '\u2658', '\u2659', '\u265a', '\u265b', '\u265c', '\u265d', '\u265e', '\u265f',  # Chess
            '\u2660', '\u2661', '\u2662', '\u2663', '\u2664', '\u2665', '\u2666', '\u2667', '\u2668', '\u2669', '\u266a', '\u266b', '\u266c', '\u266d', '\u266e', '\u266f',  # Card suits
            '\u2670', '\u2671', '\u2672', '\u2673', '\u2674', '\u2675', '\u2676', '\u2677', '\u2678', '\u2679', '\u267a', '\u267b', '\u267c', '\u267d', '\u267e', '\u267f',  # Misc symbols
            '\u2680', '\u2681', '\u2682', '\u2683', '\u2684', '\u2685', '\u2686', '\u2687', '\u2688', '\u2689', '\u268a', '\u268b', '\u268c', '\u268d', '\u268e', '\u268f',  # Braille
            '\u2690', '\u2691', '\u2692', '\u2693', '\u2694', '\u2695', '\u2696', '\u2697', '\u2698', '\u2699', '\u269a', '\u269b', '\u269c', '\u269d', '\u269e', '\u269f',  # Misc symbols
            '\u26a0', '\u26a1', '\u26a2', '\u26a3', '\u26a4', '\u26a5', '\u26a6', '\u26a7', '\u26a8', '\u26a9', '\u26aa', '\u26ab', '\u26ac', '\u26ad', '\u26ae', '\u26af',  # Misc symbols
            '\u26b0', '\u26b1', '\u26b2', '\u26b3', '\u26b4', '\u26b5', '\u26b6', '\u26b7', '\u26b8', '\u26b9', '\u26ba', '\u26bb', '\u26bc', '\u26bd', '\u26be', '\u26bf',  # Misc symbols
            '\u26c0', '\u26c1', '\u26c2', '\u26c3', '\u26c4', '\u26c5', '\u26c6', '\u26c7', '\u26c8', '\u26c9', '\u26ca', '\u26cb', '\u26cc', '\u26cd', '\u26ce', '\u26cf',  # Misc symbols
            '\u26d0', '\u26d1', '\u26d2', '\u26d3', '\u26d4', '\u26d5', '\u26d6', '\u26d7', '\u26d8', '\u26d9', '\u26da', '\u26db', '\u26dc', '\u26dd', '\u26de', '\u26df',  # Misc symbols
            '\u26e0', '\u26e1', '\u26e2', '\u26e3', '\u26e4', '\u26e5', '\u26e6', '\u26e7', '\u26e8', '\u26e9', '\u26ea', '\u26eb', '\u26ec', '\u26ed', '\u26ee', '\u26ef',  # Misc symbols
            '\u26f0', '\u26f1', '\u26f2', '\u26f3', '\u26f4', '\u26f5', '\u26f6', '\u26f7', '\u26f8', '\u26f9', '\u26fa', '\u26fb', '\u26fc', '\u26fd', '\u26fe', '\u26ff',  # Misc symbols
            '\u2700', '\u2701', '\u2702', '\u2703', '\u2704', '\u2705', '\u2706', '\u2707', '\u2708', '\u2709', '\u270a', '\u270b', '\u270c', '\u270d',  # Dingbats
            '\u270e', '\u270f', '\u2710', '\u2711', '\u2712', '\u2713', '\u2714', '\u2715', '\u2716', '\u2717', '\u2718', '\u2719', '\u271a', '\u271b', '\u271c', '\u271d', '\u271e', '\u271f',  # Dingbats
            '\u2720', '\u2721', '\u2722', '\u2723', '\u2724', '\u2725', '\u2726', '\u2727', '\u2728', '\u2729', '\u272a', '\u272b', '\u272c', '\u272d', '\u272e', '\u272f',  # Dingbats
            '\u2730', '\u2731', '\u2732', '\u2733', '\u2734', '\u2735', '\u2736', '\u2737', '\u2738', '\u2739', '\u273a', '\u273b', '\u273c', '\u273d', '\u273e', '\u273f',  # Dingbats
            '\u2740', '\u2741', '\u2742', '\u2743', '\u2744', '\u2745', '\u2746', '\u2747', '\u2748', '\u2749', '\u274a', '\u274b', '\u274c', '\u274d', '\u274e', '\u274f',  # Dingbats
            '\u2750', '\u2751', '\u2752', '\u2753', '\u2754', '\u2755', '\u2756', '\u2757', '\u2758', '\u2759', '\u275a', '\u275b', '\u275c', '\u275d', '\u275e', '\u275f',  # Dingbats
            '\u2760', '\u2761', '\u2762', '\u2763', '\u2764', '\u2765', '\u2766', '\u2767', '\u2768', '\u2769', '\u276a', '\u276b', '\u276c', '\u276d', '\u276e', '\u276f',  # Dingbats
            '\u2770', '\u2771', '\u2772', '\u2773', '\u2774', '\u2775', '\u2776', '\u2777', '\u2778', '\u2779', '\u277a', '\u277b', '\u277c', '\u277d', '\u277e', '\u277f',  # Dingbats
            '\u2780', '\u2781', '\u2782', '\u2783', '\u2784', '\u2785', '\u2786', '\u2787', '\u2788', '\u2789', '\u278a', '\u278b', '\u278c', '\u278d', '\u278e', '\u278f',  # Dingbats
            '\u2790', '\u2791', '\u2792', '\u2793', '\u2794', '\u2795', '\u2796', '\u2797', '\u2798', '\u2799', '\u279a', '\u279b', '\u279c', '\u279d', '\u279e', '\u279f',  # Dingbats
            '\u27a0', '\u27a1', '\u27a2', '\u27a3', '\u27a4', '\u27a5', '\u27a6', '\u27a7', '\u27a8', '\u27a9', '\u27aa', '\u27ab', '\u27ac', '\u27ad', '\u27ae', '\u27af',  # Dingbats
            '\u27b0', '\u27b1', '\u27b2', '\u27b3', '\u27b4', '\u27b5', '\u27b6', '\u27b7', '\u27b8', '\u27b9', '\u27ba', '\u27bb', '\u27bc', '\u27bd', '\u27be', '\u27bf',  # Dingbats
            '\u27c0', '\u27c1', '\u27c2', '\u27c3', '\u27c4', '\u27c5', '\u27c6', '\u27c7', '\u27c8', '\u27c9', '\u27ca', '\u27cb', '\u27cc', '\u27cd', '\u27ce', '\u27cf',  # Misc Math
            '\u27d0', '\u27d1', '\u27d2', '\u27d3', '\u27d4', '\u27d5', '\u27d6', '\u27d7', '\u27d8', '\u27d9', '\u27da', '\u27db', '\u27dc', '\u27dd', '\u27de', '\u27df',  # Misc Math
            '\u27e0', '\u27e1', '\u27e2', '\u27e3', '\u27e4', '\u27e5', '\u27e6', '\u27e7', '\u27e8', '\u27e9', '\u27ea', '\u27eb', '\u27ec', '\u27ed', '\u27ee', '\u27ef',  # Misc Math
            '\u27f0', '\u27f1', '\u27f2', '\u27f3', '\u27f4', '\u27f5', '\u27f6', '\u27f7', '\u27f8', '\u27f9', '\u27fa', '\u27fb', '\u27fc', '\u27fd', '\u27fe', '\u27ff',  # Misc Math
            # Extended emoji ranges
            '\U0001F300', '\U0001F301', '\U0001F302', '\U0001F303', '\U0001F304', '\U0001F305', '\U0001F306', '\U0001F307', '\U0001F308', '\U0001F309', '\U0001F30A', '\U0001F30B', '\U0001F30C', '\U0001F30D', '\U0001F30E', '\U0001F30F',  # Misc Symbols and Pictographs
            '\U0001F310', '\U0001F311', '\U0001F312', '\U0001F313', '\U0001F314', '\U0001F315', '\U0001F316', '\U0001F317', '\U0001F318', '\U0001F319', '\U0001F31A', '\U0001F31B', '\U0001F31C', '\U0001F31D', '\U0001F31E', '\U0001F31F',  # Misc Symbols and Pictographs
            '\U0001F320', '\U0001F321', '\U0001F322', '\U0001F323', '\U0001F324', '\U0001F325', '\U0001F326', '\U0001F327', '\U0001F328', '\U0001F329', '\U0001F32A', '\U0001F32B', '\U0001F32C', '\U0001F32D', '\U0001F32E', '\U0001F32F',  # Misc Symbols and Pictographs
            '\U0001F330', '\U0001F331', '\U0001F332', '\U0001F333', '\U0001F334', '\U0001F335', '\U0001F336', '\U0001F337', '\U0001F338', '\U0001F339', '\U0001F33A', '\U0001F33B', '\U0001F33C', '\U0001F33D', '\U0001F33E', '\U0001F33F',  # Misc Symbols and Pictographs
            '\U0001F340', '\U0001F341', '\U0001F342', '\U0001F343', '\U0001F344', '\U0001F345', '\U0001F346', '\U0001F347', '\U0001F348', '\U0001F349', '\U0001F34A', '\U0001F34B', '\U0001F34C', '\U0001F34D', '\U0001F34E', '\U0001F34F',  # Misc Symbols and Pictographs
            '\U0001F350', '\U0001F351', '\U0001F352', '\U0001F353', '\U0001F354', '\U0001F355', '\U0001F356', '\U0001F357', '\U0001F358', '\U0001F359', '\U0001F35A', '\U0001F35B', '\U0001F35C', '\U0001F35D', '\U0001F35E', '\U0001F35F',  # Misc Symbols and Pictographs
            '\U0001F360', '\U0001F361', '\U0001F362', '\U0001F363', '\U0001F364', '\U0001F365', '\U0001F366', '\U0001F367', '\U0001F368', '\U0001F369', '\U0001F36A', '\U0001F36B', '\U0001F36C', '\U0001F36D', '\U0001F36E', '\U0001F36F',  # Misc Symbols and Pictographs
            '\U0001F370', '\U0001F371', '\U0001F372', '\U0001F373', '\U0001F374', '\U0001F375', '\U0001F376', '\U0001F377', '\U0001F378', '\U0001F379', '\U0001F37A', '\U0001F37B', '\U0001F37C', '\U0001F37D', '\U0001F37E', '\U0001F37F',  # Misc Symbols and Pictographs
            '\U0001F380', '\U0001F381', '\U0001F382', '\U0001F383', '\U0001F384', '\U0001F385', '\U0001F386', '\U0001F387', '\U0001F388', '\U0001F389', '\U0001F38A', '\U0001F38B', '\U0001F38C', '\U0001F38D', '\U0001F38E', '\U0001F38F',  # Misc Symbols and Pictographs
            '\U0001F390', '\U0001F391', '\U0001F392', '\U0001F393', '\U0001F394', '\U0001F395', '\U0001F396', '\U0001F397', '\U0001F398', '\U0001F399', '\U0001F39A', '\U0001F39B', '\U0001F39C', '\U0001F39D', '\U0001F39E', '\U0001F39F',  # Misc Symbols and Pictographs
            '\U0001F3A0', '\U0001F3A1', '\U0001F3A2', '\U0001F3A3', '\U0001F3A4', '\U0001F3A5', '\U0001F3A6', '\U0001F3A7', '\U0001F3A8', '\U0001F3A9', '\U0001F3AA', '\U0001F3AB', '\U0001F3AC', '\U0001F3AD', '\U0001F3AE', '\U0001F3AF',  # Misc Symbols and Pictographs
            '\U0001F3B0', '\U0001F3B1', '\U0001F3B2', '\U0001F3B3', '\U0001F3B4', '\U0001F3B5', '\U0001F3B6', '\U0001F3B7', '\U0001F3B8', '\U0001F3B9', '\U0001F3BA', '\U0001F3BB', '\U0001F3BC', '\U0001F3BD', '\U0001F3BE', '\U0001F3BF',  # Misc Symbols and Pictographs
            '\U0001F3C0', '\U0001F3C1', '\U0001F3C2', '\U0001F3C3', '\U0001F3C4', '\U0001F3C5', '\U0001F3C6', '\U0001F3C7', '\U0001F3C8', '\U0001F3C9', '\U0001F3CA', '\U0001F3CB', '\U0001F3CC', '\U0001F3CD', '\U0001F3CE', '\U0001F3CF',  # Misc Symbols and Pictographs
            '\U0001F3D0', '\U0001F3D1', '\U0001F3D2', '\U0001F3D3', '\U0001F3D4', '\U0001F3D5', '\U0001F3D6', '\U0001F3D7', '\U0001F3D8', '\U0001F3D9', '\U0001F3DA', '\U0001F3DB', '\U0001F3DC', '\U0001F3DD', '\U0001F3DE', '\U0001F3DF',  # Misc Symbols and Pictographs
            '\U0001F3E0', '\U0001F3E1', '\U0001F3E2', '\U0001F3E3', '\U0001F3E4', '\U0001F3E5', '\U0001F3E6', '\U0001F3E7', '\U0001F3E8', '\U0001F3E9', '\U0001F3EA', '\U0001F3EB', '\U0001F3EC', '\U0001F3ED', '\U0001F3EE', '\U0001F3EF',  # Misc Symbols and Pictographs
            '\U0001F3F0', '\U0001F3F1', '\U0001F3F2', '\U0001F3F3', '\U0001F3F4', '\U0001F3F5', '\U0001F3F6', '\U0001F3F7', '\U0001F3F8', '\U0001F3F9', '\U0001F3FA', '\U0001F3FB', '\U0001F3FC', '\U0001F3FD', '\U0001F3FE', '\U0001F3FF',  # Misc Symbols and Pictographs
            '\U0001F400', '\U0001F401', '\U0001F402', '\U0001F403', '\U0001F404', '\U0001F405', '\U0001F406', '\U0001F407', '\U0001F408', '\U0001F409', '\U0001F40A', '\U0001F40B', '\U0001F40C', '\U0001F40D', '\U0001F40E', '\U0001F40F',  # Animal symbols
            '\U0001F410', '\U0001F411', '\U0001F412', '\U0001F413', '\U0001F414', '\U0001F415', '\U0001F416', '\U0001F417', '\U0001F418', '\U0001F419', '\U0001F41A', '\U0001F41B', '\U0001F41C', '\U0001F41D', '\U0001F41E', '\U0001F41F',  # Animal symbols
            '\U0001F420', '\U0001F421', '\U0001F422', '\U0001F423', '\U0001F424', '\U0001F425', '\U0001F426', '\U0001F427', '\U0001F428', '\U0001F429', '\U0001F42A', '\U0001F42B', '\U0001F42C', '\U0001F42D', '\U0001F42E', '\U0001F42F',  # Animal symbols
            '\U0001F430', '\U0001F431', '\U0001F432', '\U0001F433', '\U0001F434', '\U0001F435', '\U0001F436', '\U0001F437', '\U0001F438', '\U0001F439', '\U0001F43A', '\U0001F43B', '\U0001F43C', '\U0001F43D', '\U0001F43E', '\U0001F43F',  # Animal symbols
            '\U0001F440', '\U0001F441', '\U0001F442', '\U0001F443', '\U0001F444', '\U0001F445', '\U0001F446', '\U0001F447', '\U0001F448', '\U0001F449', '\U0001F44A', '\U0001F44B', '\U0001F44C', '\U0001F44D', '\U0001F44E', '\U0001F44F',  # Hand symbols
            '\U0001F450', '\U0001F451', '\U0001F452', '\U0001F453', '\U0001F454', '\U0001F455', '\U0001F456', '\U0001F457', '\U0001F458', '\U0001F459', '\U0001F45A', '\U0001F45B', '\U0001F45C', '\U0001F45D', '\U0001F45E', '\U0001F45F',  # Clothing symbols
            '\U0001F460', '\U0001F461', '\U0001F462', '\U0001F463', '\U0001F464', '\U0001F465', '\U0001F466', '\U0001F467', '\U0001F468', '\U0001F469', '\U0001F46A', '\U0001F46B', '\U0001F46C', '\U0001F46D', '\U0001F46E', '\U0001F46F',  # People symbols
            '\U0001F470', '\U0001F471', '\U0001F472', '\U0001F473', '\U0001F474', '\U0001F475', '\U0001F476', '\U0001F477', '\U0001F478', '\U0001F479', '\U0001F47A', '\U0001F47B', '\U0001F47C', '\U0001F47D', '\U0001F47E', '\U0001F47F',  # People symbols
            '\U0001F480', '\U0001F481', '\U0001F482', '\U0001F483', '\U0001F484', '\U0001F485', '\U0001F486', '\U0001F487', '\U0001F488', '\U0001F489', '\U0001F48A', '\U0001F48B', '\U0001F48C', '\U0001F48D', '\U0001F48E', '\U0001F48F',  # People symbols
            '\U0001F490', '\U0001F491', '\U0001F492', '\U0001F493', '\U0001F494', '\U0001F495', '\U0001F496', '\U0001F497', '\U0001F498', '\U0001F499', '\U0001F49A', '\U0001F49B', '\U0001F49C', '\U0001F49D', '\U0001F49E', '\U0001F49F',  # Heart symbols
            '\U0001F4A0', '\U0001F4A1', '\U0001F4A2', '\U0001F4A3', '\U0001F4A4', '\U0001F4A5', '\U0001F4A6', '\U0001F4A7', '\U0001F4A8', '\U0001F4A9', '\U0001F4AA', '\U0001F4AB', '\U0001F4AC', '\U0001F4AD', '\U0001F4AE', '\U0001F4AF',  # Misc symbols
            '\U0001F4B0', '\U0001F4B1', '\U0001F4B2', '\U0001F4B3', '\U0001F4B4', '\U0001F4B5', '\U0001F4B6', '\U0001F4B7', '\U0001F4B8', '\U0001F4B9', '\U0001F4BA', '\U0001F4BB', '\U0001F4BC', '\U0001F4BD', '\U0001F4BE', '\U0001F4BF',  # Misc symbols
            '\U0001F4C0', '\U0001F4C1', '\U0001F4C2', '\U0001F4C3', '\U0001F4C4', '\U0001F4C5', '\U0001F4C6', '\U0001F4C7', '\U0001F4C8', '\U0001F4C9', '\U0001F4CA', '\U0001F4CB', '\U0001F4CC', '\U0001F4CD', '\U0001F4CE', '\U0001F4CF',  # Misc symbols
            '\U0001F4D0', '\U0001F4D1', '\U0001F4D2', '\U0001F4D3', '\U0001F4D4', '\U0001F4D5', '\U0001F4D6', '\U0001F4D7', '\U0001F4D8', '\U0001F4D9', '\U0001F4DA', '\U0001F4DB', '\U0001F4DC', '\U0001F4DD', '\U0001F4DE', '\U0001F4DF',  # Misc symbols
            '\U0001F4E0', '\U0001F4E1', '\U0001F4E2', '\U0001F4E3', '\U0001F4E4', '\U0001F4E5', '\U0001F4E6', '\U0001F4E7', '\U0001F4E8', '\U0001F4E9', '\U0001F4EA', '\U0001F4EB', '\U0001F4EC', '\U0001F4ED', '\U0001F4EE', '\U0001F4EF',  # Misc symbols
            '\U0001F4F0', '\U0001F4F1', '\U0001F4F2', '\U0001F4F3', '\U0001F4F4', '\U0001F4F5', '\U0001F4F6', '\U0001F4F7', '\U0001F4F8', '\U0001F4F9', '\U0001F4FA', '\U0001F4FB', '\U0001F4FC', '\U0001F4FD', '\U0001F4FE', '\U0001F4FF',  # Misc symbols
            '\U0001F500', '\U0001F501', '\U0001F502', '\U0001F503', '\U0001F504', '\U0001F505', '\U0001F506', '\U0001F507', '\U0001F508', '\U0001F509', '\U0001F50A', '\U0001F50B', '\U0001F50C', '\U0001F50D', '\U0001F50E', '\U0001F50F',  # Symbols
            '\U0001F510', '\U0001F511', '\U0001F512', '\U0001F513', '\U0001F514', '\U0001F515', '\U0001F516', '\U0001F517', '\U0001F518', '\U0001F519', '\U0001F51A', '\U0001F51B', '\U0001F51C', '\U0001F51D', '\U0001F51E', '\U0001F51F',  # Symbols
            '\U0001F520', '\U0001F521', '\U0001F522', '\U0001F523', '\U0001F524', '\U0001F525', '\U0001F526', '\U0001F527', '\U0001F528', '\U0001F529', '\U0001F52A', '\U0001F52B', '\U0001F52C', '\U0001F52D', '\U0001F52E', '\U0001F52F',  # Symbols
            '\U0001F530', '\U0001F531', '\U0001F532', '\U0001F533', '\U0001F534', '\U0001F535', '\U0001F536', '\U0001F537', '\U0001F538', '\U0001F539', '\U0001F53A', '\U0001F53B', '\U0001F53C', '\U0001F53D', '\U0001F53E', '\U0001F53F',  # Symbols
            '\U0001F540', '\U0001F541', '\U0001F542', '\U0001F543', '\U0001F544', '\U0001F545', '\U0001F546', '\U0001F547', '\U0001F548', '\U0001F549', '\U0001F54A', '\U0001F54B', '\U0001F54C', '\U0001F54D', '\U0001F54E', '\U0001F54F',  # Symbols
            '\U0001F550', '\U0001F551', '\U0001F552', '\U0001F553', '\U0001F554', '\U0001F555', '\U0001F556', '\U0001F557', '\U0001F558', '\U0001F559', '\U0001F55A', '\U0001F55B', '\U0001F55C', '\U0001F55D', '\U0001F55E', '\U0001F55F',  # Clock symbols
            '\U0001F560', '\U0001F561', '\U0001F562', '\U0001F563', '\U0001F564', '\U0001F565', '\U0001F566', '\U0001F567', '\U0001F568', '\U0001F569', '\U0001F56A', '\U0001F56B', '\U0001F56C', '\U0001F56D', '\U0001F56E', '\U0001F56F',  # Clock symbols
            '\U0001F570', '\U0001F571', '\U0001F572', '\U0001F573', '\U0001F574', '\U0001F575', '\U0001F576', '\U0001F577', '\U0001F578', '\U0001F579', '\U0001F57A', '\U0001F57B', '\U0001F57C', '\U0001F57D', '\U0001F57E', '\U0001F57F',  # Misc symbols
            '\U0001F580', '\U0001F581', '\U0001F582', '\U0001F583', '\U0001F584', '\U0001F585', '\U0001F586', '\U0001F587', '\U0001F588', '\U0001F589', '\U0001F58A', '\U0001F58B', '\U0001F58C', '\U0001F58D', '\U0001F58E', '\U0001F58F',  # Misc symbols
            '\U0001F590', '\U0001F591', '\U0001F592', '\U0001F593', '\U0001F594', '\U0001F595', '\U0001F596', '\U0001F597', '\U0001F598', '\U0001F599', '\U0001F59A', '\U0001F59B', '\U0001F59C', '\U0001F59D', '\U0001F59E', '\U0001F59F',  # Misc symbols
            '\U0001F5A0', '\U0001F5A1', '\U0001F5A2', '\U0001F5A3', '\U0001F5A4', '\U0001F5A5', '\U0001F5A6', '\U0001F5A7', '\U0001F5A8', '\U0001F5A9', '\U0001F5AA', '\U0001F5AB', '\U0001F5AC', '\U0001F5AD', '\U0001F5AE', '\U0001F5AF',  # Misc symbols
            '\U0001F5B0', '\U0001F5B1', '\U0001F5B2', '\U0001F5B3', '\U0001F5B4', '\U0001F5B5', '\U0001F5B6', '\U0001F5B7', '\U0001F5B8', '\U0001F5B9', '\U0001F5BA', '\U0001F5BB', '\U0001F5BC', '\U0001F5BD', '\U0001F5BE', '\U0001F5BF',  # Misc symbols
            '\U0001F5C0', '\U0001F5C1', '\U0001F5C2', '\U0001F5C3', '\U0001F5C4', '\U0001F5C5', '\U0001F5C6', '\U0001F5C7', '\U0001F5C8', '\U0001F5C9', '\U0001F5CA', '\U0001F5CB', '\U0001F5CC', '\U0001F5CD', '\U0001F5CE', '\U0001F5CF',  # Misc symbols
            '\U0001F5D0', '\U0001F5D1', '\U0001F5D2', '\U0001F5D3', '\U0001F5D4', '\U0001F5D5', '\U0001F5D6', '\U0001F5D7', '\U0001F5D8', '\U0001F5D9', '\U0001F5DA', '\U0001F5DB', '\U0001F5DC', '\U0001F5DD', '\U0001F5DE', '\U0001F5DF',  # Misc symbols
            '\U0001F5E0', '\U0001F5E1', '\U0001F5E2', '\U0001F5E3', '\U0001F5E4', '\U0001F5E5', '\U0001F5E6', '\U0001F5E7', '\U0001F5E8', '\U0001F5E9', '\U0001F5EA', '\U0001F5EB', '\U0001F5EC', '\U0001F5ED', '\U0001F5EE', '\U0001F5EF',  # Misc symbols
            '\U0001F5F0', '\U0001F5F1', '\U0001F5F2', '\U0001F5F3', '\U0001F5F4', '\U0001F5F5', '\U0001F5F6', '\U0001F5F7', '\U0001F5F8', '\U0001F5F9', '\U0001F5FA', '\U0001F5FB', '\U0001F5FC', '\U0001F5FD', '\U0001F5FE', '\U0001F5FF',  # Misc symbols
            '\U0001F600', '\U0001F601', '\U0001F602', '\U0001F603', '\U0001F604', '\U0001F605', '\U0001F606', '\U0001F607', '\U0001F608', '\U0001F609', '\U0001F60A', '\U0001F60B', '\U0001F60C', '\U0001F60D', '\U0001F60E', '\U0001F60F',  # Emoticons
            '\U0001F610', '\U0001F611', '\U0001F612', '\U0001F613', '\U0001F614', '\U0001F615', '\U0001F616', '\U0001F617', '\U0001F618', '\U0001F619', '\U0001F61A', '\U0001F61B', '\U0001F61C', '\U0001F61D', '\U0001F61E', '\U0001F61F',  # Emoticons
            '\U0001F620', '\U0001F621', '\U0001F622', '\U0001F623', '\U0001F624', '\U0001F625', '\U0001F626', '\U0001F627', '\U0001F628', '\U0001F629', '\U0001F62A', '\U0001F62B', '\U0001F62C', '\U0001F62D', '\U0001F62E', '\U0001F62F',  # Emoticons
            '\U0001F630', '\U0001F631', '\U0001F632', '\U0001F633', '\U0001F634', '\U0001F635', '\U0001F636', '\U0001F637', '\U0001F638', '\U0001F639', '\U0001F63A', '\U0001F63B', '\U0001F63C', '\U0001F63D', '\U0001F63E', '\U0001F63F',  # Emoticons
            '\U0001F640', '\U0001F641', '\U0001F642', '\U0001F643', '\U0001F644', '\U0001F645', '\U0001F646', '\U0001F647', '\U0001F648', '\U0001F649', '\U0001F64A', '\U0001F64B', '\U0001F64C', '\U0001F64D', '\U0001F64E', '\U0001F64F',  # Emoticons
            '\U0001F650', '\U0001F651', '\U0001F652', '\U0001F653', '\U0001F654', '\U0001F655', '\U0001F656', '\U0001F657', '\U0001F658', '\U0001F659', '\U0001F65A', '\U0001F65B', '\U0001F65C', '\U0001F65D', '\U0001F65E', '\U0001F65F',  # Ornamental Dingbats
            '\U0001F660', '\U0001F661', '\U0001F662', '\U0001F663', '\U0001F664', '\U0001F665', '\U0001F666', '\U0001F667', '\U0001F668', '\U0001F669', '\U0001F66A', '\U0001F66B', '\U0001F66C', '\U0001F66D', '\U0001F66E', '\U0001F66F',  # Ornamental Dingbats
            '\U0001F670', '\U0001F671', '\U0001F672', '\U0001F673', '\U0001F674', '\U0001F675', '\U0001F676', '\U0001F677', '\U0001F678', '\U0001F679', '\U0001F67A', '\U0001F67B', '\U0001F67C', '\U0001F67D', '\U0001F67E', '\U0001F67F',  # Ornamental Dingbats
            '\U0001F680', '\U0001F681', '\U0001F682', '\U0001F683', '\U0001F684', '\U0001F685', '\U0001F686', '\U0001F687', '\U0001F688', '\U0001F689', '\U0001F68A', '\U0001F68B', '\U0001F68C', '\U0001F68D', '\U0001F68E', '\U0001F68F',  # Transport and Map
            '\U0001F690', '\U0001F691', '\U0001F692', '\U0001F693', '\U0001F694', '\U0001F695', '\U0001F696', '\U0001F697', '\U0001F698', '\U0001F699', '\U0001F69A', '\U0001F69B', '\U0001F69C', '\U0001F69D', '\U0001F69E', '\U0001F69F',  # Transport and Map
            '\U0001F6A0', '\U0001F6A1', '\U0001F6A2', '\U0001F6A3', '\U0001F6A4', '\U0001F6A5', '\U0001F6A6', '\U0001F6A7', '\U0001F6A8', '\U0001F6A9', '\U0001F6AA', '\U0001F6AB', '\U0001F6AC', '\U0001F6AD', '\U0001F6AE', '\U0001F6AF',  # Transport and Map
            '\U0001F6B0', '\U0001F6B1', '\U0001F6B2', '\U0001F6B3', '\U0001F6B4', '\U0001F6B5', '\U0001F6B6', '\U0001F6B7', '\U0001F6B8', '\U0001F6B9', '\U0001F6BA', '\U0001F6BB', '\U0001F6BC', '\U0001F6BD', '\U0001F6BE', '\U0001F6BF',  # Transport and Map
            '\U0001F6C0', '\U0001F6C1', '\U0001F6C2', '\U0001F6C3', '\U0001F6C4', '\U0001F6C5', '\U0001F6C6', '\U0001F6C7', '\U0001F6C8', '\U0001F6C9', '\U0001F6CA', '\U0001F6CB', '\U0001F6CC', '\U0001F6CD', '\U0001F6CE', '\U0001F6CF',  # Transport and Map
            '\U0001F6D0', '\U0001F6D1', '\U0001F6D2', '\U0001F6D3', '\U0001F6D4', '\U0001F6D5', '\U0001F6D6', '\U0001F6D7', '\U0001F6D8', '\U0001F6D9', '\U0001F6DA', '\U0001F6DB', '\U0001F6DC', '\U0001F6DD', '\U0001F6DE', '\U0001F6DF',  # Transport and Map
            '\U0001F6E0', '\U0001F6E1', '\U0001F6E2', '\U0001F6E3', '\U0001F6E4', '\U0001F6E5', '\U0001F6E6', '\U0001F6E7', '\U0001F6E8', '\U0001F6E9', '\U0001F6EA', '\U0001F6EB', '\U0001F6EC', '\U0001F6ED', '\U0001F6EE', '\U0001F6EF',  # Transport and Map
            '\U0001F6F0', '\U0001F6F1', '\U0001F6F2', '\U0001F6F3', '\U0001F6F4', '\U0001F6F5', '\U0001F6F6', '\U0001F6F7', '\U0001F6F8', '\U0001F6F9', '\U0001F6FA', '\U0001F6FB', '\U0001F6FC', '\U0001F6FD', '\U0001F6FE', '\U0001F6FF',  # Transport and Map
            '\U0001F700', '\U0001F701', '\U0001F702', '\U0001F703', '\U0001F704', '\U0001F705', '\U0001F706', '\U0001F707', '\U0001F708', '\U0001F709', '\U0001F70A', '\U0001F70B', '\U0001F70C', '\U0001F70D', '\U0001F70E', '\U0001F70F',  # Alchemical Symbols
            '\U0001F710', '\U0001F711', '\U0001F712', '\U0001F713', '\U0001F714', '\U0001F715', '\U0001F716', '\U0001F717', '\U0001F718', '\U0001F719', '\U0001F71A', '\U0001F71B', '\U0001F71C', '\U0001F71D', '\U0001F71E', '\U0001F71F',  # Alchemical Symbols
            '\U0001F720', '\U0001F721', '\U0001F722', '\U0001F723', '\U0001F724', '\U0001F725', '\U0001F726', '\U0001F727', '\U0001F728', '\U0001F729', '\U0001F72A', '\U0001F72B', '\U0001F72C', '\U0001F72D', '\U0001F72E', '\U0001F72F',  # Alchemical Symbols
            '\U0001F730', '\U0001F731', '\U0001F732', '\U0001F733', '\U0001F734', '\U0001F735', '\U0001F736', '\U0001F737', '\U0001F738', '\U0001F739', '\U0001F73A', '\U0001F73B', '\U0001F73C', '\U0001F73D', '\U0001F73E', '\U0001F73F',  # Alchemical Symbols
            '\U0001F740', '\U0001F741', '\U0001F742', '\U0001F743', '\U0001F744', '\U0001F745', '\U0001F746', '\U0001F747', '\U0001F748', '\U0001F749', '\U0001F74A', '\U0001F74B', '\U0001F74C', '\U0001F74D', '\U0001F74E', '\U0001F74F',  # Alchemical Symbols
            '\U0001F750', '\U0001F751', '\U0001F752', '\U0001F753', '\U0001F754', '\U0001F755', '\U0001F756', '\U0001F757', '\U0001F758', '\U0001F759', '\U0001F75A', '\U0001F75B', '\U0001F75C', '\U0001F75D', '\U0001F75E', '\U0001F75F',  # Alchemical Symbols
            '\U0001F760', '\U0001F761', '\U0001F762', '\U0001F763', '\U0001F764', '\U0001F765', '\U0001F766', '\U0001F767', '\U0001F768', '\U0001F769', '\U0001F76A', '\U0001F76B', '\U0001F76C', '\U0001F76D', '\U0001F76E', '\U0001F76F',  # Alchemical Symbols
            '\U0001F770', '\U0001F771', '\U0001F772', '\U0001F773', '\U0001F774', '\U0001F775', '\U0001F776', '\U0001F777', '\U0001F778', '\U0001F779', '\U0001F77A', '\U0001F77B', '\U0001F77C', '\U0001F77D', '\U0001F77E', '\U0001F77F',  # Alchemical Symbols
            '\U0001F780', '\U0001F781', '\U0001F782', '\U0001F783', '\U0001F784', '\U0001F785', '\U0001F786', '\U0001F787', '\U0001F788', '\U0001F789', '\U0001F78A', '\U0001F78B', '\U0001F78C', '\U0001F78D', '\U0001F78E', '\U0001F78F',  # Geometric Shapes Extended
            '\U0001F790', '\U0001F791', '\U0001F792', '\U0001F793', '\U0001F794', '\U0001F795', '\U0001F796', '\U0001F797', '\U0001F798', '\U0001F799', '\U0001F79A', '\U0001F79B', '\U0001F79C', '\U0001F79D', '\U0001F79E', '\U0001F79F',  # Geometric Shapes Extended
            '\U0001F7A0', '\U0001F7A1', '\U0001F7A2', '\U0001F7A3', '\U0001F7A4', '\U0001F7A5', '\U0001F7A6', '\U0001F7A7', '\U0001F7A8', '\U0001F7A9', '\U0001F7AA', '\U0001F7AB', '\U0001F7AC', '\U0001F7AD', '\U0001F7AE', '\U0001F7AF',  # Geometric Shapes Extended
            '\U0001F7B0', '\U0001F7B1', '\U0001F7B2', '\U0001F7B3', '\U0001F7B4', '\U0001F7B5', '\U0001F7B6', '\U0001F7B7', '\U0001F7B8', '\U0001F7B9', '\U0001F7BA', '\U0001F7BB', '\U0001F7BC', '\U0001F7BD', '\U0001F7BE', '\U0001F7BF',  # Geometric Shapes Extended
            '\U0001F7C0', '\U0001F7C1', '\U0001F7C2', '\U0001F7C3', '\U0001F7C4', '\U0001F7C5', '\U0001F7C6', '\U0001F7C7', '\U0001F7C8', '\U0001F7C9', '\U0001F7CA', '\U0001F7CB', '\U0001F7CC', '\U0001F7CD', '\U0001F7CE', '\U0001F7CF',  # Geometric Shapes Extended
            '\U0001F7D0', '\U0001F7D1', '\U0001F7D2', '\U0001F7D3', '\U0001F7D4', '\U0001F7D5', '\U0001F7D6', '\U0001F7D7', '\U0001F7D8', '\U0001F7D9', '\U0001F7DA', '\U0001F7DB', '\U0001F7DC', '\U0001F7DD', '\U0001F7DE', '\U0001F7DF',  # Geometric Shapes Extended
            '\U0001F7E0', '\U0001F7E1', '\U0001F7E2', '\U0001F7E3', '\U0001F7E4', '\U0001F7E5', '\U0001F7E6', '\U0001F7E7', '\U0001F7E8', '\U0001F7E9', '\U0001F7EA', '\U0001F7EB', '\U0001F7EC', '\U0001F7ED', '\U0001F7EE', '\U0001F7EF',  # Geometric Shapes Extended
            '\U0001F7F0', '\U0001F7F1', '\U0001F7F2', '\U0001F7F3', '\U0001F7F4', '\U0001F7F5', '\U0001F7F6', '\U0001F7F7', '\U0001F7F8', '\U0001F7F9', '\U0001F7FA', '\U0001F7FB', '\U0001F7FC', '\U0001F7FD', '\U0001F7FE', '\U0001F7FF',  # Geometric Shapes Extended
            '\U0001F800', '\U0001F801', '\U0001F802', '\U0001F803', '\U0001F804', '\U0001F805', '\U0001F806', '\U0001F807', '\U0001F808', '\U0001F809', '\U0001F80A', '\U0001F80B', '\U0001F80C', '\U0001F80D', '\U0001F80E', '\U0001F80F',  # Supplemental Arrows-C
            '\U0001F810', '\U0001F811', '\U0001F812', '\U0001F813', '\U0001F814', '\U0001F815', '\U0001F816', '\U0001F817', '\U0001F818', '\U0001F819', '\U0001F81A', '\U0001F81B', '\U0001F81C', '\U0001F81D', '\U0001F81E', '\U0001F81F',  # Supplemental Arrows-C
            '\U0001F820', '\U0001F821', '\U0001F822', '\U0001F823', '\U0001F824', '\U0001F825', '\U0001F826', '\U0001F827', '\U0001F828', '\U0001F829', '\U0001F82A', '\U0001F82B', '\U0001F82C', '\U0001F82D', '\U0001F82E', '\U0001F82F',  # Supplemental Arrows-C
            '\U0001F830', '\U0001F831', '\U0001F832', '\U0001F833', '\U0001F834', '\U0001F835', '\U0001F836', '\U0001F837', '\U0001F838', '\U0001F839', '\U0001F83A', '\U0001F83B', '\U0001F83C', '\U0001F83D', '\U0001F83E', '\U0001F83F',  # Supplemental Arrows-C
            '\U0001F840', '\U0001F841', '\U0001F842', '\U0001F843', '\U0001F844', '\U0001F845', '\U0001F846', '\U0001F847', '\U0001F848', '\U0001F849', '\U0001F84A', '\U0001F84B', '\U0001F84C', '\U0001F84D', '\U0001F84E', '\U0001F84F',  # Supplemental Arrows-C
            '\U0001F850', '\U0001F851', '\U0001F852', '\U0001F853', '\U0001F854', '\U0001F855', '\U0001F856', '\U0001F857', '\U0001F858', '\U0001F859', '\U0001F85A', '\U0001F85B', '\U0001F85C', '\U0001F85D', '\U0001F85E', '\U0001F85F',  # Supplemental Arrows-C
            '\U0001F860', '\U0001F861', '\U0001F862', '\U0001F863', '\U0001F864', '\U0001F865', '\U0001F866', '\U0001F867', '\U0001F868', '\U0001F869', '\U0001F86A', '\U0001F86B', '\U0001F86C', '\U0001F86D', '\U0001F86E', '\U0001F86F',  # Supplemental Arrows-C
            '\U0001F870', '\U0001F871', '\U0001F872', '\U0001F873', '\U0001F874', '\U0001F875', '\U0001F876', '\U0001F877', '\U0001F878', '\U0001F879', '\U0001F87A', '\U0001F87B', '\U0001F87C', '\U0001F87D', '\U0001F87E', '\U0001F87F',  # Supplemental Arrows-C
            '\U0001F880', '\U0001F881', '\U0001F882', '\U0001F883', '\U0001F884', '\U0001F885', '\U0001F886', '\U0001F887', '\U0001F888', '\U0001F889', '\U0001F88A', '\U0001F88B', '\U0001F88C', '\U0001F88D', '\U0001F88E', '\U0001F88F',  # Supplemental Arrows-C
            '\U0001F890', '\U0001F891', '\U0001F892', '\U0001F893', '\U0001F894', '\U0001F895', '\U0001F896', '\U0001F897', '\U0001F898', '\U0001F899', '\U0001F89A', '\U0001F89B', '\U0001F89C', '\U0001F89D', '\U0001F89E', '\U0001F89F',  # Supplemental Arrows-C
            '\U0001F8A0', '\U0001F8A1', '\U0001F8A2', '\U0001F8A3', '\U0001F8A4', '\U0001F8A5', '\U0001F8A6', '\U0001F8A7', '\U0001F8A8', '\U0001F8A9', '\U0001F8AA', '\U0001F8AB', '\U0001F8AC', '\U0001F8AD', '\U0001F8AE', '\U0001F8AF',  # Supplemental Arrows-C
            '\U0001F8B0', '\U0001F8B1', '\U0001F8B2', '\U0001F8B3', '\U0001F8B4', '\U0001F8B5', '\U0001F8B6', '\U0001F8B7', '\U0001F8B8', '\U0001F8B9', '\U0001F8BA', '\U0001F8BB', '\U0001F8BC', '\U0001F8BD', '\U0001F8BE', '\U0001F8BF',  # Supplemental Arrows-C
            '\U0001F8C0', '\U0001F8C1', '\U0001F8C2', '\U0001F8C3', '\U0001F8C4', '\U0001F8C5', '\U0001F8C6', '\U0001F8C7', '\U0001F8C8', '\U0001F8C9', '\U0001F8CA', '\U0001F8CB', '\U0001F8CC', '\U0001F8CD', '\U0001F8CE', '\U0001F8CF',  # Supplemental Arrows-C
            '\U0001F8D0', '\U0001F8D1', '\U0001F8D2', '\U0001F8D3', '\U0001F8D4', '\U0001F8D5', '\U0001F8D6', '\U0001F8D7', '\U0001F8D8', '\U0001F8D9', '\U0001F8DA', '\U0001F8DB', '\U0001F8DC', '\U0001F8DD', '\U0001F8DE', '\U0001F8DF',  # Supplemental Arrows-C
            '\U0001F8E0', '\U0001F8E1', '\U0001F8E2', '\U0001F8E3', '\U0001F8E4', '\U0001F8E5', '\U0001F8E6', '\U0001F8E7', '\U0001F8E8', '\U0001F8E9', '\U0001F8EA', '\U0001F8EB', '\U0001F8EC', '\U0001F8ED', '\U0001F8EE', '\U0001F8EF',  # Supplemental Arrows-C
            '\U0001F8F0', '\U0001F8F1', '\U0001F8F2', '\U0001F8F3', '\U0001F8F4', '\U0001F8F5', '\U0001F8F6', '\U0001F8F7', '\U0001F8F8', '\U0001F8F9', '\U0001F8FA', '\U0001F8FB', '\U0001F8FC', '\U0001F8FD', '\U0001F8FE', '\U0001F8FF',  # Supplemental Arrows-C
            '\U0001F900', '\U0001F901', '\U0001F902', '\U0001F903', '\U0001F904', '\U0001F905', '\U0001F906', '\U0001F907', '\U0001F908', '\U0001F909', '\U0001F90A', '\U0001F90B', '\U0001F90C', '\U0001F90D', '\U0001F90E', '\U0001F90F',  # Supplemental Symbols and Pictographs
            '\U0001F910', '\U0001F911', '\U0001F912', '\U0001F913', '\U0001F914', '\U0001F915', '\U0001F916', '\U0001F917', '\U0001F918', '\U0001F919', '\U0001F91A', '\U0001F91B', '\U0001F91C', '\U0001F91D', '\U0001F91E', '\U0001F91F',  # Supplemental Symbols and Pictographs
            '\U0001F920', '\U0001F921', '\U0001F922', '\U0001F923', '\U0001F924', '\U0001F925', '\U0001F926', '\U0001F927', '\U0001F928', '\U0001F929', '\U0001F92A', '\U0001F92B', '\U0001F92C', '\U0001F92D', '\U0001F92E', '\U0001F92F',  # Supplemental Symbols and Pictographs
            '\U0001F930', '\U0001F931', '\U0001F932', '\U0001F933', '\U0001F934', '\U0001F935', '\U0001F936', '\U0001F937', '\U0001F938', '\U0001F939', '\U0001F93A', '\U0001F93B', '\U0001F93C', '\U0001F93D', '\U0001F93E', '\U0001F93F',  # Supplemental Symbols and Pictographs
            '\U0001F940', '\U0001F941', '\U0001F942', '\U0001F943', '\U0001F944', '\U0001F945', '\U0001F946', '\U0001F947', '\U0001F948', '\U0001F949', '\U0001F94A', '\U0001F94B', '\U0001F94C', '\U0001F94D', '\U0001F94E', '\U0001F94F',  # Supplemental Symbols and Pictographs
            '\U0001F950', '\U0001F951', '\U0001F952', '\U0001F953', '\U0001F954', '\U0001F955', '\U0001F956', '\U0001F957', '\U0001F958', '\U0001F959', '\U0001F95A', '\U0001F95B', '\U0001F95C', '\U0001F95D', '\U0001F95E', '\U0001F95F',  # Supplemental Symbols and Pictographs
            '\U0001F960', '\U0001F961', '\U0001F962', '\U0001F963', '\U0001F964', '\U0001F965', '\U0001F966', '\U0001F967', '\U0001F968', '\U0001F969', '\U0001F96A', '\U0001F96B', '\U0001F96C', '\U0001F96D', '\U0001F96E', '\U0001F96F',  # Supplemental Symbols and Pictographs
            '\U0001F970', '\U0001F971', '\U0001F972', '\U0001F973', '\U0001F974', '\U0001F975', '\U0001F976', '\U0001F977', '\U0001F978', '\U0001F979', '\U0001F97A', '\U0001F97B', '\U0001F97C', '\U0001F97D', '\U0001F97E', '\U0001F97F',  # Supplemental Symbols and Pictographs
            '\U0001F980', '\U0001F981', '\U0001F982', '\U0001F983', '\U0001F984', '\U0001F985', '\U0001F986', '\U0001F987', '\U0001F988', '\U0001F989', '\U0001F98A', '\U0001F98B', '\U0001F98C', '\U0001F98D', '\U0001F98E', '\U0001F98F',  # Supplemental Symbols and Pictographs
            '\U0001F990', '\U0001F991', '\U0001F992', '\U0001F993', '\U0001F994', '\U0001F995', '\U0001F996', '\U0001F997', '\U0001F998', '\U0001F999', '\U0001F99A', '\U0001F99B', '\U0001F99C', '\U0001F99D', '\U0001F99E', '\U0001F99F',  # Supplemental Symbols and Pictographs
            '\U0001F9A0', '\U0001F9A1', '\U0001F9A2', '\U0001F9A3', '\U0001F9A4', '\U0001F9A5', '\U0001F9A6', '\U0001F9A7', '\U0001F9A8', '\U0001F9A9', '\U0001F9AA', '\U0001F9AB', '\U0001F9AC', '\U0001F9AD', '\U0001F9AE', '\U0001F9AF',  # Supplemental Symbols and Pictographs
            '\U0001F9B0', '\U0001F9B1', '\U0001F9B2', '\U0001F9B3', '\U0001F9B4', '\U0001F9B5', '\U0001F9B6', '\U0001F9B7', '\U0001F9B8', '\U0001F9B9', '\U0001F9BA', '\U0001F9BB', '\U0001F9BC', '\U0001F9BD', '\U0001F9BE', '\U0001F9BF',  # Supplemental Symbols and Pictographs
            '\U0001F9C0', '\U0001F9C1', '\U0001F9C2', '\U0001F9C3', '\U0001F9C4', '\U0001F9C5', '\U0001F9C6', '\U0001F9C7', '\U0001F9C8', '\U0001F9C9', '\U0001F9CA', '\U0001F9CB', '\U0001F9CC', '\U0001F9CD', '\U0001F9CE', '\U0001F9CF',  # Supplemental Symbols and Pictographs
            '\U0001F9D0', '\U0001F9D1', '\U0001F9D2', '\U0001F9D3', '\U0001F9D4', '\U0001F9D5', '\U0001F9D6', '\U0001F9D7', '\U0001F9D8', '\U0001F9D9', '\U0001F9DA', '\U0001F9DB', '\U0001F9DC', '\U0001F9DD', '\U0001F9DE', '\U0001F9DF',  # Supplemental Symbols and Pictographs
            '\U0001F9E0', '\U0001F9E1', '\U0001F9E2', '\U0001F9E3', '\U0001F9E4', '\U0001F9E5', '\U0001F9E6', '\U0001F9E7', '\U0001F9E8', '\U0001F9E9', '\U0001F9EA', '\U0001F9EB', '\U0001F9EC', '\U0001F9ED', '\U0001F9EE', '\U0001F9EF',  # Supplemental Symbols and Pictographs
            '\U0001F9F0', '\U0001F9F1', '\U0001F9F2', '\U0001F9F3', '\U0001F9F4', '\U0001F9F5', '\U0001F9F6', '\U0001F9F7', '\U0001F9F8', '\U0001F9F9', '\U0001F9FA', '\U0001F9FB', '\U0001F9FC', '\U0001F9FD', '\U0001F9FE', '\U0001F9FF',  # Supplemental Symbols and Pictographs
            # Card suits and other decorative symbols
            '\u2660', '\u2661', '\u2662', '\u2663', '\u2664', '\u2665', '\u2666', '\u2667',  # Card suits
            '\u25a0', '\u25a1', '\u25aa', '\u25ab', '\u25ac', '\u25ad', '\u25ae', '\u25af', '\u25b0', '\u25b1', '\u25b2', '\u25b3', '\u25b4', '\u25b5', '\u25b6', '\u25b7', '\u25b8', '\u25b9', '\u25ba', '\u25bb', '\u25bc', '\u25bd', '\u25be', '\u25bf', '\u25c0', '\u25c1', '\u25c2', '\u25c3', '\u25c4', '\u25c5', '\u25c6', '\u25c7', '\u25c8', '\u25c9', '\u25ca', '\u25cb', '\u25cc', '\u25cd', '\u25ce', '\u25cf',  # Geometric shapes
            '\u25d0', '\u25d1', '\u25d2', '\u25d3', '\u25d4', '\u25d5', '\u25d6', '\u25d7', '\u25d8', '\u25d9', '\u25da', '\u25db', '\u25dc', '\u25dd', '\u25de', '\u25df', '\u25e0', '\u25e1', '\u25e2', '\u25e3', '\u25e4', '\u25e5', '\u25e6', '\u25e7', '\u25e8', '\u25e9', '\u25ea', '\u25eb', '\u25ec', '\u25ed', '\u25ee', '\u25ef',  # Geometric shapes
            '\u25f0', '\u25f1', '\u25f2', '\u25f3', '\u25f4', '\u25f5', '\u25f6', '\u25f7', '\u25f8', '\u25f9', '\u25fa', '\u25fb', '\u25fc', '\u25fd', '\u25fe', '\u25ff',  # Geometric shapes
            # Box drawing characters
            '\u2500', '\u2501', '\u2502', '\u2503', '\u2504', '\u2505', '\u2506', '\u2507', '\u2508', '\u2509', '\u250a', '\u250b', '\u250c', '\u250d', '\u250e', '\u250f', '\u2510', '\u2511', '\u2512', '\u2513', '\u2514', '\u2515', '\u2516', '\u2517', '\u2518', '\u2519', '\u251a', '\u251b', '\u251c', '\u251d', '\u251e', '\u251f',  # Box drawing
            '\u2520', '\u2521', '\u2522', '\u2523', '\u2524', '\u2525', '\u2526', '\u2527', '\u2528', '\u2529', '\u252a', '\u252b', '\u252c', '\u252d', '\u252e', '\u252f', '\u2530', '\u2531', '\u2532', '\u2533', '\u2534', '\u2535', '\u2536', '\u2537', '\u2538', '\u2539', '\u253a', '\u253b', '\u253c', '\u253d', '\u253e', '\u253f',  # Box drawing
            '\u2540', '\u2541', '\u2542', '\u2543', '\u2544', '\u2545', '\u2546', '\u2547', '\u2548', '\u2549', '\u254a', '\u254b', '\u254c', '\u254d', '\u254e', '\u254f', '\u2550', '\u2551', '\u2552', '\u2553', '\u2554', '\u2555', '\u2556', '\u2557', '\u2558', '\u2559', '\u255a', '\u255b', '\u255c', '\u255d', '\u255e', '\u255f',  # Box drawing
            '\u2560', '\u2561', '\u2562', '\u2563', '\u2564', '\u2565', '\u2566', '\u2567', '\u2568', '\u2569', '\u256a', '\u256b', '\u256c', '\u256d', '\u256e', '\u256f', '\u2570', '\u2571', '\u2572', '\u2573', '\u2574', '\u2575', '\u2576', '\u2577', '\u2578', '\u2579', '\u257a', '\u257b', '\u257c', '\u257d', '\u257e', '\u257f',  # Box drawing
            # Arrow symbols
            '\u2190', '\u2191', '\u2192', '\u2193', '\u2194', '\u2195', '\u2196', '\u2197', '\u2198', '\u2199', '\u219a', '\u219b', '\u219c', '\u219d', '\u219e', '\u219f', '\u21a0', '\u21a1', '\u21a2', '\u21a3', '\u21a4', '\u21a5', '\u21a6', '\u21a7', '\u21a8', '\u21a9', '\u21aa', '\u21ab', '\u21ac', '\u21ad', '\u21ae', '\u21af',  # Arrows
            '\u21b0', '\u21b1', '\u21b2', '\u21b3', '\u21b4', '\u21b5', '\u21b6', '\u21b7', '\u21b8', '\u21b9', '\u21ba', '\u21bb', '\u21bc', '\u21bd', '\u21be', '\u21bf', '\u21c0', '\u21c1', '\u21c2', '\u21c3', '\u21c4', '\u21c5', '\u21c6', '\u21c7', '\u21c8', '\u21c9', '\u21ca', '\u21cb', '\u21cc', '\u21cd', '\u21ce', '\u21cf',  # Arrows
            '\u21d0', '\u21d1', '\u21d2', '\u21d3', '\u21d4', '\u21d5', '\u21d6', '\u21d7', '\u21d8', '\u21d9', '\u21da', '\u21db', '\u21dc', '\u21dd', '\u21de', '\u21df', '\u21e0', '\u21e1', '\u21e2', '\u21e3', '\u21e4', '\u21e5', '\u21e6', '\u21e7', '\u21e8', '\u21e9', '\u21ea', '\u21eb', '\u21ec', '\u21ed', '\u21ee', '\u21ef',  # Arrows
            '\u21f0', '\u21f1', '\u21f2', '\u21f3', '\u21f4', '\u21f5', '\u21f6', '\u21f7', '\u21f8', '\u21f9', '\u21fa', '\u21fb', '\u21fc', '\u21fd', '\u21fe', '\u21ff',  # Arrows
            # Math symbols
            '\u2200', '\u2201', '\u2202', '\u2203', '\u2204', '\u2205', '\u2206', '\u2207', '\u2208', '\u2209', '\u220a', '\u220b', '\u220c', '\u220d', '\u220e', '\u220f', '\u2210', '\u2211', '\u2212', '\u2213', '\u2214', '\u2215', '\u2216', '\u2217', '\u2218', '\u2219', '\u221a', '\u221b', '\u221c', '\u221d', '\u221e', '\u221f',  # Math symbols
            '\u2220', '\u2221', '\u2222', '\u2223', '\u2224', '\u2225', '\u2226', '\u2227', '\u2228', '\u2229', '\u222a', '\u222b', '\u222c', '\u222d', '\u222e', '\u222f', '\u2230', '\u2231', '\u2232', '\u2233', '\u2234', '\u2235', '\u2236', '\u2237', '\u2238', '\u2239', '\u223a', '\u223b', '\u223c', '\u223d', '\u223e', '\u223f',  # Math symbols
            '\u2240', '\u2241', '\u2242', '\u2243', '\u2244', '\u2245', '\u2246', '\u2247', '\u2248', '\u2249', '\u224a', '\u224b', '\u224c', '\u224d', '\u224e', '\u224f', '\u2250', '\u2251', '\u2252', '\u2253', '\u2254', '\u2255', '\u2256', '\u2257', '\u2258', '\u2259', '\u225a', '\u225b', '\u225c', '\u225d', '\u225e', '\u225f',  # Math symbols
            '\u2260', '\u2261', '\u2262', '\u2263', '\u2264', '\u2265', '\u2266', '\u2267', '\u2268', '\u2269', '\u226a', '\u226b', '\u226c', '\u226d', '\u226e', '\u226f', '\u2270', '\u2271', '\u2272', '\u2273', '\u2274', '\u2275', '\u2276', '\u2277', '\u2278', '\u2279', '\u227a', '\u227b', '\u227c', '\u227d', '\u227e', '\u227f',  # Math symbols
            '\u2280', '\u2281', '\u2282', '\u2283', '\u2284', '\u2285', '\u2286', '\u2287', '\u2288', '\u2289', '\u228a', '\u228b', '\u228c', '\u228d', '\u228e', '\u228f', '\u2290', '\u2291', '\u2292', '\u2293', '\u2294', '\u2295', '\u2296', '\u2297', '\u2298', '\u2299', '\u229a', '\u229b', '\u229c', '\u229d', '\u229e', '\u229f',  # Math symbols
            '\u22a0', '\u22a1', '\u22a2', '\u22a3', '\u22a4', '\u22a5', '\u22a6', '\u22a7', '\u22a8', '\u22a9', '\u22aa', '\u22ab', '\u22ac', '\u22ad', '\u22ae', '\u22af', '\u22b0', '\u22b1', '\u22b2', '\u22b3', '\u22b4', '\u22b5', '\u22b6', '\u22b7', '\u22b8', '\u22b9', '\u22ba', '\u22bb', '\u22bc', '\u22bd', '\u22be', '\u22bf',  # Math symbols
            '\u22c0', '\u22c1', '\u22c2', '\u22c3', '\u22c4', '\u22c5', '\u22c6', '\u22c7', '\u22c8', '\u22c9', '\u22ca', '\u22cb', '\u22cc', '\u22cd', '\u22ce', '\u22cf', '\u22d0', '\u22d1', '\u22d2', '\u22d3', '\u22d4', '\u22d5', '\u22d6', '\u22d7', '\u22d8', '\u22d9', '\u22da', '\u22db', '\u22dc', '\u22dd', '\u22de', '\u22df',  # Math symbols
            '\u22e0', '\u22e1', '\u22e2', '\u22e3', '\u22e4', '\u22e5', '\u22e6', '\u22e7', '\u22e8', '\u22e9', '\u22ea', '\u22eb', '\u22ec', '\u22ed', '\u22ee', '\u22ef', '\u22f0', '\u22f1', '\u22f2', '\u22f3', '\u22f4', '\u22f5', '\u22f6', '\u22f7', '\u22f8', '\u22f9', '\u22fa', '\u22fb', '\u22fc', '\u22fd', '\u22fe', '\u22ff',  # Math symbols
        ]

        # File extensions to exclude from emoji checking
        excluded_extensions = {'.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico', '.woff', '.woff2', '.ttf', '.eot'}

        for file_path in file_paths:
            # Skip binary files and images
            if file_path.suffix.lower() in excluded_extensions:
                continue

            try:
                with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                    content = f.read()

                # Check for forbidden characters
                found_chars = []
                for char in forbidden_chars:
                    if char in content:
                        found_chars.append(char)

                if found_chars:
                    # Find line numbers where forbidden characters appear
                    lines = content.split('\n')
                    violations = []
                    for line_num, line in enumerate(lines, 1):
                        for char in found_chars:
                            if char in line:
                                violations.append((line_num, char, line.strip()))

                    # Create separate message for each violation for better AI agent understanding
                    for line_num, char, line_content in violations:
                        # Get character name/description
                        char_name = self.get_character_description(char)

                        result.add_critical(
                            "EMOJI_DETECTED",
                            f"Forbidden Unicode character {char_name} (U+{ord(char):04X}) found in file",
                            file_path=file_path,
                            line_number=line_num,
                            suggestion=f"Replace the forbidden Unicode character with plain ASCII text equivalent. Remove all emoji and special Unicode characters.",
                            context={
                                "character_code": f"U+{ord(char):04X}",
                                "character_name": char_name,
                                "line_content_preview": line_content.replace(char, "[FORBIDDEN_CHAR]"),
                                "position_in_line": line_content.find(char) + 1
                            }
                        )

            except (UnicodeDecodeError, OSError) as e:
                # Skip files that can't be read as text
                result.add_info(
                    "FILE_SKIPPED",
                    f"Skipped emoji check for binary/unreadable file: {e}",
                    file_path=file_path
                )

        return result
