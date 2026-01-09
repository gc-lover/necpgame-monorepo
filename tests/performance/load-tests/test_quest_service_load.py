"""
Load Tests for Quest Service
Performance testing under various load conditions using Locust
"""

import json
import random
import time
from typing import Dict, List, Any
from locust import HttpUser, task, between, events
from locust.exception import StopUser


class QuestServiceUser(HttpUser):
    """Load test user for Quest Service"""

    # Wait time between requests (1-3 seconds)
    wait_time = between(1, 3)

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.auth_token = None
        self.user_id = None
        self.assigned_quests = []

    def on_start(self):
        """Initialize user session"""
        self.authenticate()
        self.load_user_data()

    def authenticate(self):
        """Authenticate and get JWT token"""
        auth_payload = {
            "username": f"load_test_user_{random.randint(1, 10000)}",
            "password": "test_password_123"
        }

        with self.client.post("/api/v1/auth/login",
                            json=auth_payload,
                            catch_response=True) as response:
            if response.status_code == 200:
                data = response.json()
                self.auth_token = data.get("access_token")
                self.user_id = data.get("user_id")
                response.success()
            else:
                response.failure(f"Authentication failed: {response.status_code}")
                raise StopUser()

    def load_user_data(self):
        """Load initial user data"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        with self.client.get("/api/v1/user/profile",
                           headers=headers,
                           catch_response=True) as response:
            if response.status_code != 200:
                response.failure(f"Failed to load user profile: {response.status_code}")
                raise StopUser()

    @task(3)  # Higher weight - most common operation
    def browse_quests(self):
        """Browse available quests"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        # Random filters to simulate different user behaviors
        filters = self._generate_random_filters()

        with self.client.get("/api/v1/quests",
                           params=filters,
                           headers=headers,
                           catch_response=True) as response:

            if response.status_code == 200:
                data = response.json()
                self._validate_quest_list_response(data)
                response.success()
            elif response.status_code == 429:  # Rate limited
                response.success()  # This is expected under load
            else:
                response.failure(f"Browse quests failed: {response.status_code}")

    @task(2)
    def search_quests(self):
        """Search for quests by various criteria"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        search_terms = [
            "corporate", "street", "gang", "hacking", "combat",
            "infiltration", "mercenary", "nomad", "tech", "crime"
        ]

        search_term = random.choice(search_terms)

        with self.client.get("/api/v1/quests/search",
                           params={"q": search_term},
                           headers=headers,
                           catch_response=True) as response:

            if response.status_code == 200:
                data = response.json()
                self._validate_quest_search_response(data, search_term)
                response.success()
            else:
                response.failure(f"Quest search failed: {response.status_code}")

    @task(1)
    def get_quest_details(self):
        """Get detailed information about a specific quest"""
        if not hasattr(self, '_available_quest_ids') or not self._available_quest_ids:
            self._load_available_quests()

        if self._available_quest_ids:
            quest_id = random.choice(self._available_quest_ids)
            headers = {"Authorization": f"Bearer {self.auth_token}"}

            with self.client.get(f"/api/v1/quests/{quest_id}",
                               headers=headers,
                               catch_response=True) as response:

                if response.status_code == 200:
                    data = response.json()
                    self._validate_quest_detail_response(data)
                    response.success()
                elif response.status_code == 404:
                    # Quest might have been deleted, remove from cache
                    if quest_id in self._available_quest_ids:
                        self._available_quest_ids.remove(quest_id)
                    response.success()
                else:
                    response.failure(f"Get quest details failed: {response.status_code}")

    @task(1)
    def assign_quest(self):
        """Assign a quest to the user"""
        if not hasattr(self, '_available_quest_ids') or not self._available_quest_ids:
            self._load_available_quests()

        if self._available_quest_ids:
            quest_id = random.choice(self._available_quest_ids)
            headers = {"Authorization": f"Bearer {self.auth_token}"}

            payload = {"player_id": self.user_id}

            with self.client.post(f"/api/v1/quests/{quest_id}/assign",
                                json=payload,
                                headers=headers,
                                catch_response=True) as response:

                if response.status_code == 200:
                    self.assigned_quests.append(quest_id)
                    response.success()
                elif response.status_code in [400, 409]:  # Already assigned or level too low
                    response.success()  # This is expected behavior
                else:
                    response.failure(f"Quest assignment failed: {response.status_code}")

    @task(1)
    def complete_quest(self):
        """Complete an assigned quest"""
        if self.assigned_quests:
            quest_id = random.choice(self.assigned_quests)
            headers = {"Authorization": f"Bearer {self.auth_token}"}

            completion_data = self._generate_completion_data()

            with self.client.post(f"/api/v1/quests/{quest_id}/complete",
                                json={"player_id": self.user_id, **completion_data},
                                headers=headers,
                                catch_response=True) as response:

                if response.status_code == 200:
                    # Remove from assigned quests
                    if quest_id in self.assigned_quests:
                        self.assigned_quests.remove(quest_id)
                    response.success()
                elif response.status_code == 400:  # Already completed
                    if quest_id in self.assigned_quests:
                        self.assigned_quests.remove(quest_id)
                    response.success()
                else:
                    response.failure(f"Quest completion failed: {response.status_code}")

    @task(1)
    def get_user_quests(self):
        """Get user's assigned and completed quests"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        with self.client.get("/api/v1/user/quests",
                           headers=headers,
                           catch_response=True) as response:

            if response.status_code == 200:
                data = response.json()
                self._validate_user_quests_response(data)
                response.success()
            else:
                response.failure(f"Get user quests failed: {response.status_code}")

    def _load_available_quests(self):
        """Load available quest IDs for testing"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        try:
            with self.client.get("/api/v1/quests",
                               params={"limit": 50},
                               headers=headers,
                               catch_response=True) as response:

                if response.status_code == 200:
                    data = response.json()
                    self._available_quest_ids = [quest["id"] for quest in data.get("quests", [])]
                else:
                    self._available_quest_ids = []
        except Exception:
            self._available_quest_ids = []

    def _generate_random_filters(self) -> Dict[str, Any]:
        """Generate random quest filters"""
        filters = {}

        # Random difficulty filter
        if random.random() < 0.3:
            filters["difficulty"] = random.choice(["easy", "normal", "hard"])

        # Random level range filter
        if random.random() < 0.2:
            min_level = random.randint(1, 40)
            max_level = min_level + random.randint(5, 20)
            filters["level_min"] = min_level
            filters["level_max"] = max_level

        # Random status filter
        if random.random() < 0.1:
            filters["status"] = random.choice(["active", "completed"])

        return filters

    def _generate_completion_data(self) -> Dict[str, Any]:
        """Generate random quest completion data"""
        objectives_completed = []
        num_objectives = random.randint(1, 3)

        objective_types = ["infiltrate", "hack", "combat", "collect", "deliver", "escort"]

        for _ in range(num_objectives):
            objectives_completed.append(random.choice(objective_types))

        return {
            "objectives_completed": objectives_completed,
            "time_taken_seconds": random.randint(600, 7200),  # 10 minutes to 2 hours
            "difficulty_modifier": random.uniform(0.8, 1.2)
        }

    def _validate_quest_list_response(self, data: Dict[str, Any]):
        """Validate quest list response structure"""
        assert "quests" in data
        assert isinstance(data["quests"], list)

        if data["quests"]:
            quest = data["quests"][0]
            required_fields = ["id", "title", "difficulty", "level_min", "level_max", "status"]
            for field in required_fields:
                assert field in quest, f"Missing required field: {field}"

    def _validate_quest_search_response(self, data: Dict[str, Any], search_term: str):
        """Validate quest search response"""
        assert "quests" in data
        assert isinstance(data["quests"], list)

        # At least some quests should be returned (depending on data)
        if data["quests"]:
            quest = data["quests"][0]
            assert "id" in quest
            assert "title" in quest

    def _validate_quest_detail_response(self, data: Dict[str, Any]):
        """Validate detailed quest response"""
        required_fields = [
            "id", "title", "description", "difficulty",
            "level_min", "level_max", "rewards", "objectives",
            "status", "created_at", "updated_at"
        ]

        for field in required_fields:
            assert field in data, f"Missing required field: {field}"

        assert isinstance(data["objectives"], list)
        assert isinstance(data["rewards"], dict)

    def _validate_user_quests_response(self, data: Dict[str, Any]):
        """Validate user quests response"""
        assert "assigned_quests" in data
        assert "completed_quests" in data

        assert isinstance(data["assigned_quests"], list)
        assert isinstance(data["completed_quests"], list)


# Performance monitoring hooks
@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    """Test start hook"""
    print("🏁 Starting Quest Service Load Test")
    print(f"Target: {environment.host}")
    print(f"Users: {environment.runner.user_count if environment.runner else 'N/A'}")


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """Test stop hook"""
    print("\n🏁 Quest Service Load Test Completed")

    if environment.runner:
        stats = environment.runner.stats

        print("📊 Performance Summary:")
        print(f"  Total Requests: {stats.total_requests}")
        print(f"  Average Response Time: {stats.avg_response_time:.2f}ms")
        print(f"  95th Percentile: {stats.get_response_time_percentile(0.95):.2f}ms")
        print(f"  99th Percentile: {stats.get_response_time_percentile(0.99):.2f}ms")
        print(f"  Requests/sec: {stats.total_rps:.2f}")
        print(f"  Failures: {stats.num_failures}")

        # Performance assertions
        if stats.avg_response_time > 100:
            print("⚠️  WARNING: Average response time exceeds 100ms threshold")

        if stats.get_response_time_percentile(0.95) > 200:
            print("⚠️  WARNING: P95 response time exceeds 200ms threshold")

        if stats.num_failures / stats.total_requests > 0.05:
            print("❌ FAILURE: Error rate exceeds 5% threshold")
        else:
            print("✅ SUCCESS: Error rate within acceptable limits")


@events.request.add_listener
def on_request(request_type, name, response_time, response_length, response,
               context, exception, start_time, url, **kwargs):
    """Individual request monitoring"""
    if exception:
        print(f"❌ Request failed: {name} - {exception}")
    elif response_time > 1000:  # Log slow requests
        print(f"🐌 Slow request: {name} - {response_time}ms")


# Stress test configuration for extreme load
class StressTestUser(QuestServiceUser):
    """Stress test user with aggressive behavior"""

    wait_time = between(0.1, 0.5)  # Much faster requests

    @task(10)
    def aggressive_browse(self):
        """Aggressive quest browsing"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        for _ in range(random.randint(5, 20)):  # Multiple requests per task
            filters = self._generate_random_filters()
            self.client.get("/api/v1/quests", params=filters, headers=headers)

    @task(5)
    def aggressive_search(self):
        """Aggressive quest searching"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        search_terms = ["a", "b", "c", "d", "e", "*", "%", ""]  # Edge case searches

        for term in search_terms:
            self.client.get("/api/v1/quests/search",
                          params={"q": term},
                          headers=headers)


# Spike test configuration
class SpikeTestUser(QuestServiceUser):
    """Spike test user for sudden traffic increases"""

    wait_time = between(0.05, 0.1)  # Very fast requests

    @task
    def spike_request(self):
        """High-frequency requests during spike"""
        headers = {"Authorization": f"Bearer {self.auth_token}"}

        # Make multiple requests in rapid succession
        for _ in range(random.randint(10, 50)):
            self.client.get("/api/v1/quests", headers=headers)


# Endurance test configuration
class EnduranceTestUser(QuestServiceUser):
    """Endurance test user for long-duration stability"""

    wait_time = between(5, 15)  # Slower, more realistic pacing

    def on_start(self):
        """Extended initialization for endurance testing"""
        super().on_start()

        # Simulate user session that lasts longer
        self.session_start = time.time()
        self.request_count = 0

    @task
    def endurance_task(self):
        """Tasks designed for long-term execution"""
        super().browse_quests()
        self.request_count += 1

        # Log progress every 100 requests
        if self.request_count % 100 == 0:
            session_duration = time.time() - self.session_start
            print(f"📊 Endurance user active for {session_duration:.1f}s, {self.request_count} requests")


# Custom load shapes for different test scenarios
def constant_load_shape(stage_users, stage_time):
    """Constant load shape for steady-state testing"""
    return stage_users


def ramp_up_load_shape(stage_users, stage_time):
    """Gradual ramp-up load shape"""
    # Linear ramp up over 10 minutes
    ramp_duration = 600  # 10 minutes
    if stage_time < ramp_duration:
        return int(stage_users * (stage_time / ramp_duration))
    return stage_users


def spike_load_shape(stage_users, stage_time):
    """Spike load shape with sudden increases"""
    spike_interval = 300  # 5 minutes
    spike_duration = 60   # 1 minute spikes

    cycle_time = stage_time % spike_interval

    if cycle_time < spike_duration:
        return stage_users * 5  # 5x normal load during spikes
    else:
        return stage_users // 2  # Reduced load between spikes




