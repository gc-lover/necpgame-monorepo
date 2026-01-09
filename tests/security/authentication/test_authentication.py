"""
Security Tests for Authentication and Authorization
Tests JWT tokens, RBAC, session management, and security vulnerabilities
"""

import pytest
import jwt
import json
import time
from datetime import datetime, timedelta
from unittest.mock import Mock, patch, MagicMock
import requests
from fastapi.testclient import TestClient

# Import security components (assuming they exist)
from services.quest_service.api.main import app
from services.quest_service.internal.security.jwt import JWTManager
from services.quest_service.internal.security.rbac import RBACManager, Role, Permission
from services.quest_service.internal.models.user import User


class TestAuthentication:
    """Test JWT authentication mechanisms"""

    @pytest.fixture
    def client(self):
        """FastAPI test client"""
        return TestClient(app)

    @pytest.fixture
    def jwt_manager(self):
        """JWT manager instance"""
        return JWTManager(secret_key="test_secret_key", algorithm="HS256")

    @pytest.fixture
    def rbac_manager(self):
        """RBAC manager instance"""
        return RBACManager()

    @pytest.fixture
    def valid_user(self):
        """Create valid user for testing"""
        return User(
            id="user_001",
            username="test_user",
            email="test@example.com",
            role="player",
            is_active=True,
            created_at=datetime.now()
        )

    def test_valid_jwt_token_creation(self, jwt_manager, valid_user):
        """Test creation of valid JWT token"""
        token = jwt_manager.create_token(valid_user)

        assert token is not None
        assert isinstance(token, str)

        # Decode and verify token
        payload = jwt_manager.decode_token(token)
        assert payload["sub"] == valid_user.id
        assert payload["role"] == valid_user.role
        assert "exp" in payload
        assert "iat" in payload

    def test_expired_jwt_token_rejection(self, jwt_manager, valid_user):
        """Test rejection of expired JWT tokens"""
        # Create token that's already expired
        expired_time = datetime.now() - timedelta(hours=1)
        with patch('services.quest_service.internal.security.jwt.datetime') as mock_datetime:
            mock_datetime.now.return_value = expired_time
            mock_datetime.utcnow.return_value = expired_time

            token = jwt_manager.create_token(valid_user)

        # Should be rejected as expired
        with pytest.raises(jwt.ExpiredSignatureError):
            jwt_manager.decode_token(token)

    def test_malformed_jwt_token_rejection(self, jwt_manager):
        """Test rejection of malformed JWT tokens"""
        malformed_tokens = [
            "not_a_jwt_token",
            "header.payload",  # Missing signature
            "header.payload.signature.extra",
            "",
            None
        ]

        for token in malformed_tokens:
            with pytest.raises((jwt.InvalidTokenError, AttributeError)):
                jwt_manager.decode_token(token)

    def test_tampered_jwt_token_rejection(self, jwt_manager, valid_user):
        """Test rejection of tampered JWT tokens"""
        # Create valid token
        token = jwt_manager.create_token(valid_user)

        # Tamper with the token (change payload)
        parts = token.split('.')
        import base64

        # Decode payload, modify, re-encode
        payload = json.loads(base64.urlsafe_b64decode(parts[1] + '==').decode())
        payload["role"] = "admin"  # Tamper: change role
        tampered_payload = base64.urlsafe_b64encode(
            json.dumps(payload).encode()
        ).decode().rstrip('=')

        tampered_token = f"{parts[0]}.{tampered_payload}.{parts[2]}"

        # Should be rejected due to signature mismatch
        with pytest.raises(jwt.InvalidSignatureError):
            jwt_manager.decode_token(tampered_token)

    def test_jwt_token_with_wrong_secret(self, jwt_manager, valid_user):
        """Test JWT token with wrong secret key"""
        # Create token with one secret
        token = jwt_manager.create_token(valid_user)

        # Try to decode with different secret
        wrong_jwt_manager = JWTManager(secret_key="wrong_secret", algorithm="HS256")

        with pytest.raises(jwt.InvalidSignatureError):
            wrong_jwt_manager.decode_token(token)

    def test_jwt_token_algorithm_mismatch(self, jwt_manager, valid_user):
        """Test JWT token with algorithm mismatch"""
        # Create token with HS256
        token = jwt_manager.create_token(valid_user)

        # Try to decode expecting different algorithm
        wrong_jwt_manager = JWTManager(secret_key="test_secret_key", algorithm="RS256")

        with pytest.raises(jwt.InvalidAlgorithmError):
            wrong_jwt_manager.decode_token(token)

    def test_jwt_token_api_access(self, client, jwt_manager, valid_user):
        """Test API access with valid JWT token"""
        token = jwt_manager.create_token(valid_user)

        headers = {"Authorization": f"Bearer {token}"}

        with patch('services.quest_service.api.routes.quest_service.get_quests') as mock_get:
            mock_get.return_value = []

            response = client.get("/api/v1/quests", headers=headers)

            # Should succeed (authentication passes, authorization may still fail)
            assert response.status_code in [200, 403, 404]  # Not 401

    def test_missing_jwt_token_api_rejection(self, client):
        """Test API rejection without JWT token"""
        response = client.get("/api/v1/quests")

        assert response.status_code == 401
        assert "Not authenticated" in response.json()["detail"]

    def test_invalid_jwt_token_api_rejection(self, client):
        """Test API rejection with invalid JWT token"""
        headers = {"Authorization": "Bearer invalid_token"}

        response = client.get("/api/v1/quests", headers=headers)

        assert response.status_code == 401
        assert "Invalid token" in response.json()["detail"]

    def test_expired_jwt_token_api_rejection(self, client, jwt_manager, valid_user):
        """Test API rejection with expired JWT token"""
        # Create expired token
        with patch('services.quest_service.internal.security.jwt.datetime') as mock_datetime:
            expired_time = datetime.now() - timedelta(hours=1)
            mock_datetime.now.return_value = expired_time
            mock_datetime.utcnow.return_value = expired_time

            expired_token = jwt_manager.create_token(valid_user)

        headers = {"Authorization": f"Bearer {expired_token}"}

        response = client.get("/api/v1/quests", headers=headers)

        assert response.status_code == 401
        assert "Token expired" in response.json()["detail"]


class TestAuthorization:
    """Test Role-Based Access Control (RBAC)"""

    @pytest.fixture
    def rbac_manager(self):
        """RBAC manager instance"""
        return RBACManager()

    @pytest.fixture
    def client(self):
        """FastAPI test client"""
        return TestClient(app)

    def test_role_hierarchy(self, rbac_manager):
        """Test role hierarchy and inheritance"""
        # Define role hierarchy
        assert rbac_manager.is_role_higher("admin", "moderator")
        assert rbac_manager.is_role_higher("moderator", "player")
        assert rbac_manager.is_role_higher("player", "guest")

        # Reflexive check
        assert not rbac_manager.is_role_higher("player", "admin")
        assert not rbac_manager.is_role_higher("guest", "player")

    def test_permission_assignment(self, rbac_manager):
        """Test permission assignment to roles"""
        # Admin should have all permissions
        assert rbac_manager.has_permission("admin", Permission.CREATE_QUEST)
        assert rbac_manager.has_permission("admin", Permission.DELETE_QUEST)
        assert rbac_manager.has_permission("admin", Permission.MANAGE_USERS)

        # Moderator permissions
        assert rbac_manager.has_permission("moderator", Permission.UPDATE_QUEST)
        assert rbac_manager.has_permission("moderator", Permission.DELETE_QUEST)
        assert not rbac_manager.has_permission("moderator", Permission.MANAGE_USERS)

        # Player permissions
        assert rbac_manager.has_permission("player", Permission.READ_QUEST)
        assert rbac_manager.has_permission("player", Permission.COMPLETE_QUEST)
        assert not rbac_manager.has_permission("player", Permission.CREATE_QUEST)

    def test_admin_create_quest_authorization(self, client):
        """Test admin authorization for quest creation"""
        # Create admin token
        admin_payload = {
            "sub": "admin_001",
            "role": "admin",
            "exp": datetime.now() + timedelta(hours=1)
        }
        admin_token = jwt.encode(admin_payload, "test_secret_key", algorithm="HS256")

        headers = {"Authorization": f"Bearer {admin_token}"}
        quest_data = {
            "title": "Admin Created Quest",
            "difficulty": "hard",
            "level_min": 1,
            "level_max": 10
        }

        with patch('services.quest_service.api.routes.quest_service.create_quest') as mock_create:
            mock_create.return_value = Mock(id="quest_001", title="Admin Created Quest")

            response = client.post("/api/v1/quests", json=quest_data, headers=headers)

            assert response.status_code == 201
            mock_create.assert_called_once()

    def test_player_create_quest_denied(self, client):
        """Test player denial for quest creation"""
        # Create player token
        player_payload = {
            "sub": "player_001",
            "role": "player",
            "exp": datetime.now() + timedelta(hours=1)
        }
        player_token = jwt.encode(player_payload, "test_secret_key", algorithm="HS256")

        headers = {"Authorization": f"Bearer {player_token}"}
        quest_data = {
            "title": "Player Created Quest",
            "difficulty": "easy",
            "level_min": 1,
            "level_max": 10
        }

        response = client.post("/api/v1/quests", json=quest_data, headers=headers)

        assert response.status_code == 403
        assert "Insufficient permissions" in response.json()["detail"]

    def test_moderator_update_quest_authorization(self, client):
        """Test moderator authorization for quest updates"""
        moderator_payload = {
            "sub": "mod_001",
            "role": "moderator",
            "exp": datetime.now() + timedelta(hours=1)
        }
        moderator_token = jwt.encode(moderator_payload, "test_secret_key", algorithm="HS256")

        headers = {"Authorization": f"Bearer {moderator_token}"}
        update_data = {"title": "Updated by Moderator"}

        with patch('services.quest_service.api.routes.quest_service.update_quest') as mock_update:
            mock_update.return_value = Mock(id="quest_001", title="Updated by Moderator")

            response = client.put("/api/v1/quests/quest_001", json=update_data, headers=headers)

            assert response.status_code == 200
            mock_update.assert_called_once()

    def test_guest_read_only_access(self, client):
        """Test guest role has only read access"""
        guest_payload = {
            "sub": "guest_001",
            "role": "guest",
            "exp": datetime.now() + timedelta(hours=1)
        }
        guest_token = jwt.encode(guest_payload, "test_secret_key", algorithm="HS256")

        headers = {"Authorization": f"Bearer {guest_token}"}

        # Should be able to read quests
        with patch('services.quest_service.api.routes.quest_service.get_quests') as mock_get:
            mock_get.return_value = []

            response = client.get("/api/v1/quests", headers=headers)
            assert response.status_code == 200

        # Should NOT be able to create quests
        quest_data = {"title": "Guest Quest", "difficulty": "easy", "level_min": 1, "level_max": 5}
        response = client.post("/api/v1/quests", json=quest_data, headers=headers)
        assert response.status_code == 403

    def test_role_escalation_prevention(self, client):
        """Test prevention of role escalation attacks"""
        # Try to create token with escalated privileges
        escalated_payload = {
            "sub": "user_001",
            "role": "admin",  # User trying to escalate to admin
            "exp": datetime.now() + timedelta(hours=1)
        }

        # This would be caught by proper token validation
        # But let's test the API behavior
        escalated_token = jwt.encode(escalated_payload, "test_secret_key", algorithm="HS256")
        headers = {"Authorization": f"Bearer {escalated_token}"}

        # Even with "admin" role in token, should be validated against user database
        with patch('services.quest_service.api.dependencies.get_current_user') as mock_user:
            mock_user.return_value = Mock(role="player")  # Actual role from database

            response = client.post("/api/v1/admin/quests", json={}, headers=headers)
            assert response.status_code == 403

    def test_permission_granularity(self, rbac_manager):
        """Test granular permission checking"""
        # Specific permissions
        assert rbac_manager.has_permission("admin", Permission.CREATE_QUEST)
        assert rbac_manager.has_permission("admin", Permission.READ_QUEST)
        assert rbac_manager.has_permission("admin", Permission.UPDATE_QUEST)
        assert rbac_manager.has_permission("admin", Permission.DELETE_QUEST)

        # Moderator can update and delete but not manage users
        assert rbac_manager.has_permission("moderator", Permission.UPDATE_QUEST)
        assert rbac_manager.has_permission("moderator", Permission.DELETE_QUEST)
        assert not rbac_manager.has_permission("moderator", Permission.MANAGE_USERS)

        # Player can only read and complete
        assert rbac_manager.has_permission("player", Permission.READ_QUEST)
        assert rbac_manager.has_permission("player", Permission.COMPLETE_QUEST)
        assert not rbac_manager.has_permission("player", Permission.UPDATE_QUEST)


class TestSecurityVulnerabilities:
    """Test common security vulnerabilities"""

    @pytest.fixture
    def client(self):
        """FastAPI test client"""
        return TestClient(app)

    def test_sql_injection_protection(self, client):
        """Test protection against SQL injection"""
        auth_headers = {"Authorization": "Bearer valid_token"}

        injection_payloads = [
            {"q": "'; DROP TABLE users; --"},
            {"q": "' OR '1'='1"},
            {"q": "' UNION SELECT * FROM users --"},
            {"q": "'; EXEC xp_cmdshell 'dir' --"},
            {"q": "'; WAITFOR DELAY '0:0:5' --"}
        ]

        with patch('services.quest_service.api.routes.quest_service.search_quests') as mock_search:
            mock_search.return_value = []

            for payload in injection_payloads:
                response = client.get("/api/v1/quests/search",
                                    params=payload,
                                    headers=auth_headers)

                # Should not crash or return sensitive data
                assert response.status_code in [200, 400, 422]
                if response.status_code == 200:
                    data = response.json()
                    assert "quests" in data
                    # Ensure no SQL errors in response
                    response_text = json.dumps(data)
                    assert "DROP TABLE" not in response_text
                    assert "UNION SELECT" not in response_text

    def test_xss_protection(self, client):
        """Test protection against XSS attacks"""
        auth_headers = {"Authorization": "Bearer valid_token"}

        xss_payloads = [
            "<script>alert('xss')</script>",
            "javascript:alert('xss')",
            "<img src=x onerror=alert('xss')>",
            "'><script>alert('xss')</script>",
            "<iframe src='javascript:alert(`xss`)'>"
        ]

        with patch('services.quest_service.api.routes.quest_service.search_quests') as mock_search:
            mock_search.return_value = []

            for payload in xss_payloads:
                response = client.get("/api/v1/quests/search",
                                    params={"q": payload},
                                    headers=auth_headers)

                assert response.status_code == 200
                data = response.json()
                response_text = json.dumps(data)

                # XSS payloads should be sanitized or rejected
                assert "<script>" not in response_text
                assert "javascript:" not in response_text
                assert "onerror" not in response_text

    def test_path_traversal_protection(self, client):
        """Test protection against path traversal attacks"""
        auth_headers = {"Authorization": "Bearer valid_token"}

        traversal_payloads = [
            "../../../etc/passwd",
            "..\\..\\..\\windows\\system32\\config\\sam",
            "/etc/passwd",
            "C:\\Windows\\System32\\config\\sam",
            "../../../../etc/shadow"
        ]

        for payload in traversal_payloads:
            # Try path traversal in quest ID parameter
            response = client.get(f"/api/v1/quests/{payload}", headers=auth_headers)

            # Should be rejected or return 404 (not file contents)
            assert response.status_code in [400, 404, 422]
            if response.status_code == 200:
                data = response.json()
                # Should not contain file contents
                assert "root:" not in json.dumps(data)
                assert "Administrator:" not in json.dumps(data)

    def test_rate_limiting_enforcement(self, client):
        """Test rate limiting is properly enforced"""
        auth_headers = {"Authorization": "Bearer valid_token"}

        # Make many rapid requests
        responses = []
        for i in range(105):  # Exceed rate limit
            response = client.get("/api/v1/quests", headers=auth_headers)
            responses.append(response)

        # Should eventually get rate limited
        rate_limited_responses = [r for r in responses if r.status_code == 429]
        assert len(rate_limited_responses) > 0

        # Check rate limit headers
        last_response = responses[-1]
        if last_response.status_code == 429:
            assert "Retry-After" in last_response.headers
            assert "X-RateLimit-Remaining" in last_response.headers

    def test_input_validation_comprehensive(self, client):
        """Test comprehensive input validation"""
        auth_headers = {"Authorization": "Bearer valid_token"}

        # Test various invalid inputs
        invalid_inputs = [
            {"title": "", "difficulty": "easy", "level_min": 1, "level_max": 10},  # Empty title
            {"title": "Test", "difficulty": "invalid", "level_min": 1, "level_max": 10},  # Invalid difficulty
            {"title": "Test", "difficulty": "easy", "level_min": 10, "level_max": 5},  # Min > Max
            {"title": "Test", "difficulty": "easy", "level_min": -1, "level_max": 10},  # Negative level
            {"title": "Test", "difficulty": "easy", "level_min": 1, "level_max": 101},  # Level too high
        ]

        with patch('services.quest_service.api.routes.quest_service.create_quest') as mock_create:
            mock_create.return_value = Mock(id="quest_001")

            for invalid_input in invalid_inputs:
                response = client.post("/api/v1/quests",
                                     json=invalid_input,
                                     headers=auth_headers)

                # Should be rejected with validation error
                assert response.status_code == 422
                error_data = response.json()
                assert "detail" in error_data
                mock_create.assert_not_called()

    def test_csrf_protection(self, client):
        """Test CSRF protection (if implemented)"""
        # POST requests should require CSRF tokens or other protections
        quest_data = {"title": "Test Quest", "difficulty": "easy", "level_min": 1, "level_max": 5}

        # Request without CSRF token
        response = client.post("/api/v1/quests",
                             json=quest_data,
                             headers={"Authorization": "Bearer valid_token"})

        # Should either succeed (if CSRF not required for API) or be rejected
        assert response.status_code in [200, 201, 403, 422]

    def test_secure_headers(self, client):
        """Test security headers are present"""
        response = client.get("/health")

        # Check for important security headers
        security_headers = [
            "X-Content-Type-Options",
            "X-Frame-Options",
            "X-XSS-Protection",
            "Strict-Transport-Security"
        ]

        for header in security_headers:
            assert header in response.headers, f"Missing security header: {header}"

        # Check header values
        assert response.headers.get("X-Content-Type-Options") == "nosniff"
        assert response.headers.get("X-Frame-Options") in ["DENY", "SAMEORIGIN"]
        assert response.headers.get("X-XSS-Protection") == "1; mode=block"

    def test_error_information_leakage(self, client):
        """Test that errors don't leak sensitive information"""
        # Try to access non-existent endpoint
        response = client.get("/api/v1/nonexistent")

        assert response.status_code == 404
        error_data = response.json()

        # Should not contain stack traces or internal paths
        error_text = json.dumps(error_data).lower()
        assert "traceback" not in error_text
        assert "internal" not in error_text
        assert "/app/" not in error_text
        assert "sql" not in error_text

    def test_session_management(self, client):
        """Test proper session management"""
        # Login
        login_data = {"username": "testuser", "password": "testpass"}
        response = client.post("/api/v1/auth/login", json=login_data)

        if response.status_code == 200:
            token = response.json().get("access_token")
            assert token is not None

            # Use token in subsequent requests
            headers = {"Authorization": f"Bearer {token}"}
            response = client.get("/api/v1/user/profile", headers=headers)

            # Should work initially
            assert response.status_code in [200, 404]  # 404 if profile doesn't exist

            # Simulate token expiration
            # (This would require time manipulation in real tests)
            # For now, just verify token is required for authenticated endpoints





