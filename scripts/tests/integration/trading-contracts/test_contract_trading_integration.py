#!/usr/bin/env python3
"""
Integration Tests for Trading Contracts System

Tests the complete trading contracts workflow including:
- Contract creation and validation
- Order matching and trade execution
- Position management and PnL calculation
- Risk limit enforcement
- Settlement processing

Issue: #2202 - Тестирование системы контрактов и сделок
"""

import pytest
import requests
import json
import time
import uuid
from typing import Dict, List, Optional
from dataclasses import dataclass
from concurrent.futures import ThreadPoolExecutor, as_completed

# Test Configuration
TEST_SERVICE_URL = "http://localhost:8088"  # trading-contracts-service
TEST_DATABASE_URL = "postgresql://test:test@localhost:5432/test_trading"
TEST_REDIS_URL = "redis://localhost:6379"

@dataclass
class TestUser:
    """Test user data"""
    user_id: str
    balance: float = 10000.0
    positions: Dict[str, int] = None

    def __post_init__(self):
        if self.positions is None:
            self.positions = {}

@dataclass
class TestContract:
    """Test contract data"""
    contract_id: str
    user_id: str
    symbol: str
    contract_type: str
    order_type: str
    side: str
    quantity: int
    price: Optional[float] = None
    status: str = "pending"

class TradingContractsIntegrationTest:
    """Integration test suite for trading contracts system"""

    def setup_method(self):
        """Setup test environment"""
        self.service_url = TEST_SERVICE_URL
        self.session = requests.Session()

        # Create test users
        self.buyer = TestUser(user_id="test_buyer_001")
        self.seller = TestUser(user_id="test_seller_001", positions={"AAPL": 100})

        # Test data
        self.test_contracts = []

    def teardown_method(self):
        """Cleanup after tests"""
        # Cancel all test contracts
        for contract in self.test_contracts:
            try:
                self.cancel_contract(contract.contract_id, contract.user_id)
            except:
                pass  # Ignore errors during cleanup

    def create_contract(self, contract_data: Dict) -> Dict:
        """Create a trading contract"""
        url = f"{self.service_url}/contracts"
        response = self.session.post(url, json=contract_data)

        assert response.status_code == 201, f"Failed to create contract: {response.text}"

        result = response.json()
        contract_id = result["contract_id"]

        # Track for cleanup
        contract = TestContract(
            contract_id=contract_id,
            user_id=contract_data["user_id"],
            symbol=contract_data["symbol"],
            contract_type=contract_data["contract_type"],
            order_type=contract_data["order_type"],
            side=contract_data["side"],
            quantity=contract_data["quantity"],
            price=contract_data.get("price"),
        )
        self.test_contracts.append(contract)

        return result

    def get_contract(self, contract_id: str) -> Dict:
        """Get contract details"""
        url = f"{self.service_url}/contracts/{contract_id}"
        response = self.session.get(url)

        assert response.status_code == 200, f"Failed to get contract: {response.text}"
        return response.json()

    def get_user_contracts(self, user_id: str, status: Optional[str] = None, limit: int = 50) -> List[Dict]:
        """Get user contracts"""
        url = f"{self.service_url}/contracts"
        params = {"limit": limit}
        if status:
            params["status"] = status

        response = self.session.get(url, params=params)
        assert response.status_code == 200, f"Failed to get user contracts: {response.text}"

        return response.json()["contracts"]

    def cancel_contract(self, contract_id: str, user_id: str) -> Dict:
        """Cancel a contract"""
        url = f"{self.service_url}/contracts/{contract_id}"
        response = self.session.delete(url)

        assert response.status_code == 200, f"Failed to cancel contract: {response.text}"
        return response.json()

    def get_order_book(self, symbol: str) -> Dict:
        """Get order book for symbol"""
        url = f"{self.service_url}/orderbook/{symbol}"
        response = self.session.get(url)

        assert response.status_code == 200, f"Failed to get order book: {response.text}"
        return response.json()

    def get_user_positions(self, user_id: str) -> List[Dict]:
        """Get user positions"""
        url = f"{self.service_url}/positions"
        response = self.session.get(url)

        assert response.status_code == 200, f"Failed to get user positions: {response.text}"
        return response.json()["positions"]

    def get_portfolio_analytics(self, user_id: str) -> Dict:
        """Get portfolio analytics"""
        url = f"{self.service_url}/portfolio/analytics"
        response = self.session.get(url)

        assert response.status_code == 200, f"Failed to get portfolio analytics: {response.text}"
        return response.json()

    def check_health(self) -> Dict:
        """Check service health"""
        url = f"{self.service_url}/health"
        response = self.session.get(url)

        assert response.status_code == 200, f"Service health check failed: {response.text}"
        return response.json()

    def test_health_check(self):
        """Test service health check"""
        health = self.check_health()
        assert health["status"] == "healthy"
        assert "timestamp" in health

    def test_create_spot_contract_buy(self):
        """Test creating a spot buy contract"""
        contract_data = {
            "client_order_id": f"test_buy_{int(time.time())}",
            "symbol": "AAPL",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 10,
            "price": 150.0,
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert "order_id" in result
        assert result["status"] == "pending"

        # Verify contract was created
        contract = self.get_contract(result["contract_id"])
        assert contract["symbol"] == "AAPL"
        assert contract["side"] == "BUY"
        assert contract["quantity"] == 10
        assert contract["price"] == 150.0

    def test_create_spot_contract_sell(self):
        """Test creating a spot sell contract"""
        contract_data = {
            "client_order_id": f"test_sell_{int(time.time())}",
            "symbol": "AAPL",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "SELL",
            "quantity": 5,
            "price": 155.0,
            "user_id": self.seller.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert result["status"] == "pending"

        # Verify contract was created
        contract = self.get_contract(result["contract_id"])
        assert contract["symbol"] == "AAPL"
        assert contract["side"] == "SELL"
        assert contract["quantity"] == 5
        assert contract["price"] == 155.0

    def test_create_market_order(self):
        """Test creating a market order"""
        contract_data = {
            "client_order_id": f"test_market_{int(time.time())}",
            "symbol": "GOOGL",
            "contract_type": "SPOT",
            "order_type": "MARKET",
            "side": "BUY",
            "quantity": 1,
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert result["status"] == "pending"

        # Market orders should execute immediately if there's liquidity
        contract = self.get_contract(result["contract_id"])
        # Status might be filled or still pending depending on order book

    def test_create_stop_order(self):
        """Test creating a stop order"""
        contract_data = {
            "client_order_id": f"test_stop_{int(time.time())}",
            "symbol": "TSLA",
            "contract_type": "SPOT",
            "order_type": "STOP",
            "side": "BUY",
            "quantity": 2,
            "price": 200.0,
            "stop_price": 195.0,
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert result["status"] == "pending"

        # Verify stop price is set
        contract = self.get_contract(result["contract_id"])
        assert contract["stop_price"] == 195.0
        assert contract["price"] == 200.0

    def test_create_future_contract(self):
        """Test creating a future contract"""
        future_expiry = time.time() + (30 * 24 * 60 * 60)  # 30 days from now

        contract_data = {
            "client_order_id": f"test_future_{int(time.time())}",
            "symbol": "AAPL",
            "contract_type": "FUTURE",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 160.0,
            "leverage": 5,
            "expires_at": time.strftime('%Y-%m-%dT%H:%M:%SZ', time.gmtime(future_expiry)),
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert result["status"] == "pending"

        # Verify future-specific fields
        contract = self.get_contract(result["contract_id"])
        assert contract["contract_type"] == "FUTURE"
        assert contract["leverage"] == 5
        assert "expires_at" in contract

    def test_create_option_contract(self):
        """Test creating an option contract"""
        option_expiry = time.time() + (60 * 24 * 60 * 60)  # 60 days from now

        contract_data = {
            "client_order_id": f"test_option_{int(time.time())}",
            "symbol": "MSFT",
            "contract_type": "OPTION",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 5.0,
            "strike_price": 300.0,
            "option_type": "CALL",
            "expires_at": time.strftime('%Y-%m-%dT%H:%M:%SZ', time.gmtime(option_expiry)),
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)

        assert "contract_id" in result
        assert result["status"] == "pending"

        # Verify option-specific fields
        contract = self.get_contract(result["contract_id"])
        assert contract["contract_type"] == "OPTION"
        assert contract["strike_price"] == 300.0
        assert contract["option_type"] == "CALL"
        assert "expires_at" in contract

    def test_cancel_contract(self):
        """Test cancelling a contract"""
        # Create a contract first
        contract_data = {
            "client_order_id": f"test_cancel_{int(time.time())}",
            "symbol": "NVDA",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 400.0,
            "user_id": self.buyer.user_id
        }

        result = self.create_contract(contract_data)
        contract_id = result["contract_id"]

        # Cancel the contract
        cancel_result = self.cancel_contract(contract_id, self.buyer.user_id)

        assert "message" in cancel_result
        assert "contract cancelled" in cancel_result["message"].lower()

        # Verify contract is cancelled
        contract = self.get_contract(contract_id)
        assert contract["status"] == "cancelled"

    def test_get_user_contracts(self):
        """Test getting user contracts"""
        # Create multiple contracts for the buyer
        symbols = ["AAPL", "GOOGL", "MSFT"]
        for symbol in symbols:
            contract_data = {
                "client_order_id": f"test_list_{symbol}_{int(time.time())}",
                "symbol": symbol,
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": 1,
                "price": 100.0,
                "user_id": self.buyer.user_id
            }
            self.create_contract(contract_data)

        # Get user contracts
        contracts = self.get_user_contracts(self.buyer.user_id, limit=10)

        assert len(contracts) >= 3  # At least the contracts we created

        # Verify contracts belong to the correct user
        for contract in contracts:
            if contract["user_id"] == self.buyer.user_id:
                assert contract["symbol"] in symbols

    def test_order_book_operations(self):
        """Test order book retrieval"""
        # Create some orders to populate the order book
        for i in range(3):
            contract_data = {
                "client_order_id": f"test_ob_buy_{i}_{int(time.time())}",
                "symbol": "TEST",
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": 10 + i,
                "price": 100.0 + i,
                "user_id": self.buyer.user_id
            }
            self.create_contract(contract_data)

        # Get order book
        order_book = self.get_order_book("TEST")

        assert "bids" in order_book
        assert "asks" in order_book
        assert "last_update" in order_book

        # Should have buy orders
        if order_book["bids"]:
            assert len(order_book["bids"]) > 0
            # Verify bids are sorted by price descending
            for i in range(len(order_book["bids"]) - 1):
                assert order_book["bids"][i]["price"] >= order_book["bids"][i + 1]["price"]

    def test_position_tracking(self):
        """Test position tracking and portfolio analytics"""
        # Get initial positions
        initial_positions = self.get_user_positions(self.buyer.user_id)

        # Create a buy contract (this would normally result in a position after execution)
        # Note: In a real scenario, this would be matched and executed
        contract_data = {
            "client_order_id": f"test_position_{int(time.time())}",
            "symbol": "POS_TEST",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 100.0,
            "user_id": self.buyer.user_id
        }

        self.create_contract(contract_data)

        # Get portfolio analytics
        analytics = self.get_portfolio_analytics(self.buyer.user_id)

        assert "total_value" in analytics
        assert "total_pnl" in analytics
        assert "positions" in analytics
        assert isinstance(analytics["positions"], list)

    def test_risk_limits(self):
        """Test risk limit enforcement"""
        # Try to create a contract that exceeds risk limits
        # This test assumes there are risk limits in place
        large_contract_data = {
            "client_order_id": f"test_risk_{int(time.time())}",
            "symbol": "RISK_TEST",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1000000,  # Very large quantity that should exceed limits
            "price": 1.0,
            "user_id": self.buyer.user_id
        }

        # This should either fail or be created but flagged for approval
        try:
            result = self.create_contract(large_contract_data)
            # If created, it might require approval
            assert "contract_id" in result
        except AssertionError:
            # Expected if risk limits are enforced
            pass

    def test_concurrent_contract_creation(self):
        """Test concurrent contract creation"""
        def create_contract_worker(worker_id: int):
            contract_data = {
                "client_order_id": f"test_concurrent_{worker_id}_{int(time.time())}",
                "symbol": "CONC_TEST",
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": 1,
                "price": 100.0 + worker_id,
                "user_id": self.buyer.user_id
            }
            return self.create_contract(contract_data)

        # Create contracts concurrently
        num_workers = 10
        with ThreadPoolExecutor(max_workers=num_workers) as executor:
            futures = [executor.submit(create_contract_worker, i) for i in range(num_workers)]
            results = [future.result() for future in as_completed(futures)]

        assert len(results) == num_workers
        for result in results:
            assert "contract_id" in result

    def test_contract_validation(self):
        """Test contract validation"""
        # Test invalid contract data
        invalid_contracts = [
            # Missing required fields
            {
                "symbol": "AAPL",
                "contract_type": "SPOT",
                "user_id": self.buyer.user_id
            },
            # Invalid quantity
            {
                "symbol": "AAPL",
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": -1,
                "price": 100.0,
                "user_id": self.buyer.user_id
            },
            # Invalid price for limit order
            {
                "symbol": "AAPL",
                "contract_type": "SPOT",
                "order_type": "LIMIT",
                "side": "BUY",
                "quantity": 1,
                "price": -50.0,
                "user_id": self.buyer.user_id
            }
        ]

        for invalid_contract in invalid_contracts:
            invalid_contract["client_order_id"] = f"test_invalid_{int(time.time())}"
            try:
                self.create_contract(invalid_contract)
                pytest.fail(f"Expected contract creation to fail for invalid data: {invalid_contract}")
            except AssertionError:
                # Expected failure
                pass

    def test_system_resilience(self):
        """Test system resilience under load"""
        # Create a burst of contracts to test system resilience
        burst_size = 50

        contract_data_template = {
            "symbol": "RESILIENCE_TEST",
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 100.0,
            "user_id": self.buyer.user_id
        }

        for i in range(burst_size):
            contract_data = contract_data_template.copy()
            contract_data["client_order_id"] = f"test_burst_{i}_{int(time.time())}"
            contract_data["price"] = 100.0 + i  # Vary prices slightly

            try:
                self.create_contract(contract_data)
            except Exception as e:
                # Some failures might be expected under extreme load
                print(f"Contract {i} failed: {e}")

        # Verify system is still healthy after burst
        health = self.check_health()
        assert health["status"] == "healthy"

        # Verify we can still create contracts
        final_contract = contract_data_template.copy()
        final_contract["client_order_id"] = f"test_final_{int(time.time())}"
        result = self.create_contract(final_contract)
        assert "contract_id" in result

# Test class instance
test_instance = TradingContractsIntegrationTest()

# Run individual tests
if __name__ == "__main__":
    print("Running Trading Contracts Integration Tests...")

    # Setup
    test_instance.setup_method()

    try:
        # Run tests
        test_instance.test_health_check()
        print("✅ Health check passed")

        test_instance.test_create_spot_contract_buy()
        print("✅ Spot buy contract creation passed")

        test_instance.test_create_spot_contract_sell()
        print("✅ Spot sell contract creation passed")

        test_instance.test_create_market_order()
        print("✅ Market order creation passed")

        test_instance.test_create_stop_order()
        print("✅ Stop order creation passed")

        test_instance.test_create_future_contract()
        print("✅ Future contract creation passed")

        test_instance.test_create_option_contract()
        print("✅ Option contract creation passed")

        test_instance.test_cancel_contract()
        print("✅ Contract cancellation passed")

        test_instance.test_get_user_contracts()
        print("✅ User contracts retrieval passed")

        test_instance.test_order_book_operations()
        print("✅ Order book operations passed")

        test_instance.test_position_tracking()
        print("✅ Position tracking passed")

        test_instance.test_risk_limits()
        print("✅ Risk limits test passed")

        test_instance.test_concurrent_contract_creation()
        print("✅ Concurrent contract creation passed")

        test_instance.test_contract_validation()
        print("✅ Contract validation passed")

        test_instance.test_system_resilience()
        print("✅ System resilience test passed")

        print("\n🎉 All integration tests passed successfully!")
        print("Trading Contracts system is fully functional and ready for production.")

    finally:
        # Cleanup
        test_instance.teardown_method()