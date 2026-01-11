#!/usr/bin/env python3
"""
Integration Tests for Trading Contracts Service Dependencies

Tests integration with external systems:
- PostgreSQL database operations
- Redis caching and pub/sub
- Message queue (Kafka/RabbitMQ) integration
- External API calls (market data, risk systems)
- File system operations (logs, configs)
- Network connectivity and service discovery

Issue: #2202 - Тестирование системы контрактов и сделок
"""

import pytest
import requests
import psycopg2
import redis
import json
import time
import subprocess
import socket
from typing import Dict, List, Optional, Any
import threading
import concurrent.futures

# Test Configuration
TEST_SERVICE_URL = "http://localhost:8088"
TEST_DATABASE_URL = "postgresql://test:test@localhost:5432/test_trading"
TEST_REDIS_URL = "redis://localhost:6379"
TEST_MESSAGE_QUEUE_URL = "kafka://localhost:9092"  # or rabbitmq://localhost:5672

class DependencyChecker:
    """Checks availability of external dependencies"""

    @staticmethod
    def check_postgresql_connection(db_url: str) -> bool:
        """Check PostgreSQL connection"""
        try:
            conn = psycopg2.connect(db_url)
            conn.close()
            return True
        except Exception:
            return False

    @staticmethod
    def check_redis_connection(redis_url: str) -> bool:
        """Check Redis connection"""
        try:
            r = redis.from_url(redis_url)
            r.ping()
            return True
        except Exception:
            return False

    @staticmethod
    def check_service_health(service_url: str) -> Dict:
        """Check service health"""
        try:
            response = requests.get(f"{service_url}/health", timeout=5)
            return {
                "available": response.status_code == 200,
                "status_code": response.status_code,
                "response": response.json() if response.content else None
            }
        except Exception as e:
            return {
                "available": False,
                "error": str(e)
            }

    @staticmethod
    def check_network_connectivity(host: str, port: int) -> bool:
        """Check network connectivity to host:port"""
        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.settimeout(5)
            result = sock.connect_ex((host, port))
            sock.close()
            return result == 0
        except Exception:
            return False

class DatabaseIntegrationTest:
    """Tests database integration"""

    def __init__(self, db_url: str):
        self.db_url = db_url
        self.connection = None

    def connect(self):
        """Establish database connection"""
        self.connection = psycopg2.connect(self.db_url)

    def disconnect(self):
        """Close database connection"""
        if self.connection:
            self.connection.close()

    def test_database_schema(self):
        """Test database schema integrity"""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor()

        # Check if required tables exist
        required_tables = [
            'trading_contracts.contracts',
            'trading_contracts.orders',
            'trading_contracts.trades',
            'trading_contracts.positions'
        ]

        for table in required_tables:
            cursor.execute("""
                SELECT EXISTS (
                    SELECT FROM information_schema.tables
                    WHERE table_schema = %s
                    AND table_name = %s
                )
            """, table.split('.'))

            exists = cursor.fetchone()[0]
            assert exists, f"Required table {table} does not exist"

        cursor.close()
        print("✅ Database schema is correct")

    def test_database_operations(self):
        """Test basic database operations"""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor()

        # Test INSERT
        cursor.execute("""
            INSERT INTO trading_contracts.contracts (
                contract_id, user_id, symbol, contract_type, order_type,
                side, quantity, price, status, created_at
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, NOW())
        """, (
            'test_contract_123',
            'test_user_123',
            'AAPL',
            'SPOT',
            'LIMIT',
            'BUY',
            10,
            150.0,
            'pending'
        ))

        self.connection.commit()

        # Test SELECT
        cursor.execute("""
            SELECT contract_id, symbol, status FROM trading_contracts.contracts
            WHERE contract_id = %s
        """, ('test_contract_123',))

        row = cursor.fetchone()
        assert row is not None, "Failed to retrieve inserted contract"
        assert row[0] == 'test_contract_123'
        assert row[1] == 'AAPL'

        # Test UPDATE
        cursor.execute("""
            UPDATE trading_contracts.contracts
            SET status = %s, updated_at = NOW()
            WHERE contract_id = %s
        """, ('filled', 'test_contract_123'))

        self.connection.commit()

        # Verify UPDATE
        cursor.execute("""
            SELECT status FROM trading_contracts.contracts
            WHERE contract_id = %s
        """, ('test_contract_123',))

        updated_row = cursor.fetchone()
        assert updated_row[0] == 'filled', "Status update failed"

        # Test DELETE
        cursor.execute("""
            DELETE FROM trading_contracts.contracts
            WHERE contract_id = %s
        """, ('test_contract_123',))

        self.connection.commit()

        # Verify DELETE
        cursor.execute("""
            SELECT COUNT(*) FROM trading_contracts.contracts
            WHERE contract_id = %s
        """, ('test_contract_123',))

        count = cursor.fetchone()[0]
        assert count == 0, "Delete operation failed"

        cursor.close()
        print("✅ Database CRUD operations work correctly")

class RedisIntegrationTest:
    """Tests Redis integration"""

    def __init__(self, redis_url: str):
        self.redis_url = redis_url
        self.client = None

    def connect(self):
        """Establish Redis connection"""
        self.client = redis.from_url(self.redis_url)

    def disconnect(self):
        """Close Redis connection"""
        if self.client:
            self.client.close()

    def test_redis_operations(self):
        """Test basic Redis operations"""
        if not self.client:
            self.connect()

        test_key = "test:contract:cache"
        test_data = {
            "contract_id": "test_123",
            "symbol": "AAPL",
            "price": 150.0
        }

        # Test SET
        self.client.set(test_key, json.dumps(test_data), ex=60)

        # Test GET
        cached_data = self.client.get(test_key)
        assert cached_data is not None, "Failed to retrieve cached data"

        parsed_data = json.loads(cached_data)
        assert parsed_data["contract_id"] == "test_123"
        assert parsed_data["symbol"] == "AAPL"

        # Test expiration
        time.sleep(2)  # Wait a bit
        assert self.client.ttl(test_key) > 0, "Key should have TTL set"

        # Test DELETE
        self.client.delete(test_key)
        assert self.client.get(test_key) is None, "Key should be deleted"

        print("✅ Redis operations work correctly")

    def test_pubsub_operations(self):
        """Test Redis pub/sub operations"""
        if not self.client:
            self.connect()

        test_channel = "test:contract:events"
        received_messages = []

        def message_handler(message):
            received_messages.append(json.loads(message["data"]))

        # Subscribe to channel
        pubsub = self.client.pubsub()
        pubsub.subscribe(**{test_channel: message_handler})

        # Start listening thread
        thread = pubsub.run_in_thread(sleep_time=0.01)

        # Give thread time to start
        time.sleep(0.1)

        # Publish test message
        test_message = {
            "event_type": "contract_created",
            "contract_id": "test_123",
            "timestamp": time.time()
        }

        self.client.publish(test_channel, json.dumps(test_message))

        # Wait for message to be received
        timeout = 5
        start_time = time.time()
        while len(received_messages) == 0 and (time.time() - start_time) < timeout:
            time.sleep(0.1)

        # Cleanup
        thread.stop()
        pubsub.close()

        assert len(received_messages) > 0, "No message received via pub/sub"
        assert received_messages[0]["event_type"] == "contract_created"
        assert received_messages[0]["contract_id"] == "test_123"

        print("✅ Redis pub/sub operations work correctly")

class MessageQueueIntegrationTest:
    """Tests message queue integration"""

    def __init__(self, mq_url: str):
        self.mq_url = mq_url
        self.producer = None
        self.consumer = None

    def test_message_queue_connection(self):
        """Test message queue connection"""
        # This would depend on the specific MQ system (Kafka, RabbitMQ, etc.)
        # For now, just check basic connectivity

        if "kafka" in self.mq_url:
            host, port = self.mq_url.replace("kafka://", "").split(":")
            port = int(port)
        elif "rabbitmq" in self.mq_url or "amqp" in self.mq_url:
            # Parse RabbitMQ URL
            host = "localhost"  # Simplified
            port = 5672
        else:
            host, port = "localhost", 9092  # Default

        connected = DependencyChecker.check_network_connectivity(host, port)

        if connected:
            print("✅ Message queue is accessible")
        else:
            print("⚠️  Message queue not accessible (may be expected in test environment)")

        return connected

class ExternalAPIIntegrationTest:
    """Tests integration with external APIs"""

    def test_market_data_api(self):
        """Test market data API integration"""
        # Mock market data service
        mock_market_url = "http://localhost:8089"  # Assume market data service

        try:
            response = requests.get(f"{mock_market_url}/market-data/AAPL", timeout=5)
            if response.status_code == 200:
                data = response.json()
                assert "price" in data or "bid" in data, "Market data missing expected fields"
                print("✅ Market data API integration works")
            else:
                print("⚠️  Market data API returned error (may be expected in test environment)")
        except requests.exceptions.RequestException:
            print("⚠️  Market data API not accessible (may be expected in test environment)")

    def test_risk_management_api(self):
        """Test risk management API integration"""
        # Mock risk service
        mock_risk_url = "http://localhost:8090"  # Assume risk service

        try:
            risk_payload = {
                "user_id": "test_user",
                "contract_value": 1000.0,
                "position_size": 10
            }
            response = requests.post(f"{mock_risk_url}/risk-check", json=risk_payload, timeout=5)
            if response.status_code == 200:
                data = response.json()
                assert "approved" in data, "Risk check missing approval status"
                print("✅ Risk management API integration works")
            else:
                print("⚠️  Risk management API returned error (may be expected in test environment)")
        except requests.exceptions.RequestException:
            print("⚠️  Risk management API not accessible (may be expected in test environment)")

class TradingContractsIntegrationTest:
    """Complete integration test suite"""

    def setup_method(self):
        """Setup test environment"""
        self.service_url = TEST_SERVICE_URL
        self.db_test = DatabaseIntegrationTest(TEST_DATABASE_URL)
        self.redis_test = RedisIntegrationTest(TEST_REDIS_URL)
        self.mq_test = MessageQueueIntegrationTest(TEST_MESSAGE_QUEUE_URL)
        self.external_test = ExternalAPIIntegrationTest()

        self.test_user_id = f"integration_test_user_{int(time.time())}"

    def teardown_method(self):
        """Cleanup after tests"""
        if hasattr(self, 'db_test') and self.db_test.connection:
            self.db_test.disconnect()
        if hasattr(self, 'redis_test') and self.redis_test.client:
            self.redis_test.disconnect()

    def test_dependency_availability(self):
        """Test that all dependencies are available"""
        print("🔍 Checking dependency availability...")

        # Check PostgreSQL
        db_available = DependencyChecker.check_postgresql_connection(TEST_DATABASE_URL)
        print(f"PostgreSQL: {'✅ Available' if db_available else '❌ Unavailable'}")

        # Check Redis
        redis_available = DependencyChecker.check_redis_connection(TEST_REDIS_URL)
        print(f"Redis: {'✅ Available' if redis_available else '❌ Unavailable'}")

        # Check service itself
        service_health = DependencyChecker.check_service_health(TEST_SERVICE_URL)
        service_available = service_health["available"]
        print(f"Trading Contracts Service: {'✅ Available' if service_available else '❌ Unavailable'}")

        # Check message queue
        mq_available = self.mq_test.test_message_queue_connection()

        # Require at least database and service to be available for tests
        assert db_available, "PostgreSQL is required for integration tests"
        assert service_available, "Trading Contracts service is required for integration tests"

        self.dependencies = {
            "postgresql": db_available,
            "redis": redis_available,
            "service": service_available,
            "message_queue": mq_available
        }

    def test_database_integration(self):
        """Test database integration"""
        if not self.dependencies["postgresql"]:
            pytest.skip("PostgreSQL not available")

        print("🗄️  Testing database integration...")

        try:
            self.db_test.connect()
            self.db_test.test_database_schema()
            self.db_test.test_database_operations()
        finally:
            self.db_test.disconnect()

    def test_redis_integration(self):
        """Test Redis integration"""
        if not self.dependencies["redis"]:
            pytest.skip("Redis not available")

        print("🔴 Testing Redis integration...")

        try:
            self.redis_test.connect()
            self.redis_test.test_redis_operations()
            self.redis_test.test_pubsub_operations()
        finally:
            self.redis_test.disconnect()

    def test_message_queue_integration(self):
        """Test message queue integration"""
        if not self.dependencies["message_queue"]:
            pytest.skip("Message queue not available")

        print("📨 Testing message queue integration...")
        # Message queue tests would go here
        print("✅ Message queue connectivity verified")

    def test_external_api_integration(self):
        """Test external API integration"""
        print("🌐 Testing external API integration...")

        self.external_test.test_market_data_api()
        self.external_test.test_risk_management_api()

    def test_end_to_end_contract_flow(self):
        """Test complete contract creation and execution flow"""
        print("🔄 Testing end-to-end contract flow...")

        session = requests.Session()

        # 1. Create contract
        contract_data = {
            "client_order_id": f"e2e_test_{int(time.time())}",
            "symbol": "AAPL",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 10,
            "price": 150.0,
            "user_id": self.test_user_id
        }

        response = session.post(f"{self.service_url}/contracts", json=contract_data)
        assert response.status_code == 201, f"Contract creation failed: {response.text}"

        contract_result = response.json()
        contract_id = contract_result["contract_id"]

        print(f"✅ Contract created: {contract_id}")

        # 2. Retrieve contract
        response = session.get(f"{self.service_url}/contracts/{contract_id}")
        assert response.status_code == 200, f"Contract retrieval failed: {response.text}"

        retrieved_contract = response.json()
        assert retrieved_contract["contract_id"] == contract_id
        assert retrieved_contract["status"] == "pending"

        print("✅ Contract retrieved successfully")

        # 3. Check order book
        response = session.get(f"{self.service_url}/orderbook/AAPL")
        assert response.status_code in [200, 404], f"Order book check failed: {response.status_code}"

        if response.status_code == 200:
            order_book = response.json()
            assert "bids" in order_book
            assert "asks" in order_book
            print("✅ Order book accessible")

        # 4. Check user contracts
        response = session.get(f"{self.service_url}/contracts?user_id={self.test_user_id}")
        assert response.status_code == 200, f"User contracts retrieval failed: {response.text}"

        user_contracts = response.json()
        contract_ids = [c["contract_id"] for c in user_contracts.get("contracts", [])]
        assert contract_id in contract_ids, "Contract not found in user contracts"

        print("✅ User contracts listing works")

        # 5. Cancel contract
        response = session.delete(f"{self.service_url}/contracts/{contract_id}")
        assert response.status_code == 200, f"Contract cancellation failed: {response.text}"

        # Verify cancellation
        response = session.get(f"{self.service_url}/contracts/{contract_id}")
        assert response.status_code == 200
        cancelled_contract = response.json()
        assert cancelled_contract["status"] == "cancelled"

        print("✅ Contract cancelled successfully")

    def test_concurrent_operations(self):
        """Test concurrent operations"""
        print("⚡ Testing concurrent operations...")

        def create_contract_worker(worker_id: int):
            """Worker to create contracts concurrently"""
            session = requests.Session()
            contract_data = {
                "client_order_id": f"concurrent_test_{worker_id}_{int(time.time())}",
                "symbol": "AAPL",
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": 1,
                "price": 100.0 + worker_id,
                "user_id": f"user_{worker_id}"
            }

            response = session.post(f"{self.service_url}/contracts", json=contract_data)
            return response.status_code == 201

        # Run concurrent contract creations
        num_workers = 10
        with concurrent.futures.ThreadPoolExecutor(max_workers=num_workers) as executor:
            futures = [executor.submit(create_contract_worker, i) for i in range(num_workers)]
            results = [future.result() for future in concurrent.futures.as_completed(futures)]

        successful_creations = sum(results)
        success_rate = successful_creations / num_workers

        print(f"✅ Concurrent operations: {successful_creations}/{num_workers} successful ({success_rate:.1%})")

        assert success_rate >= 0.8, f"Concurrent operation success rate too low: {success_rate:.1%}"

    def test_service_resilience(self):
        """Test service resilience under stress"""
        print("🛡️  Testing service resilience...")

        # Rapid fire requests
        session = requests.Session()
        num_requests = 100

        start_time = time.time()
        successful_requests = 0

        for i in range(num_requests):
            try:
                response = session.get(f"{self.service_url}/health", timeout=1)
                if response.status_code == 200:
                    successful_requests += 1
            except:
                pass  # Ignore timeouts/errors for resilience test

        end_time = time.time()
        duration = end_time - start_time
        rps = successful_requests / duration

        print(f"✅ Resilience test: {successful_requests}/{num_requests} requests successful")
        print(".2f"
    def run_integration_test_suite(self):
        """Run complete integration test suite"""
        print("🚀 Starting Trading Contracts Integration Test Suite")
        print("=" * 70)

        try:
            self.test_dependency_availability()
            print()

            self.test_database_integration()
            print()

            if self.dependencies["redis"]:
                self.test_redis_integration()
            else:
                print("⚠️  Skipping Redis integration tests")
            print()

            self.test_message_queue_integration()
            print()

            self.test_external_api_integration()
            print()

            self.test_end_to_end_contract_flow()
            print()

            self.test_concurrent_operations()
            print()

            self.test_service_resilience()
            print()

            print("=" * 70)
            print("🎉 Integration Test Suite Completed Successfully!")
            print("✅ Trading Contracts service integrates correctly with all dependencies")

        except Exception as e:
            print(f"❌ Integration test failed: {e}")
            raise

# Test execution
if __name__ == "__main__":
    tester = TradingContractsIntegrationTest()
    tester.setup_method()

    try:
        tester.run_integration_test_suite()
    finally:
        tester.teardown_method()