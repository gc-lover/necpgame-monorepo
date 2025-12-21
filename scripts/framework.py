#!/usr/bin/env python3
"""
NECPGAME Python Script Framework

Standard framework for all Python scripts in the project.
Provides common functionality, logging, error handling, and CLI utilities.

Usage:
    python scripts/framework.py  # Shows available utilities

    # Inherit in your script:
    from scripts.framework import ScriptFramework

    class MyScript(ScriptFramework):
        def run(self):
            # Your script logic here
            pass

    if __name__ == "__main__":
        script = MyScript()
        script.main()
"""

import os
import sys
import logging
import argparse
from pathlib import Path
from typing import Optional, List, Dict, Any
import json
import subprocess
import time


class ScriptFramework:
    """Base class for all Python scripts in NECPGAME"""

    def __init__(self, name: str = None, description: str = None):
        self.name = name or self.__class__.__name__
        self.description = description or "NECPGAME script"
        self.project_root = Path(__file__).parent.parent
        self.scripts_dir = self.project_root / "scripts"

        # Setup logging
        self.setup_logging()

        # Setup argument parser
        self.parser = argparse.ArgumentParser(
            prog=self.name,
            description=self.description,
            formatter_class=argparse.RawDescriptionHelpFormatter,
            epilog=self.get_epilog()
        )

        # Add common arguments
        self.add_common_args()

        # Add script-specific arguments
        self.add_script_args()

    def setup_logging(self):
        """Setup structured logging"""
        logging.basicConfig(
            level=logging.INFO,
            format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
            datefmt='%Y-%m-%d %H:%M:%S'
        )
        self.logger = logging.getLogger(self.name)

    def add_common_args(self):
        """Add common arguments to all scripts"""
        self.parser.add_argument(
            '--verbose', '-v',
            action='store_true',
            help='Enable verbose logging'
        )
        self.parser.add_argument(
            '--dry-run',
            action='store_true',
            help='Show what would be done without making changes'
        )
        self.parser.add_argument(
            '--config',
            type=str,
            help='Path to config file (JSON/YAML)'
        )

    def add_script_args(self):
        """Override in subclasses to add script-specific arguments"""
        pass

    def get_epilog(self) -> str:
        """Get help epilog with project info"""
        return f"""
NECPGAME Project Script Framework
Project root: {self.project_root}
Scripts directory: {self.scripts_dir}

For help with specific script: <script> --help
For framework utilities: python scripts/framework.py --help
"""

    def parse_args(self):
        """Parse command line arguments"""
        return self.parser.parse_args()

    def validate_environment(self) -> bool:
        """Validate that script can run in current environment"""
        # Check if we're in project root
        if not (self.project_root / ".git").exists():
            self.logger.error(f"Not in project root: {self.project_root}")
            return False

        # Check Python version
        if sys.version_info < (3, 8):
            self.logger.error("Python 3.8+ required")
            return False

        return True

    def run_command(self, cmd: List[str], cwd: Optional[Path] = None,
                   capture_output: bool = False, check: bool = True) -> subprocess.CompletedProcess:
        """Run shell command with proper error handling"""
        try:
            self.logger.debug(f"Running: {' '.join(cmd)}")
            if cwd:
                self.logger.debug(f"In directory: {cwd}")

            result = subprocess.run(
                cmd,
                cwd=cwd or self.project_root,
                capture_output=capture_output,
                text=True,
                check=check
            )

            if capture_output:
                if result.stdout:
                    self.logger.debug(f"Output: {result.stdout.strip()}")
                if result.stderr:
                    self.logger.debug(f"Errors: {result.stderr.strip()}")

            return result

        except subprocess.CalledProcessError as e:
            self.logger.error(f"Command failed: {' '.join(cmd)}")
            self.logger.error(f"Exit code: {e.returncode}")
            if e.stdout:
                self.logger.error(f"Stdout: {e.stdout}")
            if e.stderr:
                self.logger.error(f"Stderr: {e.stderr}")
            raise

    def load_config(self, config_path: Optional[str] = None) -> Dict[str, Any]:
        """Load configuration from file"""
        if not config_path:
            config_path = self.project_root / "scripts" / "config.json"

        config_path = Path(config_path)
        if not config_path.exists():
            return {}

        with open(config_path, 'r', encoding='utf-8') as f:
            if config_path.suffix == '.json':
                return json.load(f)
            else:
                # Could add YAML support later
                self.logger.warning(f"Unsupported config format: {config_path.suffix}")
                return {}

    def save_config(self, config: Dict[str, Any], config_path: Optional[str] = None):
        """Save configuration to file"""
        if not config_path:
            config_path = self.project_root / "scripts" / "config.json"

        config_path = Path(config_path)
        config_path.parent.mkdir(parents=True, exist_ok=True)

        with open(config_path, 'w', encoding='utf-8') as f:
            json.dump(config, f, indent=2, ensure_ascii=False)

    def get_confirmation(self, message: str, default: bool = False) -> bool:
        """Get user confirmation"""
        default_text = "(Y/n)" if default else "(y/N)"
        response = input(f"{message} {default_text}: ").strip().lower()

        if not response:
            return default

        return response in ('y', 'yes', 'true', '1')

    def run(self):
        """Main script logic - override in subclasses"""
        raise NotImplementedError("Subclasses must implement run() method")

    def main(self):
        """Main entry point"""
        try:
            args = self.parse_args()

            if args.verbose:
                logging.getLogger().setLevel(logging.DEBUG)

            if not self.validate_environment():
                sys.exit(1)

            self.logger.info(f"Starting {self.name}")
            start_time = time.time()

            self.run()

            elapsed = time.time() - start_time
            self.logger.info(".2f")

        except KeyboardInterrupt:
            self.logger.info("Interrupted by user")
            sys.exit(1)
        except Exception as e:
            self.logger.error(f"Script failed: {e}", exc_info=True)
            sys.exit(1)


# Utility functions for common operations
def find_files(pattern: str, directory: Path = None) -> List[Path]:
    """Find files matching pattern"""
    if directory is None:
        directory = Path.cwd()

    return list(directory.glob(pattern))


def ensure_directory(path: Path):
    """Ensure directory exists"""
    path.mkdir(parents=True, exist_ok=True)


def read_file(path: Path, encoding: str = 'utf-8') -> str:
    """Read file content"""
    return path.read_text(encoding=encoding)


def write_file(path: Path, content: str, encoding: str = 'utf-8'):
    """Write file content"""
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding=encoding)


def is_git_clean() -> bool:
    """Check if git working directory is clean"""
    result = subprocess.run(
        ['git', 'status', '--porcelain'],
        capture_output=True,
        text=True,
        cwd=Path(__file__).parent.parent
    )
    return len(result.stdout.strip()) == 0


if __name__ == "__main__":
    # Show framework information when run directly
    framework = ScriptFramework("NECPGAME Script Framework", "Framework utilities for Python scripts")

    framework.parser.add_argument(
        '--list-scripts',
        action='store_true',
        help='List all Python scripts in project'
    )

    framework.parser.add_argument(
        '--validate-scripts',
        action='store_true',
        help='Validate all Python scripts for syntax errors'
    )

    args = framework.parse_args()

    if args.list_scripts:
        print("Python scripts in project:")
        scripts_dir = Path(__file__).parent
        for script in scripts_dir.glob("*.py"):
            if script.name != "framework.py":
                print(f"  {script.name}")
        for script in scripts_dir.glob("**/*.py"):
            print(f"  {script.relative_to(scripts_dir.parent)}")

    elif args.validate_scripts:
        print("Validating Python scripts...")
        scripts_dir = Path(__file__).parent
        failed = []

        for script in scripts_dir.glob("**/*.py"):
            try:
                subprocess.run(
                    [sys.executable, "-m", "py_compile", str(script)],
                    capture_output=True,
                    check=True
                )
                print(f"[OK] {script.relative_to(scripts_dir.parent)}")
            except subprocess.CalledProcessError:
                print(f"[ERROR] {script.relative_to(scripts_dir.parent)}")
                failed.append(script)

        if failed:
            print(f"\n{len(failed)} scripts have syntax errors")
            sys.exit(1)
        else:
            print("\nAll scripts are syntactically valid")

    else:
        framework.parser.print_help()
