#!/usr/bin/env python3
"""
Apply enhanced error handling and logging system to all Go services.

This script automatically updates all Go microservices to use the new
error handling, logging, and response systems.
"""

import os
import re
import glob
from pathlib import Path

class ServiceUpdater:
    def __init__(self, base_path: str):
        self.base_path = Path(base_path)
        self.error_handling_import = 'github.com/your-org/necpgame/scripts/core/error-handling'

    def find_go_services(self):
        """Find all Go service directories"""
        services = []
        for service_dir in self.base_path.glob("services/*-go"):
            if service_dir.is_dir() and (service_dir / "main.go").exists():
                services.append(service_dir)
        return services

    def update_main_go(self, service_dir: Path):
        """Update main.go file to use enhanced error handling"""
        main_file = service_dir / "main.go"

        with open(main_file, 'r') as f:
            content = f.read()

        # Add import
        import_pattern = r'(import\s*\()'
        import_replacement = rf'\1{self.error_handling_import}'

        if 'error-handling' not in content:
            content = re.sub(import_pattern, import_replacement, content)

        # Update logger initialization
        logger_init_pattern = r'(\s*)logger,\s*err\s*:=\s*zap\.NewProduction\(\)'
        logger_init_replacement = rf'''\1\t// Initialize enhanced structured logger
\1\tloggerConfig := &errorhandling.LoggerConfig{{
\1\t\t\tServiceName: "{service_dir.name}",
\1\t\t\tLevel:       zap.InfoLevel,
\1\t\t\tDevelopment: os.Getenv("ENV") == "development",
\1\t\t\tAddCaller:   true,
\1\t\t}}

\1\tlogger, err := errorhandling.NewLogger(loggerConfig)'''

        content = re.sub(logger_init_pattern, logger_init_replacement, content, flags=re.MULTILINE)

        # Update sugar logger removal
        content = re.sub(r'\s*sugar\s*:=\s*logger\.Sugar\(\)', '', content)

        # Update router setup
        router_pattern = r'setupRouter\([^)]+\)'
        router_replacement = r'setupRouter(\1, logger)'

        # This is complex, let's do it manually for now
        if 'setupRouter' in content:
            # Replace old middleware with new ones
            old_middleware = '''r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))'''

            new_middleware = '''r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(errorhandling.LoggingMiddleware(logger))
	r.Use(errorhandling.ErrorHandler(logger))
	r.Use(errorhandling.RecoveryMiddleware(logger))
	r.Use(errorhandling.TimeoutMiddleware(30 * time.Second))
	r.Use(errorhandling.RateLimitMiddleware(logger))'''

            content = content.replace(old_middleware, new_middleware)

        with open(main_file, 'w') as f:
            f.write(content)

        print(f"Updated {main_file}")

    def update_handlers(self, service_dir: Path):
        """Update handlers to use enhanced error handling"""
        handlers_dir = service_dir / "internal" / "handlers"
        if not handlers_dir.exists():
            return

        for handler_file in handlers_dir.glob("*.go"):
            self.update_handler_file(handler_file)

    def update_handler_file(self, handler_file: Path):
        """Update individual handler file"""
        with open(handler_file, 'r') as f:
            content = f.read()

        # Add import
        if 'error-handling' not in content:
            import_pattern = r'(import\s*\()'
            import_replacement = rf'\1{self.error_handling_import}'

            content = re.sub(import_pattern, import_replacement, content)

            # Remove old zap import if present
            content = re.sub(r'\s*"go\.uber\.org/zap"', '', content)

        # Update handler struct
        struct_pattern = r'type\s+(\w+Handlers)\s+struct\s*\{([^}]+)\}'
        struct_replacement = r'type \1 struct {\n\2\n\tlogger   *errorhandling.Logger\n\tresponder *errorhandling.Responder\n}'

        content = re.sub(struct_pattern, struct_replacement, content, flags=re.MULTILINE)

        # Update constructor
        constructor_pattern = r'func\s+New(\w+Handlers)\([^)]+\)\s*\*(\w+Handlers)\s*\{'
        constructor_replacement = r'func New\1(svc *service.\2Service, logger *errorhandling.Logger) *\2Handlers {\n\treturn &\2Handlers{\n\t\tservice:  svc,\n\t\tlogger:   logger,\n\t\tresponder: errorhandling.NewResponder(logger),\n\t}\n}'

        # This is too complex for regex, let's do simpler approach
        if 'New' in content and 'Handlers' in content:
            # Replace zap.SugaredLogger with errorhandling.Logger
            content = content.replace('*zap.SugaredLogger', '*errorhandling.Logger')

            # Add responder initialization
            if 'return &' in content:
                content = re.sub(r'(return\s*&[^}]+})', r'\1\n\t\tresponder: errorhandling.NewResponder(logger),', content)

        with open(handler_file, 'w') as f:
            f.write(content)

        print(f"Updated {handler_file}")

    def create_go_mod_update(self):
        """Create script to update go.mod files"""
        script_content = '''#!/bin/bash
# Update go.mod files to include error-handling module

SERVICES=$(find services -name "*-go" -type d)

for service in $SERVICES; do
    if [ -f "$service/go.mod" ]; then
        echo "Updating $service/go.mod"
        cd $service

        # Add replace directive for local module
        if ! grep -q "error-handling" go.mod; then
            echo "replace github.com/your-org/necpgame/scripts/core/error-handling => ../../scripts/core/error-handling" >> go.mod
        fi

        # Run go mod tidy
        go mod tidy

        cd - > /dev/null
    fi
done

echo "Go modules updated successfully"
'''

        script_path = self.base_path / "scripts" / "core" / "error-handling" / "update_go_modules.sh"
        with open(script_path, 'w') as f:
            f.write(script_content)

        # Make executable
        os.chmod(script_path, 0o755)

        print(f"Created {script_path}")

    def apply_to_all_services(self):
        """Apply updates to all services"""
        services = self.find_go_services()

        print(f"Found {len(services)} Go services")

        for service in services:
            print(f"Updating {service.name}...")
            try:
                self.update_main_go(service)
                self.update_handlers(service)
            except Exception as e:
                print(f"Error updating {service.name}: {e}")
                continue

        self.create_go_mod_update()

        print("\nAll services updated successfully!")
        print("Run the following commands to complete the update:")
        print("1. chmod +x scripts/core/error-handling/update_go_modules.sh")
        print("2. ./scripts/core/error-handling/update_go_modules.sh")
        print("3. Test compilation: go build ./services/*/main.go")

def main():
    updater = ServiceUpdater(".")
    updater.apply_to_all_services()

if __name__ == "__main__":
    main()
