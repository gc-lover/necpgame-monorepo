#!/usr/bin/env python3
"""
NECPGAME World Events Service
Dynamic world events and global content management

Features:
- Global event scheduling and management
- Dynamic content activation based on player actions
- World state changes and environmental effects
- Event-driven narrative progression
- Real-time event broadcasting
"""

import os
import sys
import time
import json
import asyncio
import aiohttp
import psycopg2
import psycopg2.extras
from datetime import datetime, timedelta
from typing import Dict, List, Any, Optional
from dataclasses import dataclass, asdict
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor
import logging
import random

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

@dataclass
class WorldEvent:
    """World event data structure"""
    id: str
    name: str
    description: str
    event_type: str  # "global", "regional", "local", "personal"
    severity: str    # "minor", "moderate", "major", "catastrophic"
    affected_regions: List[str]
    start_time: datetime
    end_time: Optional[datetime]
    status: str  # "scheduled", "active", "completed", "cancelled"
    effects: Dict[str, Any]
    triggers: List[Dict[str, Any]]
    rewards: Dict[str, Any]

class WorldEventsService:
    """World events management service"""

    def __init__(self):
        self.db_config = {
            'host': 'localhost',
            'port': 5432,
            'database': 'necpgame',
            'user': 'postgres',
            'password': 'postgres'
        }
        self.active_events: Dict[str, WorldEvent] = {}
        self.executor = ThreadPoolExecutor(max_workers=4)

    def get_db_connection(self):
        """Get database connection"""
        try:
            return psycopg2.connect(**self.db_config)
        except Exception as e:
            logger.error(f"Database connection failed: {e}")
            return None

    def create_sample_events(self) -> List[WorldEvent]:
        """Create sample world events for demonstration"""
        now = datetime.now()
        events = []

        # Corporate Espionage Event
        events.append(WorldEvent(
            id="event-corporate-espionage-001",
            name="Corporate Data Breach Alert",
            description="Arasaka Corporation has detected a massive data breach. All corporate zones are on high alert.",
            event_type="global",
            severity="major",
            affected_regions=["america", "europe", "asia"],
            start_time=now,
            end_time=now + timedelta(hours=2),
            status="active",
            effects={
                "security_level": "high",
                "corporate_zones": "locked_down",
                "data_prices": "increased_200%"
            },
            triggers=[
                {"type": "player_action", "action": "hack_corporate_server", "threshold": 10},
                {"type": "time_based", "interval": "2_hours"}
            ],
            rewards={
                "experience_bonus": 150,
                "reputation_change": {"corporate": -20, "street": 10}
            }
        ))

        # Weather Event
        events.append(WorldEvent(
            id="event-weather-storm-001",
            name="Neural Storm Warning",
            description="A massive neural storm is approaching Night City. Network connectivity is unstable.",
            event_type="regional",
            severity="moderate",
            affected_regions=["america"],
            start_time=now + timedelta(minutes=30),
            end_time=now + timedelta(hours=1, minutes=30),
            status="scheduled",
            effects={
                "network_stability": "unstable",
                "travel_difficulty": "increased",
                "mission_rewards": "bonus_50%"
            },
            triggers=[
                {"type": "environmental", "condition": "storm_intensity", "threshold": 80},
                {"type": "time_based", "interval": "1_hour"}
            ],
            rewards={
                "experience_bonus": 100,
                "currency_multiplier": 1.5
            }
        ))

        # Gang War Event
        events.append(WorldEvent(
            id="event-gang-war-001",
            name="Street Gang Territory Dispute",
            description="Maas and Rogue Amendiares are clashing over territory in downtown Seattle.",
            event_type="local",
            severity="moderate",
            affected_regions=["america"],
            start_time=now + timedelta(hours=1),
            end_time=now + timedelta(hours=3),
            status="scheduled",
            effects={
                "street_zones": "combat_active",
                "npc_behavior": "aggressive",
                "safe_zones": "reduced"
            },
            triggers=[
                {"type": "faction_reputation", "faction_a": "maas", "faction_b": "rogue_amendiares", "threshold": -50},
                {"type": "player_involvement", "actions": ["combat", "territory_control"]}
            ],
            rewards={
                "faction_reputation": {"winner_faction": 25},
                "experience_bonus": 200,
                "rare_items": ["gang_artifacts"]
            }
        ))

        return events

    def get_active_events(self) -> List[Dict[str, Any]]:
        """Get all currently active world events"""
        events = self.create_sample_events()
        active_events = [e for e in events if e.status == "active"]

        # Convert datetime objects to ISO strings for JSON serialization
        result = []
        for event in active_events:
            event_dict = asdict(event)
            event_dict['start_time'] = event.start_time.isoformat()
            if event.end_time:
                event_dict['end_time'] = event.end_time.isoformat()
            result.append(event_dict)
        return result

    def get_scheduled_events(self) -> List[Dict[str, Any]]:
        """Get all scheduled world events"""
        events = self.create_sample_events()
        scheduled_events = [e for e in events if e.status == "scheduled"]

        # Convert datetime objects to ISO strings for JSON serialization
        result = []
        for event in scheduled_events:
            event_dict = asdict(event)
            event_dict['start_time'] = event.start_time.isoformat()
            if event.end_time:
                event_dict['end_time'] = event.end_time.isoformat()
            result.append(event_dict)
        return result

    def get_events_by_region(self, region: str) -> List[Dict[str, Any]]:
        """Get events affecting a specific region"""
        events = self.create_sample_events()
        region_events = [e for e in events if region in e.affected_regions]

        # Convert datetime objects to ISO strings for JSON serialization
        result = []
        for event in region_events:
            event_dict = asdict(event)
            event_dict['start_time'] = event.start_time.isoformat()
            if event.end_time:
                event_dict['end_time'] = event.end_time.isoformat()
            result.append(event_dict)
        return result

    def trigger_event(self, event_id: str) -> Dict[str, Any]:
        """Trigger a specific world event"""
        events = self.create_sample_events()
        event = next((e for e in events if e.id == event_id), None)

        if not event:
            return {"error": "Event not found"}

        event.status = "active"
        event.start_time = datetime.now()

        logger.info(f"Triggered world event: {event.name}")

        return {
            "event_id": event_id,
            "status": "triggered",
            "message": f"World event '{event.name}' has been activated",
            "effects": event.effects,
            "rewards": event.rewards
        }

    def get_event_statistics(self) -> Dict[str, Any]:
        """Get world events statistics"""
        events = self.create_sample_events()

        stats = {
            "total_events": len(events),
            "active_events": len([e for e in events if e.status == "active"]),
            "scheduled_events": len([e for e in events if e.status == "scheduled"]),
            "completed_events": len([e for e in events if e.status == "completed"]),
            "events_by_type": {},
            "events_by_severity": {},
            "most_affected_region": "america"
        }

        # Count by type and severity
        for event in events:
            stats["events_by_type"][event.event_type] = stats["events_by_type"].get(event.event_type, 0) + 1
            stats["events_by_severity"][event.severity] = stats["events_by_severity"].get(event.severity, 0) + 1

        return stats

class WorldEventsAPIServer:
    """HTTP API server for world events service"""

    def __init__(self, service: WorldEventsService, port: int = 8086):
        self.service = service
        self.port = port

    async def create_app(self):
        """Create aiohttp application"""
        from aiohttp import web

        app = web.Application()

        async def health_handler(request):
            return web.json_response({"status": "healthy", "service": "world-events"})

        async def active_events_handler(request):
            events = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_active_events
            )
            return web.json_response({"active_events": events})

        async def scheduled_events_handler(request):
            events = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_scheduled_events
            )
            return web.json_response({"scheduled_events": events})

        async def region_events_handler(request):
            region = request.match_info.get('region', 'america')
            events = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_events_by_region, region
            )
            return web.json_response({"region": region, "events": events})

        async def trigger_event_handler(request):
            event_id = request.match_info.get('event_id')
            if not event_id:
                return web.json_response({"error": "Event ID required"}, status=400)

            result = await asyncio.get_event_loop().run_in_executor(
                None, self.service.trigger_event, event_id
            )
            return web.json_response(result)

        async def statistics_handler(request):
            stats = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_event_statistics
            )
            return web.json_response({"statistics": stats})

        app.router.add_get('/health', health_handler)
        app.router.add_get('/api/v1/events/active', active_events_handler)
        app.router.add_get('/api/v1/events/scheduled', scheduled_events_handler)
        app.router.add_get('/api/v1/events/region/{region}', region_events_handler)
        app.router.add_post('/api/v1/events/{event_id}/trigger', trigger_event_handler)
        app.router.add_get('/api/v1/events/statistics', statistics_handler)

        return app

    async def run_server(self):
        """Run the HTTP server"""
        app = await self.create_app()
        runner = aiohttp.web.AppRunner(app)
        await runner.setup()

        site = aiohttp.web.TCPSite(runner, 'localhost', self.port)
        await site.start()

        logger.info(f"World Events API Server started on http://localhost:{self.port}")
        logger.info("Endpoints:")
        logger.info(f"  GET  /health")
        logger.info(f"  GET  /api/v1/events/active")
        logger.info(f"  GET  /api/v1/events/scheduled")
        logger.info(f"  GET  /api/v1/events/region/{{region}}")
        logger.info(f"  POST /api/v1/events/{{event_id}}/trigger")
        logger.info(f"  GET  /api/v1/events/statistics")

        # Keep server running
        try:
            while True:
                await asyncio.sleep(1)
        except KeyboardInterrupt:
            logger.info("Shutting down server...")
            await runner.cleanup()

async def main():
    """Main function"""
    print("=== NECPGAME World Events Service ===")

    # Initialize service
    service = WorldEventsService()

    # Create sample events
    events = service.create_sample_events()
    print(f"[OK] Created {len(events)} sample world events")

    # Start API server
    server = WorldEventsAPIServer(service, port=8086)

    try:
        await server.run_server()
    except KeyboardInterrupt:
        print("\n[INFO] World Events service stopped")
    except Exception as e:
        print(f"[ERROR] Server error: {e}")
        return 1

    return 0

if __name__ == '__main__':
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n[INFO] World Events service stopped")
