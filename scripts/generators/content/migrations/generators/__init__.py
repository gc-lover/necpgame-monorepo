"""
Content migration generators.
"""

from .quests_generator import QuestMigrationGenerator
from .npcs_generator import NpcsMigrationGenerator
from .npcs_v2_generator import NpcsV2MigrationGenerator
from .dialogues_generator import DialoguesMigrationGenerator
from .lore_generator import LoreMigrationGenerator
from .enemies_generator import EnemiesMigrationGenerator
from .interactives_generator import InteractivesMigrationGenerator
from .items_generator import ItemsMigrationGenerator
from .culture_generator import CultureMigrationGenerator
from .documentation_generator import DocumentationMigrationGenerator

__all__ = [
    'QuestMigrationGenerator',
    'NpcsMigrationGenerator',
    'NpcsV2MigrationGenerator',
    'DialoguesMigrationGenerator',
    'LoreMigrationGenerator',
    'EnemiesMigrationGenerator',
    'InteractivesMigrationGenerator',
    'ItemsMigrationGenerator',
    'CultureMigrationGenerator',
    'DocumentationMigrationGenerator'
]
