"""
Unit Tests for Quest Service
Tests individual components of the quest service with mocked dependencies
"""

import pytest
import asyncio
from unittest.mock import Mock, AsyncMock, patch, MagicMock
from datetime import datetime, timedelta
import json
from typing import Dict, List, Any

# Import service components (assuming they exist)
from services.quest_service.internal.service.quest_service import QuestService
from services.quest_service.internal.models.quest import Quest, QuestStatus
from services.quest_service.internal.models.player import Player
from services.quest_service.internal.repository.quest_repository import QuestRepository
from services.quest_service.internal.service.validation_service import ValidationService


class TestQuestService:
    """Unit tests for QuestService class"""

    @pytest.fixture
    def mock_repository(self):
        """Mock quest repository"""
        return Mock(spec=QuestRepository)

    @pytest.fixture
    def mock_validation_service(self):
        """Mock validation service"""
        return Mock(spec=ValidationService)

    @pytest.fixture
    def quest_service(self, mock_repository, mock_validation_service):
        """Create quest service with mocked dependencies"""
        return QuestService(
            repository=mock_repository,
            validation_service=mock_validation_service
        )

    @pytest.fixture
    def sample_quest(self):
        """Create sample quest for testing"""
        return Quest(
            id="quest_001",
            title="Test Corporate Espionage",
            description="Infiltrate Arasaka tower and steal corporate secrets",
            difficulty="hard",
            level_min=15,
            level_max=25,
            rewards={
                "experience": 800,
                "eddies": 2500,
                "reputation": {"corporate": -10, "street": 15}
            },
            objectives=[
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
            status=QuestStatus.ACTIVE,
            created_at=datetime.now(),
            updated_at=datetime.now()
        )

    @pytest.fixture
    def sample_player(self):
        """Create sample player for testing"""
        return Player(
            id="player_001",
            username="test_player",
            level=20,
            experience=15000,
            reputation={
                "corporate": 25,
                "street": -5,
                "humanity": 80
            }
        )

    @pytest.mark.asyncio
    async def test_create_quest_success(self, quest_service, mock_repository,
                                      mock_validation_service, sample_quest):
        """Test successful quest creation"""
        # Arrange
        quest_data = {
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
                }
            ]
        }

        mock_validation_service.validate_quest_data.return_value = True
        mock_repository.create_quest.return_value = sample_quest

        # Act
        result = await quest_service.create_quest(quest_data)

        # Assert
        assert result is not None
        assert result.id == sample_quest.id
        assert result.title == quest_data["title"]
        mock_validation_service.validate_quest_data.assert_called_once_with(quest_data)
        mock_repository.create_quest.assert_called_once()

    @pytest.mark.asyncio
    async def test_create_quest_validation_failure(self, quest_service,
                                                 mock_validation_service):
        """Test quest creation with validation failure"""
        # Arrange
        invalid_quest_data = {
            "title": "",  # Invalid: empty title
            "difficulty": "invalid_difficulty"
        }

        mock_validation_service.validate_quest_data.return_value = False
        mock_validation_service.get_validation_errors.return_value = [
            "Title cannot be empty",
            "Invalid difficulty level"
        ]

        # Act & Assert
        with pytest.raises(ValueError) as exc_info:
            await quest_service.create_quest(invalid_quest_data)

        assert "Title cannot be empty" in str(exc_info.value)
        mock_validation_service.validate_quest_data.assert_called_once_with(invalid_quest_data)
        mock_repository.create_quest.assert_not_called()

    @pytest.mark.asyncio
    async def test_get_quest_by_id_found(self, quest_service, mock_repository, sample_quest):
        """Test retrieving existing quest by ID"""
        # Arrange
        quest_id = "quest_001"
        mock_repository.get_quest_by_id.return_value = sample_quest

        # Act
        result = await quest_service.get_quest_by_id(quest_id)

        # Assert
        assert result is not None
        assert result.id == quest_id
        assert result.title == sample_quest.title
        mock_repository.get_quest_by_id.assert_called_once_with(quest_id)

    @pytest.mark.asyncio
    async def test_get_quest_by_id_not_found(self, quest_service, mock_repository):
        """Test retrieving non-existent quest by ID"""
        # Arrange
        quest_id = "non_existent_quest"
        mock_repository.get_quest_by_id.return_value = None

        # Act & Assert
        with pytest.raises(ValueError) as exc_info:
            await quest_service.get_quest_by_id(quest_id)

        assert "Quest not found" in str(exc_info.value)
        mock_repository.get_quest_by_id.assert_called_once_with(quest_id)

    @pytest.mark.asyncio
    async def test_assign_quest_to_player_success(self, quest_service, mock_repository,
                                                sample_quest, sample_player):
        """Test successful quest assignment to player"""
        # Arrange
        quest_id = sample_quest.id
        player_id = sample_player.id

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.get_player_by_id.return_value = sample_player
        mock_repository.assign_quest_to_player.return_value = True

        # Act
        result = await quest_service.assign_quest_to_player(quest_id, player_id)

        # Assert
        assert result is True
        mock_repository.assign_quest_to_player.assert_called_once_with(quest_id, player_id)

    @pytest.mark.asyncio
    async def test_assign_quest_to_player_level_too_low(self, quest_service, mock_repository,
                                                      sample_quest, sample_player):
        """Test quest assignment failure due to insufficient player level"""
        # Arrange - Modify player to be too low level
        sample_player.level = 10  # Below quest minimum of 15
        quest_id = sample_quest.id
        player_id = sample_player.id

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.get_player_by_id.return_value = sample_player

        # Act & Assert
        with pytest.raises(ValueError) as exc_info:
            await quest_service.assign_quest_to_player(quest_id, player_id)

        assert "Player level too low" in str(exc_info.value)
        mock_repository.assign_quest_to_player.assert_not_called()

    @pytest.mark.asyncio
    async def test_assign_quest_to_player_level_too_high(self, quest_service, mock_repository,
                                                       sample_quest, sample_player):
        """Test quest assignment failure due to excessive player level"""
        # Arrange - Modify player to be too high level
        sample_player.level = 30  # Above quest maximum of 25
        quest_id = sample_quest.id
        player_id = sample_player.id

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.get_player_by_id.return_value = sample_player

        # Act & Assert
        with pytest.raises(ValueError) as exc_info:
            await quest_service.assign_quest_to_player(quest_id, player_id)

        assert "Player level too high" in str(exc_info.value)
        mock_repository.assign_quest_to_player.assert_not_called()

    @pytest.mark.asyncio
    async def test_complete_quest_success(self, quest_service, mock_repository,
                                        sample_quest, sample_player):
        """Test successful quest completion"""
        # Arrange
        quest_id = sample_quest.id
        player_id = sample_player.id
        completion_data = {
            "objectives_completed": ["infiltrate", "hack"],
            "time_taken_seconds": 1800,
            "difficulty_modifier": 1.0
        }

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.get_player_by_id.return_value = sample_player
        mock_repository.complete_quest.return_value = {
            "experience_gained": 800,
            "eddies_gained": 2500,
            "reputation_changes": {"corporate": -10, "street": 15}
        }

        # Act
        result = await quest_service.complete_quest(quest_id, player_id, completion_data)

        # Assert
        assert result is not None
        assert "experience_gained" in result
        assert "eddies_gained" in result
        assert "reputation_changes" in result
        mock_repository.complete_quest.assert_called_once()

    @pytest.mark.asyncio
    async def test_complete_quest_already_completed(self, quest_service, mock_repository,
                                                  sample_quest, sample_player):
        """Test quest completion failure for already completed quest"""
        # Arrange
        sample_quest.status = QuestStatus.COMPLETED
        quest_id = sample_quest.id
        player_id = sample_player.id

        mock_repository.get_quest_by_id.return_value = sample_quest

        # Act & Assert
        with pytest.raises(ValueError) as exc_info:
            await quest_service.complete_quest(quest_id, player_id, {})

        assert "Quest already completed" in str(exc_info.value)
        mock_repository.complete_quest.assert_not_called()

    @pytest.mark.asyncio
    async def test_get_quests_by_difficulty(self, quest_service, mock_repository, sample_quest):
        """Test retrieving quests filtered by difficulty"""
        # Arrange
        difficulty = "hard"
        mock_repository.get_quests_by_difficulty.return_value = [sample_quest]

        # Act
        result = await quest_service.get_quests_by_difficulty(difficulty)

        # Assert
        assert len(result) == 1
        assert result[0].id == sample_quest.id
        assert result[0].difficulty == difficulty
        mock_repository.get_quests_by_difficulty.assert_called_once_with(difficulty)

    @pytest.mark.asyncio
    async def test_get_quests_by_level_range(self, quest_service, mock_repository, sample_quest):
        """Test retrieving quests within level range"""
        # Arrange
        min_level = 15
        max_level = 25
        mock_repository.get_quests_by_level_range.return_value = [sample_quest]

        # Act
        result = await quest_service.get_quests_by_level_range(min_level, max_level)

        # Assert
        assert len(result) == 1
        assert result[0].level_min >= min_level
        assert result[0].level_max <= max_level
        mock_repository.get_quests_by_level_range.assert_called_once_with(min_level, max_level)

    @pytest.mark.asyncio
    async def test_search_quests_by_title(self, quest_service, mock_repository, sample_quest):
        """Test quest search by title"""
        # Arrange
        search_term = "corporate"
        mock_repository.search_quests_by_title.return_value = [sample_quest]

        # Act
        result = await quest_service.search_quests_by_title(search_term)

        # Assert
        assert len(result) == 1
        assert search_term.lower() in result[0].title.lower()
        mock_repository.search_quests_by_title.assert_called_once_with(search_term)

    @pytest.mark.asyncio
    async def test_update_quest_rewards(self, quest_service, mock_repository, sample_quest):
        """Test updating quest rewards"""
        # Arrange
        quest_id = sample_quest.id
        new_rewards = {
            "experience": 1000,
            "eddies": 3000,
            "reputation": {"corporate": -15, "street": 20}
        }

        updated_quest = sample_quest.copy()
        updated_quest.rewards = new_rewards

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.update_quest.return_value = updated_quest

        # Act
        result = await quest_service.update_quest_rewards(quest_id, new_rewards)

        # Assert
        assert result.rewards == new_rewards
        mock_repository.update_quest.assert_called_once()

    @pytest.mark.asyncio
    async def test_archive_quest(self, quest_service, mock_repository, sample_quest):
        """Test quest archiving"""
        # Arrange
        quest_id = sample_quest.id
        archived_quest = sample_quest.copy()
        archived_quest.status = QuestStatus.ARCHIVED

        mock_repository.get_quest_by_id.return_value = sample_quest
        mock_repository.update_quest.return_value = archived_quest

        # Act
        result = await quest_service.archive_quest(quest_id)

        # Assert
        assert result.status == QuestStatus.ARCHIVED
        mock_repository.update_quest.assert_called_once()

    @pytest.mark.asyncio
    async def test_get_quest_statistics(self, quest_service, mock_repository):
        """Test retrieving quest completion statistics"""
        # Arrange
        expected_stats = {
            "total_quests": 150,
            "active_quests": 120,
            "completed_quests": 25,
            "archived_quests": 5,
            "average_completion_time": 3600,  # 1 hour
            "most_popular_difficulty": "normal",
            "highest_success_rate": "easy"
        }

        mock_repository.get_quest_statistics.return_value = expected_stats

        # Act
        result = await quest_service.get_quest_statistics()

        # Assert
        assert result["total_quests"] == 150
        assert result["most_popular_difficulty"] == "normal"
        mock_repository.get_quest_statistics.assert_called_once()

    # Error handling tests
    @pytest.mark.asyncio
    async def test_database_connection_error(self, quest_service, mock_repository):
        """Test handling database connection errors"""
        # Arrange
        mock_repository.get_quest_by_id.side_effect = Exception("Database connection lost")

        # Act & Assert
        with pytest.raises(Exception) as exc_info:
            await quest_service.get_quest_by_id("quest_001")

        assert "Database connection lost" in str(exc_info.value)

    @pytest.mark.asyncio
    async def test_concurrent_quest_operations(self, quest_service, mock_repository, sample_quest):
        """Test concurrent quest operations"""
        # Arrange
        mock_repository.get_quest_by_id.return_value = sample_quest

        # Act - Simulate concurrent access
        import asyncio
        tasks = []
        for i in range(10):
            task = asyncio.create_task(quest_service.get_quest_by_id(f"quest_{i}"))
            tasks.append(task)

        results = await asyncio.gather(*tasks, return_exceptions=True)

        # Assert - All operations should succeed
        successful_results = [r for r in results if not isinstance(r, Exception)]
        assert len(successful_results) == 10

    # Performance tests
    @pytest.mark.performance
    @pytest.mark.asyncio
    async def test_quest_creation_performance(self, quest_service, mock_repository,
                                            mock_validation_service, benchmark):
        """Performance test for quest creation"""
        # Arrange
        quest_data = {
            "title": "Performance Test Quest",
            "description": "Testing quest creation performance",
            "difficulty": "normal",
            "level_min": 10,
            "level_max": 20,
            "rewards": {"experience": 200, "eddies": 500},
            "objectives": [{"type": "collect", "target": "items", "count": 5}]
        }

        mock_validation_service.validate_quest_data.return_value = True
        mock_repository.create_quest.return_value = sample_quest

        # Act & Benchmark
        async def create_quest_operation():
            return await quest_service.create_quest(quest_data)

        result = await benchmark(create_quest_operation)

        # Assert performance requirements
        assert result.stats.mean < 0.050  # Should complete in <50ms on average
        assert result.stats.max < 0.100   # Should never exceed 100ms
