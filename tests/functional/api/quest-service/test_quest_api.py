"""
API Tests for Quest Service
Tests REST API endpoints with real HTTP calls and mocked backend
"""

import pytest
import json
import jwt
from datetime import datetime, timedelta
from unittest.mock import Mock, patch
import requests_mock
from fastapi.testclient import TestClient

# Import API components (assuming they exist)
from services.quest_service.api.main import app
from services.quest_service.internal.models.quest import Quest, QuestStatus
from services.quest_service.internal.models.player import Player


class TestQuestAPI:
    """API tests for quest service endpoints"""

    @pytest.fixture
    def client(self):
        """FastAPI test client"""
        return TestClient(app)

    @pytest.fixture
    def auth_headers(self):
        """Generate authentication headers"""
        payload = {
            "sub": "test_player_001",
            "role": "player",
            "exp": datetime.utcnow() + timedelta(hours=1)
        }
        token = jwt.encode(payload, "test_secret_key", algorithm="HS256")
        return {"Authorization": f"Bearer {token}"}

    @pytest.fixture
    def admin_auth_headers(self):
        """Generate admin authentication headers"""
        payload = {
            "sub": "admin_user_001",
            "role": "admin",
            "exp": datetime.utcnow() + timedelta(hours=1)
        }
        token = jwt.encode(payload, "test_secret_key", algorithm="HS256")
        return {"Authorization": f"Bearer {token}"}

    @pytest.fixture
    def sample_quest_data(self):
        """Sample quest creation data"""
        return {
            "title": "Test Corporate Espionage",
            "description": "Infiltrate Arasaka tower and steal corporate secrets",
            "difficulty": "hard",
            "level_min": 15,
            "level_max": 25,
            "rewards": {
                "experience": 800,
                "eddies": 2500,
                "reputation": {"corporate": -10, "street": 15}
            },
            "objectives": [
                {
                    "type": "infiltrate",
                    "target": "arasaka_tower",
                    "description": "Reach the server room undetected"
                },
                {
                    "type": "hack",
                    "target": "security_system",
                    "description": "Bypass security systems"
                }
            ]
        }

    @pytest.fixture
    def sample_quest_response(self):
        """Sample quest response data"""
        return {
            "id": "quest_001",
            "title": "Test Corporate Espionage",
            "description": "Infiltrate Arasaka tower and steal corporate secrets",
            "difficulty": "hard",
            "level_min": 15,
            "level_max": 25,
            "rewards": {
                "experience": 800,
                "eddies": 2500,
                "reputation": {"corporate": -10, "street": 15}
            },
            "objectives": [
                {
                    "type": "infiltrate",
                    "target": "arasaka_tower",
                    "description": "Reach the server room undetected"
                },
                {
                    "type": "hack",
                    "target": "security_system",
                    "description": "Bypass security systems"
                }
            ],
            "status": "active",
            "created_at": "2026-01-06T12:00:00Z",
            "updated_at": "2026-01-06T12:00:00Z"
        }

    def test_health_check(self, client):
        """Test health check endpoint"""
        response = client.get("/health")

        assert response.status_code == 200
        assert response.json() == {"status": "healthy", "service": "quest-service"}

    def test_create_quest_success(self, client, admin_auth_headers, sample_quest_data, sample_quest_response):
        """Test successful quest creation"""
        with patch('services.quest_service.api.routes.quest_service.create_quest') as mock_create:
            mock_create.return_value = Quest(**sample_quest_response)

            response = client.post(
                "/api/v1/quests",
                json=sample_quest_data,
                headers=admin_auth_headers
            )

            assert response.status_code == 201
            response_data = response.json()
            assert response_data["id"] is not None
            assert response_data["title"] == sample_quest_data["title"]
            assert response_data["difficulty"] == sample_quest_data["difficulty"]
            mock_create.assert_called_once()

    def test_create_quest_unauthorized(self, client, sample_quest_data):
        """Test quest creation without authentication"""
        response = client.post("/api/v1/quests", json=sample_quest_data)

        assert response.status_code == 401
        assert "Not authenticated" in response.json()["detail"]

    def test_create_quest_forbidden(self, client, auth_headers, sample_quest_data):
        """Test quest creation with insufficient permissions"""
        response = client.post(
            "/api/v1/quests",
            json=sample_quest_data,
            headers=auth_headers
        )

        assert response.status_code == 403
        assert "Insufficient permissions" in response.json()["detail"]

    def test_create_quest_validation_error(self, client, admin_auth_headers):
        """Test quest creation with validation errors"""
        invalid_data = {
            "title": "",  # Empty title
            "difficulty": "invalid_difficulty",
            "level_min": 30,
            "level_max": 20  # Min > Max
        }

        response = client.post(
            "/api/v1/quests",
            json=invalid_data,
            headers=admin_auth_headers
        )

        assert response.status_code == 422
        errors = response.json()["detail"]
        assert len(errors) > 0
        assert any("title" in str(error) for error in errors)

    def test_get_quest_by_id_success(self, client, auth_headers, sample_quest_response):
        """Test retrieving quest by ID"""
        quest_id = "quest_001"

        with patch('services.quest_service.api.routes.quest_service.get_quest_by_id') as mock_get:
            mock_get.return_value = Quest(**sample_quest_response)

            response = client.get(f"/api/v1/quests/{quest_id}", headers=auth_headers)

            assert response.status_code == 200
            response_data = response.json()
            assert response_data["id"] == quest_id
            assert response_data["title"] == sample_quest_response["title"]
            mock_get.assert_called_once_with(quest_id)

    def test_get_quest_by_id_not_found(self, client, auth_headers):
        """Test retrieving non-existent quest"""
        quest_id = "non_existent_quest"

        with patch('services.quest_service.api.routes.quest_service.get_quest_by_id') as mock_get:
            mock_get.side_effect = ValueError("Quest not found")

            response = client.get(f"/api/v1/quests/{quest_id}", headers=auth_headers)

            assert response.status_code == 404
            assert "Quest not found" in response.json()["detail"]

    def test_get_quests_list(self, client, auth_headers, sample_quest_response):
        """Test retrieving quests list"""
        with patch('services.quest_service.api.routes.quest_service.get_quests') as mock_get:
            mock_get.return_value = [Quest(**sample_quest_response)]

            response = client.get("/api/v1/quests", headers=auth_headers)

            assert response.status_code == 200
            response_data = response.json()
            assert "quests" in response_data
            assert len(response_data["quests"]) == 1
            assert response_data["quests"][0]["id"] == sample_quest_response["id"]

    def test_get_quests_with_filters(self, client, auth_headers, sample_quest_response):
        """Test retrieving quests with filters"""
        filters = {
            "difficulty": "hard",
            "level_min": 15,
            "level_max": 25,
            "status": "active"
        }

        with patch('services.quest_service.api.routes.quest_service.get_quests') as mock_get:
            mock_get.return_value = [Quest(**sample_quest_response)]

            response = client.get("/api/v1/quests", params=filters, headers=auth_headers)

            assert response.status_code == 200
            mock_get.assert_called_once()
            call_args = mock_get.call_args[1]  # Keyword arguments
            assert call_args["difficulty"] == "hard"
            assert call_args["level_min"] == 15

    def test_assign_quest_to_player_success(self, client, auth_headers):
        """Test successful quest assignment to player"""
        quest_id = "quest_001"
        player_id = "player_001"

        with patch('services.quest_service.api.routes.quest_service.assign_quest_to_player') as mock_assign:
            mock_assign.return_value = True

            response = client.post(
                f"/api/v1/quests/{quest_id}/assign",
                json={"player_id": player_id},
                headers=auth_headers
            )

            assert response.status_code == 200
            assert response.json()["success"] is True
            mock_assign.assert_called_once_with(quest_id, player_id)

    def test_assign_quest_to_player_quest_not_found(self, client, auth_headers):
        """Test quest assignment when quest doesn't exist"""
        quest_id = "non_existent_quest"
        player_id = "player_001"

        with patch('services.quest_service.api.routes.quest_service.assign_quest_to_player') as mock_assign:
            mock_assign.side_effect = ValueError("Quest not found")

            response = client.post(
                f"/api/v1/quests/{quest_id}/assign",
                json={"player_id": player_id},
                headers=auth_headers
            )

            assert response.status_code == 404
            assert "Quest not found" in response.json()["detail"]

    def test_assign_quest_to_player_level_mismatch(self, client, auth_headers):
        """Test quest assignment with level mismatch"""
        quest_id = "quest_001"
        player_id = "player_001"

        with patch('services.quest_service.api.routes.quest_service.assign_quest_to_player') as mock_assign:
            mock_assign.side_effect = ValueError("Player level too low for this quest")

            response = client.post(
                f"/api/v1/quests/{quest_id}/assign",
                json={"player_id": player_id},
                headers=auth_headers
            )

            assert response.status_code == 400
            assert "level too low" in response.json()["detail"]

    def test_complete_quest_success(self, client, auth_headers):
        """Test successful quest completion"""
        quest_id = "quest_001"
        player_id = "player_001"
        completion_data = {
            "objectives_completed": ["infiltrate", "hack"],
            "time_taken_seconds": 1800,
            "difficulty_modifier": 1.0
        }

        expected_rewards = {
            "experience_gained": 800,
            "eddies_gained": 2500,
            "reputation_changes": {"corporate": -10, "street": 15}
        }

        with patch('services.quest_service.api.routes.quest_service.complete_quest') as mock_complete:
            mock_complete.return_value = expected_rewards

            response = client.post(
                f"/api/v1/quests/{quest_id}/complete",
                json={"player_id": player_id, **completion_data},
                headers=auth_headers
            )

            assert response.status_code == 200
            response_data = response.json()
            assert response_data["experience_gained"] == 800
            assert response_data["eddies_gained"] == 2500
            mock_complete.assert_called_once()

    def test_complete_quest_already_completed(self, client, auth_headers):
        """Test quest completion when already completed"""
        quest_id = "quest_001"
        player_id = "player_001"

        with patch('services.quest_service.api.routes.quest_service.complete_quest') as mock_complete:
            mock_complete.side_effect = ValueError("Quest already completed")

            response = client.post(
                f"/api/v1/quests/{quest_id}/complete",
                json={"player_id": player_id},
                headers=auth_headers
            )

            assert response.status_code == 400
            assert "already completed" in response.json()["detail"]

    def test_search_quests(self, client, auth_headers, sample_quest_response):
        """Test quest search functionality"""
        search_term = "corporate"

        with patch('services.quest_service.api.routes.quest_service.search_quests') as mock_search:
            mock_search.return_value = [Quest(**sample_quest_response)]

            response = client.get(
                "/api/v1/quests/search",
                params={"q": search_term},
                headers=auth_headers
            )

            assert response.status_code == 200
            response_data = response.json()
            assert len(response_data["quests"]) == 1
            assert search_term.lower() in response_data["quests"][0]["title"].lower()
            mock_search.assert_called_once_with(search_term)

    def test_get_quest_statistics(self, client, admin_auth_headers):
        """Test quest statistics endpoint"""
        expected_stats = {
            "total_quests": 150,
            "active_quests": 120,
            "completed_quests": 25,
            "archived_quests": 5,
            "average_completion_time": 3600,
            "most_popular_difficulty": "normal",
            "highest_success_rate": "easy"
        }

        with patch('services.quest_service.api.routes.quest_service.get_quest_statistics') as mock_stats:
            mock_stats.return_value = expected_stats

            response = client.get("/api/v1/quests/statistics", headers=admin_auth_headers)

            assert response.status_code == 200
            response_data = response.json()
            assert response_data["total_quests"] == 150
            assert response_data["most_popular_difficulty"] == "normal"
            mock_stats.assert_called_once()

    def test_update_quest_success(self, client, admin_auth_headers, sample_quest_response):
        """Test successful quest update"""
        quest_id = "quest_001"
        update_data = {
            "title": "Updated Quest Title",
            "rewards": {"experience": 1000, "eddies": 3000}
        }

        updated_quest = sample_quest_response.copy()
        updated_quest.update(update_data)
        updated_quest["updated_at"] = "2026-01-06T13:00:00Z"

        with patch('services.quest_service.api.routes.quest_service.update_quest') as mock_update:
            mock_update.return_value = Quest(**updated_quest)

            response = client.put(
                f"/api/v1/quests/{quest_id}",
                json=update_data,
                headers=admin_auth_headers
            )

            assert response.status_code == 200
            response_data = response.json()
            assert response_data["title"] == "Updated Quest Title"
            assert response_data["rewards"]["experience"] == 1000
            mock_update.assert_called_once()

    def test_archive_quest_success(self, client, admin_auth_headers, sample_quest_response):
        """Test successful quest archiving"""
        quest_id = "quest_001"

        archived_quest = sample_quest_response.copy()
        archived_quest["status"] = "archived"

        with patch('services.quest_service.api.routes.quest_service.archive_quest') as mock_archive:
            mock_archive.return_value = Quest(**archived_quest)

            response = client.delete(f"/api/v1/quests/{quest_id}", headers=admin_auth_headers)

            assert response.status_code == 200
            response_data = response.json()
            assert response_data["status"] == "archived"
            mock_archive.assert_called_once_with(quest_id)

    def test_rate_limiting(self, client, auth_headers):
        """Test API rate limiting"""
        # Make multiple rapid requests
        responses = []
        for i in range(105):  # Exceed rate limit
            response = client.get("/api/v1/quests", headers=auth_headers)
            responses.append(response)

        # Should get rate limited after threshold
        rate_limited_responses = [r for r in responses if r.status_code == 429]
        assert len(rate_limited_responses) > 0

        last_response = responses[-1]
        assert last_response.status_code == 429
        assert "rate limit" in last_response.json()["detail"].lower()

    def test_cors_headers(self, client):
        """Test CORS headers"""
        response = client.options("/api/v1/quests")

        assert response.status_code == 200
        assert "access-control-allow-origin" in response.headers
        assert "access-control-allow-methods" in response.headers
        assert "access-control-allow-headers" in response.headers

    def test_api_versioning(self, client, auth_headers):
        """Test API versioning"""
        # Test v1 endpoint
        response_v1 = client.get("/api/v1/quests", headers=auth_headers)
        assert response_v1.status_code in [200, 401, 403]  # Authentication may fail but endpoint exists

        # Test version header
        response = client.get("/api/v1/quests", headers={
            **auth_headers,
            "Accept-Version": "1.0"
        })
        assert "api-version" in response.headers

    def test_content_type_validation(self, client, admin_auth_headers, sample_quest_data):
        """Test content type validation"""
        # Test with wrong content type
        response = client.post(
            "/api/v1/quests",
            data=json.dumps(sample_quest_data),  # Raw data instead of json
            headers={**admin_auth_headers, "Content-Type": "text/plain"}
        )

        assert response.status_code == 422  # Unprocessable Entity

        # Test with correct content type
        with patch('services.quest_service.api.routes.quest_service.create_quest') as mock_create:
            mock_create.return_value = Quest(id="test", title="test", difficulty="easy", level_min=1, level_max=10)

            response = client.post(
                "/api/v1/quests",
                json=sample_quest_data,
                headers={**admin_auth_headers, "Content-Type": "application/json"}
            )

            assert response.status_code == 201

    def test_request_size_limits(self, client, admin_auth_headers):
        """Test request size limits"""
        # Create large payload
        large_quest_data = {
            "title": "Large Quest",
            "description": "x" * 10000,  # 10KB description
            "objectives": [{"description": "x" * 1000} for _ in range(100)]  # Large objectives
        }

        response = client.post(
            "/api/v1/quests",
            json=large_quest_data,
            headers=admin_auth_headers
        )

        # Should be rejected due to size limits
        assert response.status_code in [413, 422]  # Payload Too Large or Validation Error

    def test_concurrent_requests(self, client, auth_headers, sample_quest_response):
        """Test handling of concurrent requests"""
        import threading
        import queue

        results = queue.Queue()

        def make_request():
            try:
                with patch('services.quest_service.api.routes.quest_service.get_quests') as mock_get:
                    mock_get.return_value = [Quest(**sample_quest_response)]
                    response = client.get("/api/v1/quests", headers=auth_headers)
                    results.put((response.status_code, response.json()))
            except Exception as e:
                results.put(("error", str(e)))

        # Create multiple concurrent threads
        threads = []
        for i in range(10):
            thread = threading.Thread(target=make_request)
            threads.append(thread)
            thread.start()

        # Wait for all threads to complete
        for thread in threads:
            thread.join()

        # Check results
        successful_requests = 0
        while not results.empty():
            status, data = results.get()
            if status == 200:
                successful_requests += 1
                assert "quests" in data
            elif status == "error":
                pytest.fail(f"Request failed with error: {data}")

        assert successful_requests == 10  # All requests should succeed
