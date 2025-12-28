#!/usr/bin/env python3
"""
Compilation Tester Component
SOLID: Single Responsibility - tests Go code compilation
"""

from pathlib import Path

import logging

from core.command_runner import CommandRunner


class CompilationTester:
    """
    Tests that the generated Go code compiles successfully.
    Single Responsibility: Test compilation.
    """

    def __init__(self, command_runner: CommandRunner, logger: logging.Logger):
        self.command_runner = command_runner
        self.logger = logger

    def test_compilation(self, service_dir: Path, service_name: str) -> None:
        """Test that the generated code compiles"""
        print(f"[TEST] Testing compilation of {service_name}")
        try:
            old_cwd = self.command_runner.cwd
            self.command_runner.cwd = service_dir
            try:
                self.command_runner.run(['go', 'build', './...'])
            finally:
                self.command_runner.cwd = old_cwd
        except Exception as e:
            raise RuntimeError(f"Compilation failed: {e}")
