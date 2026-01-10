#!/usr/bin/env python3
"""
Crowd Simulation Module - Mesa ABM Framework
Background crowd agents generating signals and events (#2280)

Implements:
- CrowdAgent with simple behaviors (walk, buy_food, tweet)
- WorldModel as grid environment
- Signal aggregation and Kafka publishing
"""

import random
import logging
from typing import Dict, List, Tuple, Optional
from datetime import datetime

from mesa import Agent, Model
from mesa.space import MultiGrid
from mesa.datacollection import DataCollector

logger = logging.getLogger(__name__)

class CrowdAgent(Agent):
    """Simple crowd agent with basic behaviors"""

    def __init__(self, model: 'WorldModel'):
        super().__init__(model)
        # Set unique_id manually since Mesa 3.x doesn't take it in constructor
        self.unique_id = id(self)  # Use object id as unique identifier

        # Agent characteristics
        self.age = random.randint(18, 65)
        self.wealth = random.randint(50, 500)  # eddies
        self.hunger = random.uniform(0, 100)  # 0-100 scale
        self.social_drive = random.uniform(0, 100)  # tendency to interact
        self.movement_speed = random.uniform(0.5, 2.0)  # grid cells per step

        # Behavior state
        self.current_behavior = "idle"
        self.last_action = None
        self.home_location = None
        self.work_location = None
        self.favorite_spots = []  # Popular locations

        # Social connections
        self.friends = set()
        self.influence_score = random.uniform(0, 10)  # How influential this agent is

        # Initialize home location
        self._set_home_location()

    def _set_home_location(self):
        """Set random home location in residential areas"""
        # Assuming grid layout with residential areas in corners
        grid_width, grid_height = self.model.grid.width, self.model.grid.height

        # Residential zones (bottom-left corner for simplicity)
        residential_x = random.randint(0, grid_width // 4)
        residential_y = random.randint(0, grid_height // 4)

        self.home_location = (residential_x, residential_y)
        self.pos = self.home_location  # Start at home

    def step(self):
        """Main agent step - decide and execute behavior"""
        self._update_state()
        behavior = self._choose_behavior()
        self._execute_behavior(behavior)

    def _update_state(self):
        """Update agent internal state"""
        # Increase hunger over time
        self.hunger = min(100, self.hunger + random.uniform(1, 3))

        # Random social drive fluctuation
        self.social_drive += random.uniform(-5, 5)
        self.social_drive = max(0, min(100, self.social_drive))

    def _choose_behavior(self) -> str:
        """Choose next behavior based on state and priorities"""

        # High priority: extreme hunger
        if self.hunger > 80:
            return "buy_food"

        # Medium priority: social interaction
        if self.social_drive > 70 and random.random() < 0.3:
            return "tweet"

        # Low priority: random movement
        if random.random() < 0.6:
            return "walk"

        return "idle"

    def _execute_behavior(self, behavior: str):
        """Execute chosen behavior"""
        self.current_behavior = behavior
        self.last_action = datetime.now()

        if behavior == "walk":
            self._walk_randomly()
        elif behavior == "buy_food":
            self._buy_food()
        elif behavior == "tweet":
            self._tweet()
        elif behavior == "idle":
            pass  # Do nothing

    def _walk_randomly(self):
        """Random walk within grid bounds"""
        possible_steps = self.model.grid.get_neighborhood(
            self.pos, moore=True, include_center=False, radius=1
        )

        if possible_steps:
            new_position = random.choice(possible_steps)
            self.model.grid.move_agent(self, new_position)

            # Record movement signal
            self.model.add_signal({
                'type': 'movement',
                'agent_id': self.unique_id,
                'from_pos': self.pos,
                'to_pos': new_position,
                'timestamp': datetime.now().isoformat()
            })

    def _buy_food(self):
        """Attempt to buy food at current location"""
        # Check if current location has food vendor
        location_type = self.model.get_location_type(self.pos)

        if location_type == "food_vendor":
            # Successful purchase
            cost = random.randint(10, 30)
            if self.wealth >= cost:
                self.wealth -= cost
                self.hunger = max(0, self.hunger - random.uniform(40, 60))

                # Record purchase signal
                self.model.add_signal({
                    'type': 'purchase',
                    'agent_id': self.unique_id,
                    'item': 'food',
                    'cost': cost,
                    'location': self.pos,
                    'timestamp': datetime.now().isoformat()
                })

                logger.debug(f"Agent {self.unique_id} bought food at {self.pos}")
            else:
                # Can't afford - walk away disappointed
                self._walk_randomly()
        else:
            # Not at food vendor - look for one
            self._move_toward_food_vendor()

    def _move_toward_food_vendor(self):
        """Move toward nearest food vendor"""
        food_vendors = self.model.get_food_vendor_locations()

        if food_vendors:
            # Find closest food vendor
            closest_vendor = min(food_vendors,
                               key=lambda pos: abs(pos[0] - self.pos[0]) + abs(pos[1] - self.pos[1]))

            # Move one step toward vendor
            dx = 1 if closest_vendor[0] > self.pos[0] else -1 if closest_vendor[0] < self.pos[0] else 0
            dy = 1 if closest_vendor[1] > self.pos[1] else -1 if closest_vendor[1] < self.pos[1] else 0

            new_x = max(0, min(self.model.grid.width - 1, self.pos[0] + dx))
            new_y = max(0, min(self.model.grid.height - 1, self.pos[1] + dy))

            if (new_x, new_y) != self.pos:
                self.model.grid.move_agent(self, (new_x, new_y))

    def _tweet(self):
        """Post a social media update"""
        tweet_types = [
            "Just saw something weird in the alley...",
            "Food prices are insane today!",
            "Met some interesting people at the bar",
            "Night City never sleeps",
            "Working late again...",
            "Anyone seen my neural implant charger?"
        ]

        tweet_content = random.choice(tweet_types)

        # Record tweet signal
        self.model.add_signal({
            'type': 'social_post',
            'agent_id': self.unique_id,
            'content': tweet_content,
            'influence_score': self.influence_score,
            'location': self.pos,
            'timestamp': datetime.now().isoformat()
        })

        # Social drive satisfied
        self.social_drive = max(0, self.social_drive - random.uniform(20, 40))

        logger.debug(f"Agent {self.unique_id} tweeted: {tweet_content}")


class WorldModel(Model):
    """Mesa model for Night City crowd simulation"""

    def __init__(self, width: int = 50, height: int = 50, num_agents: int = 100):
        super().__init__()

        self.width = width
        self.height = height
        self.num_agents = num_agents

        # Initialize grid
        self.grid = MultiGrid(width, height, torus=False)

        # Mesa 3.x uses agents collection, no separate scheduler needed
        self.current_step = 0

        # Location types
        self.location_types = self._initialize_location_types()

        # Signals collector
        self.signals = []

        # Data collection
        self.datacollector = DataCollector(
            model_reporters={
                "total_agents": lambda m: len(m.agents),
                "total_signals": lambda m: len(m.signals),
                "active_behaviors": self._count_behaviors
            }
        )

        # Create agents
        for i in range(num_agents):
            agent = CrowdAgent(self)
            agent.unique_id = i  # Set proper unique ID

            # Place agent at home initially
            if agent.pos:
                self.grid.place_agent(agent, agent.pos)

        logger.info(f"Initialized WorldModel with {num_agents} agents on {width}x{height} grid")

    def _initialize_location_types(self) -> Dict[Tuple[int, int], str]:
        """Initialize location types on the grid"""
        locations = {}

        # Food vendors (restaurants, street food)
        num_food_vendors = max(5, self.width * self.height // 200)  # 1 per 200 cells

        for _ in range(num_food_vendors):
            x = random.randint(0, self.width - 1)
            y = random.randint(0, self.height - 1)
            locations[(x, y)] = "food_vendor"

        # Social spots (bars, clubs)
        num_social_spots = max(3, self.width * self.height // 500)

        for _ in range(num_social_spots):
            x = random.randint(0, self.width - 1)
            y = random.randint(0, self.height - 1)
            if (x, y) not in locations:  # Don't overwrite food vendors
                locations[(x, y)] = "social_spot"

        return locations

    def get_location_type(self, pos: Tuple[int, int]) -> str:
        """Get location type at position"""
        return self.location_types.get(pos, "street")

    def get_food_vendor_locations(self) -> List[Tuple[int, int]]:
        """Get all food vendor locations"""
        return [pos for pos, loc_type in self.location_types.items() if loc_type == "food_vendor"]

    def add_signal(self, signal: Dict):
        """Add signal to collection"""
        self.signals.append(signal)

    def _count_behaviors(self) -> Dict[str, int]:
        """Count agents by current behavior"""
        behaviors = {}
        for agent in self.agents:
            behavior = getattr(agent, 'current_behavior', 'unknown')
            behaviors[behavior] = behaviors.get(behavior, 0) + 1
        return behaviors

    def step(self):
        """Model step - execute all agent steps and collect data"""
        # Clear previous signals
        self.signals = []

        # Execute agent steps (Mesa 3.x style)
        for agent in self.agents:
            agent.step()

        # Increment step counter
        self.current_step += 1

        # Collect data
        self.datacollector.collect(self)

        # Process signals for this step
        self._process_step_signals()

    def _process_step_signals(self):
        """Process and aggregate signals from this step"""
        if not self.signals:
            return

        # Aggregate signals by type
        signal_counts = {}
        purchases_total = 0
        social_posts = []

        for signal in self.signals:
            signal_type = signal['type']
            signal_counts[signal_type] = signal_counts.get(signal_type, 0) + 1

            if signal_type == 'purchase':
                purchases_total += signal.get('cost', 0)
            elif signal_type == 'social_post':
                social_posts.append(signal)

        # Create aggregated signals
        aggregated_signals = []

        # Economic signals
        if signal_counts.get('purchase', 0) > 0:
            aggregated_signals.append({
                'type': 'economic_signal',
                'signal_type': 'food_demand',
                'value': signal_counts['purchase'],
                'total_spent': purchases_total,
                'description': f"Food demand increased: {signal_counts['purchase']} purchases, {purchases_total} eddies spent",
                'timestamp': datetime.now().isoformat(),
                'step': self.current_step
            })

        # Social signals
        if social_posts:
            # Find most influential post
            most_influential = max(social_posts, key=lambda x: x.get('influence_score', 0))
            aggregated_signals.append({
                'type': 'social_signal',
                'signal_type': 'rumor_spread',
                'content': most_influential['content'],
                'influence': most_influential['influence_score'],
                'total_posts': len(social_posts),
                'description': f"Social buzz: {len(social_posts)} posts, trending: '{most_influential['content']}'",
                'timestamp': datetime.now().isoformat(),
                'step': self.current_step
            })

        # Movement signals (crowd density)
        if signal_counts.get('movement', 0) > 10:
            aggregated_signals.append({
                'type': 'crowd_signal',
                'signal_type': 'population_movement',
                'movement_count': signal_counts['movement'],
                'description': f"High population movement: {signal_counts['movement']} agents moving",
                'timestamp': datetime.now().isoformat(),
                'step': self.current_step
            })

        # Store aggregated signals for external access
        self.aggregated_signals = aggregated_signals

        logger.info(f"Step {self.current_step}: Generated {len(aggregated_signals)} aggregated signals from {len(self.signals)} raw signals")

    def get_aggregated_signals(self) -> List[Dict]:
        """Get aggregated signals from last step"""
        return getattr(self, 'aggregated_signals', [])