#!/usr/bin/env python3
"""
Simple script to generate economy-domain Go service
"""

import sys
from pathlib import Path

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent
sys.path.insert(0, str(scripts_dir))

from core.command_runner import CommandRunner
from core.config import ConfigManager
from core.file_manager import FileManager
from core.logger import Logger
from openapi.openapi_analyzer import OpenAPIAnalyzer
from generation.enhanced_service_generator import EnhancedServiceGenerator

def main():
    """Generate economy-domain service"""
    # Initialize components
    config = ConfigManager()
    logger = Logger(config)
    command_runner = CommandRunner(logger.get_logger("CommandRunner"))
    file_manager = FileManager(logger.get_logger("FileManager"))

    # Initialize OpenAPI analyzer
    openapi_analyzer = OpenAPIAnalyzer(logger.get_logger("OpenAPIAnalyzer"))

    # Initialize service generator
    generator = EnhancedServiceGenerator(
        config, openapi_analyzer, file_manager, command_runner, logger.get_logger("EnhancedServiceGenerator")
    )

    # Load and analyze economy domain spec
    print("Analyzing economy-domain OpenAPI specification...")
    spec_path = Path("proto/openapi/economy-domain/main.yaml")
    if not spec_path.exists():
        raise FileNotFoundError(f"Spec file not found: {spec_path}")

    import yaml
    with open(spec_path, 'r', encoding='utf-8') as f:
        spec = yaml.safe_load(f)

    analysis = openapi_analyzer.analyze_spec(spec)

    print(f"Analysis complete. Found {len(analysis.endpoints)} endpoints.")

    # Generate service
    service_dir = Path("services/economy-domain-service-go")
    print(f"Generating service in {service_dir}...")

    generator.generate_complete_service(
        domain="economy",
        analysis=analysis,
        service_dir=service_dir,
        dry_run=False
    )

    print("Service generation complete!")

if __name__ == '__main__':
    main()
