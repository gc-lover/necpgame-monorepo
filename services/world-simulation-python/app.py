#!/usr/bin/env python3
"""
World Simulation Service - Python
Event-Driven Diplomacy and World Simulation (#2281)

Handles:
- Diplomacy engine with Love/Fear metrics
- Daily tick processing
- World state simulation
"""

import asyncio
import json
import logging
import os
import signal
import sys
from datetime import datetime
from typing import Dict, List, Optional

from confluent_kafka import Consumer, Producer, KafkaError, KafkaException
from confluent_kafka.admin import AdminClient, NewTopic

# Import simulation modules
from crowd_simulation import WorldModel
from diplomacy_engine import DiplomacyEngine, DiplomaticState

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# DiplomacyEngine is imported from diplomacy_engine module above

class WorldSimulationService:
    """Main World Simulation Service"""

    def __init__(self):
        self.diplomacy_engine = DiplomacyEngine()
        self.crowd_model = None
        self.kafka_bootstrap = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:9092')
        self.consumer = None
        self.producer = None
        self.running = True
        self.crowd_simulation_enabled = os.getenv('CROWD_SIMULATION_ENABLED', 'true').lower() == 'true'

    async def initialize_kafka(self):
        """Initialize Kafka consumer and producer"""
        try:
            # Create topics if they don't exist
            admin_client = AdminClient({'bootstrap.servers': self.kafka_bootstrap})

            # Check if topics exist
            topics = ['world.tick.daily', 'simulation.event']
            existing_topics = admin_client.list_topics().topics

            topics_to_create = []
            for topic in topics:
                if topic not in existing_topics:
                    topics_to_create.append(NewTopic(topic, num_partitions=3, replication_factor=1))

            if topics_to_create:
                admin_client.create_topics(topics_to_create)
                logger.info(f"Created topics: {[t.topic for t in topics_to_create]}")

            # Initialize consumer for daily ticks
            self.consumer = Consumer({
                'bootstrap.servers': self.kafka_bootstrap,
                'group.id': 'world-simulation-group',
                'auto.offset.reset': 'latest'
            })
            self.consumer.subscribe(['world.tick.daily'])

            # Initialize producer for simulation events
            self.producer = Producer({'bootstrap.servers': self.kafka_bootstrap})

            logger.info("Kafka initialization completed")

        except Exception as e:
            logger.error(f"Kafka initialization failed: {e}")
            raise

    async def initialize_crowd_simulation(self):
        """Initialize crowd simulation model"""
        try:
            if self.crowd_simulation_enabled:
                # Initialize crowd model
                grid_width = int(os.getenv('CROWD_GRID_WIDTH', '50'))
                grid_height = int(os.getenv('CROWD_GRID_HEIGHT', '50'))
                num_agents = int(os.getenv('CROWD_NUM_AGENTS', '100'))

                self.crowd_model = WorldModel(
                    width=grid_width,
                    height=grid_height,
                    num_agents=num_agents
                )

                logger.info(f"Initialized crowd simulation: {num_agents} agents on {grid_width}x{grid_height} grid")
            else:
                logger.info("Crowd simulation disabled")

        except Exception as e:
            logger.error(f"Crowd simulation initialization failed: {e}")
            raise

    async def process_daily_tick(self, tick_data: dict):
        """Process daily tick - run diplomacy evaluation and crowd simulation"""
        logger.info("Processing daily tick - running diplomacy evaluation and crowd simulation")

        try:
            # Diplomacy evaluation
            diplomacy_events = await self._process_diplomacy_evaluation(tick_data)

            # Crowd simulation (if enabled)
            crowd_events = await self._process_crowd_simulation(tick_data)

            # Combine and publish all events
            all_events = diplomacy_events + crowd_events

            for event in all_events:
                self.producer.produce(
                    'simulation.event',
                    key=event.get('key', 'simulation_event'),
                    value=json.dumps(event).encode('utf-8')
                )

            self.producer.flush()
            logger.info(f"Published {len(all_events)} simulation events ({len(diplomacy_events)} diplomacy, {len(crowd_events)} crowd)")

        except Exception as e:
            logger.error(f"Error processing daily tick: {e}")

    async def _process_diplomacy_evaluation(self, tick_data: dict) -> List[Dict]:
        """Process diplomacy evaluation using FreeCiv-style Love/Fear engine"""
        events = []

        try:
            # Initialize factions if none exist
            if not self.diplomacy_engine.factions:
                self._initialize_factions()

            # Run diplomacy evaluation for all faction pairs
            results = self.diplomacy_engine.evaluate_all_relations()

            # Get diplomacy summary
            summary = self.diplomacy_engine.get_diplomacy_summary()

            # Create main evaluation event
            event_data = {
                'event_type': 'diplomacy_evaluation',
                'timestamp': datetime.now().isoformat(),
                'tick_id': tick_data.get('tick_id'),
                'results': results,
                'summary': summary,
                'key': 'diplomacy_results'
            }

            events.append(event_data)

            # Create individual state change events
            state_changes = self._extract_state_changes(results)
            events.extend(state_changes)

            logger.info(f"Generated diplomacy evaluation: {len(results)} relations, {len(state_changes)} state changes")

        except Exception as e:
            logger.error(f"Error in diplomacy evaluation: {e}")

        return events

    def _initialize_factions(self):
        """Initialize test factions with realistic power levels"""
        factions_data = [
            ('arastov_family', 8.5, 'Arasaka Corporation Family'),
            ('nomad_nomads', 6.2, 'Nomad Trading Families'),
            ('valentino_gang', 7.1, 'Valentino Crime Syndicate'),
            ('maelstrom_gang', 9.3, 'Maestro Cyberpsycho Gang'),
            ('tyger_claws', 7.8, 'Tyger Claws Triad'),
            ('militia', 5.5, 'Free City Militia'),
            ('corporation', 8.8, 'Mega Corporation Alliance'),
        ]

        for faction_id, power, name in factions_data:
            self.diplomacy_engine.add_faction(faction_id, power, name)

            # Set additional faction properties
            faction = self.diplomacy_engine.factions[faction_id]
            faction.economic_strength = power * 0.8  # Economic power correlated with military
            faction.territory_size = power * 0.6     # Territory size
            faction.technology_level = power * 0.7   # Tech level
            faction.morale = 0.7 + (power / 10) * 0.3  # Morale based on power

    def _extract_state_changes(self, results: Dict[str, DiplomaticState]) -> List[Dict]:
        """Extract diplomatic state changes from evaluation results"""
        changes = []

        for relation_key, state in results.items():
            # Check if this represents a change (simplified - in real implementation
            # we'd track previous states)
            if state in [DiplomaticState.WAR, DiplomaticState.ALLIANCE]:
                # This is a significant state change
                factions = relation_key.split('_', 1)
                if len(factions) == 2:
                    event = {
                        'event_type': 'diplomacy_state_change',
                        'timestamp': datetime.now().isoformat(),
                        'faction_a': factions[0],
                        'faction_b': factions[1],
                        'new_state': state.value,
                        'severity': 'high' if state == DiplomaticState.WAR else 'medium',
                        'key': f"diplomacy_{relation_key}_{state.value.lower()}"
                    }
                    changes.append(event)

        return changes

    async def _process_crowd_simulation(self, tick_data: dict) -> List[Dict]:
        """Process crowd simulation step and return aggregated signals"""
        events = []

        if not self.crowd_simulation_enabled or not self.crowd_model:
            return events

        try:
            # Run one step of crowd simulation
            self.crowd_model.step()

            # Get aggregated signals
            signals = self.crowd_model.get_aggregated_signals()

            # Convert signals to events
            for signal in signals:
                event_data = {
                    'event_type': 'crowd_signal',
                    'timestamp': datetime.now().isoformat(),
                    'tick_id': tick_data.get('tick_id'),
                    'signal_data': signal,
                    'key': f"crowd_{signal.get('signal_type', 'unknown')}"
                }
                events.append(event_data)

            logger.debug(f"Generated {len(signals)} crowd signals")

        except Exception as e:
            logger.error(f"Error in crowd simulation: {e}")

        return events

    async def run_consumer(self):
        """Run Kafka consumer loop"""
        logger.info("Starting daily tick consumer...")

        while self.running:
            try:
                msg = self.consumer.poll(1.0)

                if msg is None:
                    continue

                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        continue
                    else:
                        logger.error(f"Consumer error: {msg.error()}")
                        continue

                # Process the tick message
                tick_data = json.loads(msg.value().decode('utf-8'))
                await self.process_daily_tick(tick_data)

            except Exception as e:
                logger.error(f"Error in consumer loop: {e}")
                await asyncio.sleep(1)

    async def shutdown(self):
        """Graceful shutdown"""
        logger.info("Shutting down World Simulation Service...")
        self.running = False

        if self.consumer:
            self.consumer.close()
        if self.producer:
            self.producer.flush()

async def main():
    """Main service entry point"""
    service = WorldSimulationService()

    # Setup signal handlers
    def signal_handler(signum, frame):
        logger.info(f"Received signal {signum}, shutting down...")
        service.running = False

    signal.signal(signal.SIGINT, signal_handler)
    signal.signal(signal.SIGTERM, signal_handler)

    try:
        # Initialize Kafka
        await service.initialize_kafka()

        # Initialize crowd simulation
        await service.initialize_crowd_simulation()

        # Run consumer
        await service.run_consumer()

    except Exception as e:
        logger.error(f"Service error: {e}")
        sys.exit(1)
    finally:
        await service.shutdown()

if __name__ == "__main__":
    asyncio.run(main())