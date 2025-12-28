#!/usr/bin/env python3
"""Test script for Cyberspace Easter Eggs API"""

import requests
import json

def test_api():
    """Test the easter eggs API endpoints"""
    print("Testing Cyberspace Easter Eggs API...")

    try:
        # Test health check
        print("\n1. Testing health check...")
        response = requests.get('http://localhost:8080/health', timeout=5)
        print(f"Health check: {response.status_code}")

        if response.status_code == 200:
            print("✓ Service is healthy")

            # Test easter eggs endpoint
            print("\n2. Testing easter eggs list...")
            response = requests.get('http://localhost:8080/api/v1/easter-eggs', timeout=5)
            print(f"Easter eggs endpoint: {response.status_code}")

            if response.status_code == 200:
                data = response.json()
                easter_eggs = data.get("easter_eggs", [])
                print(f"✓ Found {len(easter_eggs)} easter eggs in API")

                if easter_eggs:
                    # Test individual easter egg
                    first_egg = easter_eggs[0]
                    egg_id = first_egg.get("id")
                    print(f"\n3. Testing individual easter egg: {egg_id}")
                    response = requests.get(f'http://localhost:8080/api/v1/easter-eggs/{egg_id}', timeout=5)
                    print(f"Individual egg endpoint: {response.status_code}")

                    if response.status_code == 200:
                        print("✓ Individual easter egg retrieval works")
                    else:
                        print(f"✗ Individual egg failed: {response.text}")

                    # Test category endpoint
                    category = first_egg.get("category", "technology")
                    print(f"\n4. Testing category endpoint: {category}")
                    response = requests.get(f'http://localhost:8080/api/v1/easter-eggs/category/{category}', timeout=5)
                    print(f"Category endpoint: {response.status_code}")

                    if response.status_code == 200:
                        print("✓ Category filtering works")
                    else:
                        print(f"✗ Category endpoint failed: {response.text}")

                print("\n✓ API testing completed successfully")
                return True
            else:
                print(f"✗ Easter eggs endpoint failed: {response.text}")
                return False
        else:
            print(f"✗ Health check failed: {response.text}")
            return False

    except requests.exceptions.RequestException as e:
        print(f"✗ Connection failed: {e}")
        print("Service may not be running")
        return False

if __name__ == "__main__":
    test_api()
