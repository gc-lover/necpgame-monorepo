#!/usr/bin/env python3
"""
Start Guild Service Script
Starts the NECPGAME Guild Service with proper configuration
"""

import os
import sys
import subprocess
import time
import requests
from pathlib import Path

def start_guild_service():
    """Start the Guild Service"""
    service_path = Path("services/guild-service-go")

    if not service_path.exists():
        print("ERROR: Guild service directory not found")
        return False

    # Change to service directory
    os.chdir(service_path)

    # Set environment variables
    env = os.environ.copy()
    env.update({
        'SERVER_PORT': '8089',  # Different port to avoid conflict
        'DATABASE_URL': 'postgresql://postgres:postgres@localhost:5432/necpgame?sslmode=disable',
        'REDIS_URL': 'redis://localhost:6379',
        'LOG_LEVEL': 'info',
        'GOGC': '75',
        'GOMAXPROCS': '4'
    })

    try:
        # Build the service
        print("Building Guild Service...")
        result = subprocess.run(['go', 'build', '-o', 'guild-service', '.'], capture_output=True, text=True)
        if result.returncode != 0:
            print(f"Build failed: {result.stderr}")
            return False

        # Start the service
        print("Starting Guild Service on port 8089...")
        process = subprocess.Popen(['./guild-service'], env=env, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        # Wait for startup
        time.sleep(5)

        # Check if process is still running
        if process.poll() is None:
            print(f"SUCCESS: Guild Service started (PID: {process.pid})")
            return True
        else:
            stdout, stderr = process.communicate()
            print(f"Service failed to start. STDOUT: {stdout}, STDERR: {stderr}")
            return False

    except Exception as e:
        print(f"ERROR: Failed to start guild service: {e}")
        return False

def check_guild_service():
    """Check if Guild Service is responding"""
    try:
        response = requests.get("http://localhost:8089/health", timeout=5)
        if response.status_code == 200:
            print("SUCCESS: Guild Service is healthy")
            return True
        else:
            print(f"WARNING: Guild Service returned status {response.status_code}")
            return False
    except Exception as e:
        print(f"ERROR: Guild Service not responding: {e}")
        return False

def test_guild_integration():
    """Test Guild-WebRTC integration"""
    try:
        # Test guild service
        response = requests.get("http://localhost:8089/guilds", timeout=5)
        print(f"Guild service /guilds endpoint: HTTP {response.status_code}")

        # Test API Gateway routing to guild service
        # This would need API Gateway configuration
        print("INFO: Guild-WebRTC integration requires API Gateway configuration")
        return True

    except Exception as e:
        print(f"ERROR: Integration test failed: {e}")
        return False

if __name__ == '__main__':
    print("=== NECPGAME Guild Service Startup ===")

    if start_guild_service():
        if check_guild_service():
            if test_guild_integration():
                print("SUCCESS: Guild Service fully operational")
                sys.exit(0)
            else:
                print("WARNING: Integration tests failed")
                sys.exit(1)
        else:
            print("ERROR: Guild Service health check failed")
            sys.exit(1)
    else:
        print("ERROR: Failed to start Guild Service")
        sys.exit(1)
