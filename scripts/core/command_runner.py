#!/usr/bin/env python3
"""
NECPGAME Command Runner
SOLID: Single Responsibility - runs external commands
"""

import shlex
import subprocess
from pathlib import Path
from typing import List, Optional, Union

from scripts.core.logger import Logger


class CommandRunner:
    """
    Runs external commands safely.
    Single Responsibility: Execute commands and handle results.
    """

    def __init__(self, logger: Logger, cwd: Optional[Path] = None):
        self.logger = logger
        self.cwd = cwd or Path.cwd()

    def run(self, command: Union[str, List[str]],
            capture_output: bool = True,
            check: bool = True,
            timeout: Optional[int] = None,
            env: Optional[dict] = None) -> subprocess.CompletedProcess:
        """
        Run command and return result.
        """

        if isinstance(command, str):
            command = shlex.split(command)

        self.logger.debug(f"Running: {' '.join(command)}")
        if self.cwd != Path.cwd():
            self.logger.debug(f"In directory: {self.cwd}")

        try:
            result = subprocess.run(
                command,
                cwd=self.cwd,
                capture_output=capture_output,
                text=True,
                check=check,
                timeout=timeout,
                env=env
            )

            if capture_output and result.stdout:
                self.logger.debug(f"Output: {result.stdout.strip()}")
            if capture_output and result.stderr:
                self.logger.debug(f"Errors: {result.stderr.strip()}")

            return result

        except subprocess.CalledProcessError as e:
            self.logger.error(f"Command failed: {' '.join(command)}")
            self.logger.error(f"Exit code: {e.returncode}")
            if e.stdout:
                self.logger.error(f"Stdout: {e.stdout}")
            if e.stderr:
                self.logger.error(f"Stderr: {e.stderr}")
            raise

        except subprocess.TimeoutExpired:
            self.logger.error(f"Command timeout: {' '.join(command)}")
            raise

    def run_quiet(self, command: Union[str, List[str]], **kwargs) -> subprocess.CompletedProcess:
        """Run command without debug logging"""
        return self.run(command, **kwargs)

    def check_command_exists(self, command: str) -> bool:
        """Check if command is available"""
        try:
            self.run(['which', command], capture_output=True, check=False)
            return True
        except:
            return False
