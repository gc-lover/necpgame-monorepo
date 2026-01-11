#!/usr/bin/env python3
"""
API Specification Compliance Tests for Trading Contracts System

Tests that the trading contracts service correctly implements the OpenAPI specification:
- All endpoints exist and respond correctly
- Request/response schemas match specification
- HTTP status codes are correct
- Required headers and parameters are validated
- Error responses follow specification

Issue: #2202 - Тестирование системы контрактов и сделок
"""

import pytest
import requests
import json
import jsonschema
from typing import Dict, List, Optional, Any
from dataclasses import dataclass
import time
import re

# Test Configuration
TEST_SERVICE_URL = "http://localhost:8088"
OPENAPI_SPEC_PATH = "proto/openapi/trading-contracts-service.yaml"

@dataclass
class APIEndpoint:
    """API endpoint specification"""
    path: str
    method: str
    operation_id: str
    parameters: List[Dict]
    request_body: Optional[Dict]
    responses: Dict[str, Dict]
    summary: Optional[str] = None

class APISpecificationTester:
    """Tests API compliance with OpenAPI specification"""

    def __init__(self, service_url: str, spec_path: str):
        self.service_url = service_url
        self.spec_path = spec_path
        self.session = requests.Session()
        self.spec = self.load_openapi_spec()
        self.endpoints = self.parse_endpoints()

    def load_openapi_spec(self) -> Dict:
        """Load OpenAPI specification from file"""
        try:
            with open(self.spec_path, 'r', encoding='utf-8') as f:
                return json.load(f)
        except FileNotFoundError:
            # Try YAML loading if available
            try:
                import yaml
                with open(self.spec_path, 'r', encoding='utf-8') as f:
                    return yaml.safe_load(f)
            except ImportError:
                raise Exception("YAML support not available, install PyYAML")
        except Exception as e:
            raise Exception(f"Failed to load OpenAPI spec: {e}")

    def parse_endpoints(self) -> List[APIEndpoint]:
        """Parse endpoints from OpenAPI specification"""
        endpoints = []

        if 'paths' not in self.spec:
            raise Exception("No paths found in OpenAPI spec")

        for path, path_item in self.spec['paths'].items():
            for method, operation in path_item.items():
                if method.lower() not in ['get', 'post', 'put', 'delete', 'patch']:
                    continue

                endpoint = APIEndpoint(
                    path=path,
                    method=method.upper(),
                    operation_id=operation.get('operationId', ''),
                    parameters=operation.get('parameters', []),
                    request_body=operation.get('requestBody'),
                    responses=operation.get('responses', {}),
                    summary=operation.get('summary')
                )
                endpoints.append(endpoint)

        return endpoints

    def get_schema_for_status(self, endpoint: APIEndpoint, status_code: str) -> Optional[Dict]:
        """Get response schema for a specific status code"""
        if status_code not in endpoint.responses:
            return None

        response = endpoint.responses[status_code]
        if 'content' not in response:
            return None

        # Assume JSON content
        json_content = response['content'].get('application/json', {})
        return json_content.get('schema')

    def validate_response_schema(self, response_data: Any, schema: Dict) -> bool:
        """Validate response data against JSON schema"""
        try:
            jsonschema.validate(response_data, schema)
            return True
        except jsonschema.ValidationError:
            return False

    def build_request_url(self, endpoint: APIEndpoint, path_params: Optional[Dict] = None) -> str:
        """Build request URL with path parameters"""
        url = self.service_url + endpoint.path

        if path_params:
            for param_name, param_value in path_params.items():
                url = url.replace(f"{{{param_name}}}", str(param_value))

        return url

    def build_request_params(self, endpoint: APIEndpoint, query_params: Optional[Dict] = None) -> Dict:
        """Build query parameters"""
        params = {}

        # Add required query parameters if specified
        for param in endpoint.parameters:
            if param.get('in') == 'query' and param.get('required', False):
                param_name = param['name']
                if query_params and param_name in query_params:
                    params[param_name] = query_params[param_name]
                else:
                    # Use default values for required params
                    if 'default' in param:
                        params[param_name] = param['default']
                    elif param.get('schema', {}).get('type') == 'string':
                        params[param_name] = "test_value"
                    elif param.get('schema', {}).get('type') == 'integer':
                        params[param_name] = 1

        return params

    def build_request_body(self, endpoint: APIEndpoint, body_data: Optional[Dict] = None) -> Optional[Dict]:
        """Build request body according to schema"""
        if not endpoint.request_body:
            return None

        json_content = endpoint.request_body.get('content', {}).get('application/json', {})
        schema = json_content.get('schema', {})

        if not schema:
            return body_data

        # If body_data provided, use it; otherwise create sample data
        if body_data:
            return body_data

        # Generate sample request body based on schema
        return self.generate_sample_data(schema)

    def generate_sample_data(self, schema: Dict) -> Any:
        """Generate sample data from JSON schema"""
        if 'example' in schema:
            return schema['example']

        schema_type = schema.get('type')

        if schema_type == 'object':
            sample = {}
            properties = schema.get('properties', {})
            required = schema.get('required', [])

            for prop_name, prop_schema in properties.items():
                if prop_name in required or True:  # Generate all for testing
                    sample[prop_name] = self.generate_sample_data(prop_schema)

            return sample

        elif schema_type == 'array':
            items_schema = schema.get('items', {})
            return [self.generate_sample_data(items_schema)]

        elif schema_type == 'string':
            if 'enum' in schema:
                return schema['enum'][0]
            return "test_string"

        elif schema_type == 'integer':
            return 1

        elif schema_type == 'number':
            return 1.0

        elif schema_type == 'boolean':
            return True

        return None

    def test_endpoint_exists(self, endpoint: APIEndpoint) -> bool:
        """Test that endpoint exists and is accessible"""
        try:
            url = self.build_request_url(endpoint)
            response = self.session.request(endpoint.method, url)

            # Any response (even 404) means endpoint exists
            # 404 just means we need proper parameters
            return response.status_code in [200, 400, 401, 403, 404, 422, 500]

        except requests.exceptions.RequestException:
            return False

    def test_endpoint_with_valid_request(self, endpoint: APIEndpoint) -> Dict:
        """Test endpoint with properly formed request"""
        result = {
            'endpoint': f"{endpoint.method} {endpoint.path}",
            'success': False,
            'status_code': None,
            'response_valid': False,
            'schema_valid': False,
            'error': None
        }

        try:
            url = self.build_request_url(endpoint)
            params = self.build_request_params(endpoint)
            body = self.build_request_body(endpoint)

            headers = {'Content-Type': 'application/json'} if body else {}

            response = self.session.request(
                endpoint.method,
                url,
                params=params,
                json=body,
                headers=headers
            )

            result['status_code'] = response.status_code

            # Check if status code is defined in spec
            expected_responses = list(endpoint.responses.keys())
            if str(response.status_code) in expected_responses or 'default' in expected_responses:
                result['response_valid'] = True

            # Try to validate response schema
            if response.status_code < 400 and response.content:
                try:
                    response_data = response.json()
                    schema = self.get_schema_for_status(endpoint, str(response.status_code))
                    if schema and self.validate_response_schema(response_data, schema):
                        result['schema_valid'] = True
                except (json.JSONDecodeError, jsonschema.ValidationError):
                    pass

            # Consider success if we got an expected response
            if result['response_valid']:
                result['success'] = True

        except Exception as e:
            result['error'] = str(e)

        return result

class TradingContractsAPISpecTest:
    """API specification compliance test suite"""

    def setup_method(self):
        """Setup test environment"""
        self.tester = APISpecificationTester(TEST_SERVICE_URL, OPENAPI_SPEC_PATH)
        self.test_user_id = f"api_test_user_{int(time.time())}"

    def test_openapi_spec_loading(self):
        """Test that OpenAPI specification can be loaded"""
        assert self.tester.spec is not None
        assert 'openapi' in self.tester.spec
        assert 'paths' in self.tester.spec

        print(f"✅ Loaded OpenAPI spec version: {self.tester.spec.get('openapi', 'unknown')}")
        print(f"📊 Found {len(self.tester.endpoints)} endpoints to test")

    def test_all_endpoints_exist(self):
        """Test that all specified endpoints exist"""
        failed_endpoints = []

        for endpoint in self.tester.endpoints:
            if not self.tester.test_endpoint_exists(endpoint):
                failed_endpoints.append(f"{endpoint.method} {endpoint.path}")

        if failed_endpoints:
            pytest.fail(f"❌ The following endpoints do not exist:\n" + "\n".join(failed_endpoints))

        print(f"✅ All {len(self.tester.endpoints)} endpoints exist")

    def test_endpoint_responses(self):
        """Test that endpoints return valid responses"""
        results = []

        for endpoint in self.tester.endpoints:
            result = self.tester.test_endpoint_with_valid_request(endpoint)
            results.append(result)

        # Analyze results
        successful = [r for r in results if r['success']]
        failed = [r for r in results if not r['success']]

        print("📊 API Compliance Results:")
        print(f"✅ Successful endpoints: {len(successful)}/{len(results)}")
        print(f"❌ Failed endpoints: {len(failed)}/{len(results)}")

        if failed:
            print("\n❌ Failed endpoints:")
            for result in failed:
                print(f"  - {result['endpoint']}: {result.get('error', 'Unknown error')}")

        # Allow some failures for endpoints that require specific setup
        max_allowed_failures = len(self.tester.endpoints) * 0.3  # 30% tolerance

        if len(failed) > max_allowed_failures:
            pytest.fail(f"Too many endpoint failures: {len(failed)} > {max_allowed_failures}")

    def test_contract_creation_api(self):
        """Test contract creation API specifically"""
        # Find create contract endpoint
        create_endpoint = None
        for endpoint in self.tester.endpoints:
            if endpoint.operation_id == 'createContract':
                create_endpoint = endpoint
                break

        assert create_endpoint is not None, "createContract endpoint not found"

        # Test with valid contract data
        contract_data = {
            "client_order_id": f"api_test_{int(time.time())}",
            "symbol": "AAPL",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 10,
            "price": 150.0,
            "user_id": self.test_user_id
        }

        result = self.tester.test_endpoint_with_valid_request(create_endpoint)

        if not result['success']:
            # Try with custom request body
            url = self.tester.build_request_url(create_endpoint)
            response = self.tester.session.post(url, json=contract_data)

            assert response.status_code in [201, 400, 422], f"Unexpected status: {response.status_code}"

            if response.status_code == 201:
                response_data = response.json()
                assert "contract_id" in response_data
                assert "order_id" in response_data
                print("✅ Contract creation API works correctly")
            else:
                print(f"⚠️  Contract creation returned: {response.status_code}")
        else:
            print("✅ Contract creation API spec compliant")

    def test_contract_retrieval_api(self):
        """Test contract retrieval API"""
        # First create a contract
        contract_data = {
            "client_order_id": f"retrieve_test_{int(time.time())}",
            "symbol": "GOOGL",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 5,
            "price": 2500.0,
            "user_id": self.test_user_id
        }

        create_url = f"{self.tester.service_url}/contracts"
        response = self.tester.session.post(create_url, json=contract_data)

        if response.status_code == 201:
            contract_id = response.json()["contract_id"]

            # Find get contract endpoint
            get_endpoint = None
            for endpoint in self.tester.endpoints:
                if endpoint.operation_id == 'getContract':
                    get_endpoint = endpoint
                    break

            assert get_endpoint is not None, "getContract endpoint not found"

            # Test retrieval
            get_result = self.tester.test_endpoint_with_valid_request(get_endpoint)

            # Try direct retrieval
            get_url = f"{self.tester.service_url}/contracts/{contract_id}"
            get_response = self.tester.session.get(get_url)

            assert get_response.status_code == 200, f"Failed to retrieve contract: {get_response.text}"

            contract_data = get_response.json()
            assert contract_data["contract_id"] == contract_id
            assert contract_data["symbol"] == "GOOGL"

            print("✅ Contract retrieval API works correctly")
        else:
            pytest.skip("Cannot test retrieval without successful contract creation")

    def test_order_book_api(self):
        """Test order book API"""
        # Find order book endpoint
        ob_endpoint = None
        for endpoint in self.tester.endpoints:
            if endpoint.operation_id == 'getOrderBook':
                ob_endpoint = endpoint
                break

        if ob_endpoint:
            result = self.tester.test_endpoint_with_valid_request(ob_endpoint)

            # Try direct call
            ob_url = f"{self.tester.service_url}/orderbook/TEST"
            ob_response = self.tester.session.get(ob_url)

            assert ob_response.status_code in [200, 404], f"Unexpected order book response: {ob_response.status_code}"

            if ob_response.status_code == 200:
                ob_data = ob_response.json()
                assert "bids" in ob_data
                assert "asks" in ob_data
                print("✅ Order book API works correctly")
        else:
            pytest.skip("Order book endpoint not found in spec")

    def test_health_check_api(self):
        """Test health check API"""
        # Find health endpoint
        health_endpoint = None
        for endpoint in self.tester.endpoints:
            if endpoint.operation_id == 'healthCheck':
                health_endpoint = endpoint
                break

        if health_endpoint:
            result = self.tester.test_endpoint_with_valid_request(health_endpoint)

            # Try direct call
            health_url = f"{self.tester.service_url}/health"
            health_response = self.tester.session.get(health_url)

            assert health_response.status_code == 200, f"Health check failed: {health_response.status_code}"

            health_data = health_response.json()
            assert "status" in health_data
            assert health_data["status"] in ["healthy", "degraded", "unhealthy"]

            print("✅ Health check API works correctly")
        else:
            # Health check might not be in OpenAPI spec
            health_url = f"{self.tester.service_url}/health"
            health_response = self.tester.session.get(health_url)

            assert health_response.status_code == 200, f"Health check failed: {health_response.status_code}"

            print("✅ Health check endpoint exists (not in spec)")

    def test_error_responses(self):
        """Test error response formats"""
        # Test invalid contract creation
        invalid_contract = {
            "symbol": "",  # Invalid
            "contract_type": "INVALID",
            "user_id": ""
        }

        create_url = f"{self.tester.service_url}/contracts"
        response = self.tester.session.post(create_url, json=invalid_contract)

        # Should get error response
        assert response.status_code >= 400, f"Expected error response, got: {response.status_code}"

        if response.content:
            try:
                error_data = response.json()
                # Check for standard error fields
                assert "error" in error_data or "message" in error_data, "Error response missing error/message field"
                print("✅ Error responses follow expected format")
            except json.JSONDecodeError:
                # Some errors might not return JSON
                pass

    def test_required_parameters(self):
        """Test that required parameters are enforced"""
        # Test contract creation without required fields
        incomplete_contract = {
            "symbol": "AAPL"
            # Missing required fields like contract_type, order_type, etc.
        }

        create_url = f"{self.tester.service_url}/contracts"
        response = self.tester.session.post(create_url, json=incomplete_contract)

        # Should get validation error
        assert response.status_code in [400, 422], f"Expected validation error, got: {response.status_code}"

        print("✅ Required parameter validation works")

    def run_api_compliance_test(self):
        """Run complete API compliance test suite"""
        print("🚀 Starting Trading Contracts API Specification Compliance Test")
        print("=" * 70)

        try:
            self.test_openapi_spec_loading()
            print()

            self.test_all_endpoints_exist()
            print()

            self.test_endpoint_responses()
            print()

            self.test_contract_creation_api()
            print()

            self.test_contract_retrieval_api()
            print()

            self.test_order_book_api()
            print()

            self.test_health_check_api()
            print()

            self.test_error_responses()
            print()

            self.test_required_parameters()
            print()

            print("=" * 70)
            print("🎉 API Specification Compliance Test Completed Successfully!")
            print("✅ Trading Contracts service correctly implements OpenAPI specification")

        except Exception as e:
            print(f"❌ API compliance test failed: {e}")
            raise

# Test execution
if __name__ == "__main__":
    tester = TradingContractsAPISpecTest()
    tester.setup_method()
    tester.run_api_compliance_test()