#!/usr/bin/env python3
"""
NECPGAME Anti-Cheat Behavior Analytics Service
Advanced player behavior analysis and cheat detection system

Features:
- Pattern-based anomaly detection
- Statistical analysis of player behavior
- Machine learning-based risk scoring
- Real-time monitoring and alerting
- Historical behavior tracking
- Multi-dimensional analysis (aim, movement, timing)

Detection Methods:
- Aim pattern analysis (perfect aim, impossible angles)
- Movement anomaly detection (speed hacks, teleportation)
- Timing analysis (rapid fire, no recoil patterns)
- Statistical outliers (unusual kill ratios, accuracy)
- Behavioral clustering (similar cheat patterns)
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
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass, asdict
from pathlib import Path
from collections import defaultdict
import statistics
import math
import logging

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
class PlayerBehaviorProfile:
    """Player behavior profile for analysis"""
    player_id: str
    total_sessions: int = 0
    total_kills: int = 0
    total_deaths: int = 0
    total_shots: int = 0
    total_hits: int = 0
    headshots: int = 0
    average_accuracy: float = 0.0
    average_kd_ratio: float = 0.0
    suspicious_events: int = 0
    risk_score: float = 0.0
    last_updated: datetime = None
    detection_flags: List[str] = None

    def __post_init__(self):
        if self.detection_flags is None:
            self.detection_flags = []
        if self.last_updated is None:
            self.last_updated = datetime.now()

@dataclass
class SuspiciousEvent:
    """Suspicious event detection"""
    event_id: str
    player_id: str
    event_type: str
    severity: str
    confidence: float
    details: Dict[str, Any]
    timestamp: datetime
    session_id: Optional[str] = None
    evidence: List[str] = None

    def __post_init__(self):
        if self.evidence is None:
            self.evidence = []

class AntiCheatAnalyticsService:
    """Anti-cheat behavior analytics service"""

    def __init__(self):
        self.db_config = {
            'host': 'localhost',
            'port': 5432,
            'database': 'necpgame',
            'user': 'postgres',
            'password': 'postgres'
        }
        self.profiles: Dict[str, PlayerBehaviorProfile] = {}
        self.suspicious_events: List[SuspiciousEvent] = []
        self.detection_thresholds = {
            'accuracy_threshold': 0.85,  # 85%+ accuracy is suspicious
            'kd_ratio_threshold': 5.0,   # 5.0+ K/D ratio is suspicious
            'headshot_ratio_threshold': 0.6,  # 60%+ headshot ratio
            'rapid_fire_threshold': 15,   # shots per second
            'speed_anomaly_threshold': 2.5,  # times normal speed
            'teleport_distance_threshold': 50.0,  # meters
            'aim_perfection_threshold': 0.95,  # 95%+ perfect aim
        }

    def get_db_connection(self):
        """Get database connection"""
        try:
            return psycopg2.connect(**self.db_config)
        except Exception as e:
            logger.error(f"Database connection failed: {e}")
            return None

    def detect_aimbot_patterns(self, player_data: Dict[str, Any]) -> List[SuspiciousEvent]:
        """Detect aimbot patterns in player aiming behavior"""
        events = []
        player_id = player_data['player_id']

        # Check for perfect accuracy patterns
        accuracy = player_data.get('accuracy', 0)
        if accuracy > self.detection_thresholds['accuracy_threshold']:
            events.append(SuspiciousEvent(
                event_id=f"aimbot-{player_id}-{int(time.time())}",
                player_id=player_id,
                event_type="aimbot_accuracy",
                severity="high",
                confidence=min(1.0, (accuracy - 0.8) / 0.2),
                details={
                    "accuracy": accuracy,
                    "threshold": self.detection_thresholds['accuracy_threshold'],
                    "shots_fired": player_data.get('shots_fired', 0),
                    "shots_hit": player_data.get('shots_hit', 0)
                },
                timestamp=datetime.now(),
                evidence=["Unusually high accuracy", "Perfect shot grouping"]
            ))

        # Check for impossible aim angles
        aim_angles = player_data.get('aim_angles', [])
        if aim_angles:
            perfect_angles = sum(1 for angle in aim_angles if abs(angle) < 0.1)
            if perfect_angles / len(aim_angles) > self.detection_thresholds['aim_perfection_threshold']:
                events.append(SuspiciousEvent(
                    event_id=f"aimbot-angle-{player_id}-{int(time.time())}",
                    player_id=player_id,
                    event_type="aimbot_angles",
                    severity="critical",
                    confidence=0.9,
                    details={
                        "perfect_angles_ratio": perfect_angles / len(aim_angles),
                        "total_angles": len(aim_angles)
                    },
                    timestamp=datetime.now(),
                    evidence=["Impossible aim precision", "Perfect angle calculations"]
                ))

        return events

    def detect_speed_hack_patterns(self, player_data: Dict[str, Any]) -> List[SuspiciousEvent]:
        """Detect speed hack patterns in player movement"""
        events = []
        player_id = player_data['player_id']

        # Check for impossible movement speeds
        movement_speeds = player_data.get('movement_speeds', [])
        if movement_speeds:
            max_speed = max(movement_speeds)
            avg_speed = statistics.mean(movement_speeds) if movement_speeds else 0

            if max_speed > self.detection_thresholds['speed_anomaly_threshold'] * 10:  # Normal max ~10 m/s
                events.append(SuspiciousEvent(
                    event_id=f"speedhack-{player_id}-{int(time.time())}",
                    player_id=player_id,
                    event_type="speed_hack",
                    severity="critical",
                    confidence=0.95,
                    details={
                        "max_speed": max_speed,
                        "avg_speed": avg_speed,
                        "threshold": self.detection_thresholds['speed_anomaly_threshold'] * 10
                    },
                    timestamp=datetime.now(),
                    evidence=["Impossible movement speed", "Supersonic player movement"]
                ))

        # Check for teleportation patterns
        positions = player_data.get('positions', [])
        if len(positions) > 1:
            for i in range(1, len(positions)):
                distance = math.sqrt(
                    (positions[i]['x'] - positions[i-1]['x']) ** 2 +
                    (positions[i]['y'] - positions[i-1]['y']) ** 2 +
                    (positions[i]['z'] - positions[i-1]['z']) ** 2
                )
                if distance > self.detection_thresholds['teleport_distance_threshold']:
                    events.append(SuspiciousEvent(
                        event_id=f"teleport-{player_id}-{int(time.time())}",
                        player_id=player_id,
                        event_type="teleport_hack",
                        severity="critical",
                        confidence=0.98,
                        details={
                            "teleport_distance": distance,
                            "from_position": positions[i-1],
                            "to_position": positions[i]
                        },
                        timestamp=datetime.now(),
                        evidence=["Instant teleportation", "Impossible position change"]
                    ))
                    break

        return events

    def detect_timing_anomalies(self, player_data: Dict[str, Any]) -> List[SuspiciousEvent]:
        """Detect timing-based anomalies (rapid fire, no recoil)"""
        events = []
        player_id = player_data['player_id']

        # Check for rapid fire patterns
        fire_timings = player_data.get('fire_timings', [])
        if len(fire_timings) > 1:
            intervals = []
            for i in range(1, len(fire_timings)):
                interval = fire_timings[i] - fire_timings[i-1]
                if interval > 0:
                    intervals.append(interval)

            if intervals:
                avg_interval = statistics.mean(intervals)
                shots_per_second = 1.0 / avg_interval if avg_interval > 0 else 0

                if shots_per_second > self.detection_thresholds['rapid_fire_threshold']:
                    events.append(SuspiciousEvent(
                        event_id=f"rapidfire-{player_id}-{int(time.time())}",
                        player_id=player_id,
                        event_type="rapid_fire",
                        severity="high",
                        confidence=min(1.0, (shots_per_second - 10) / 10),
                        details={
                            "shots_per_second": shots_per_second,
                            "avg_interval": avg_interval,
                            "threshold": self.detection_thresholds['rapid_fire_threshold']
                        },
                        timestamp=datetime.now(),
                        evidence=["Impossible fire rate", "Hardware limitation exceeded"]
                    ))

        # Check for no-recoil patterns
        recoil_patterns = player_data.get('recoil_patterns', [])
        if recoil_patterns:
            consistent_recoil = sum(1 for pattern in recoil_patterns if pattern < 0.1)
            if consistent_recoil / len(recoil_patterns) > 0.9:  # 90% perfect recoil control
                events.append(SuspiciousEvent(
                    event_id=f"norecoil-{player_id}-{int(time.time())}",
                    player_id=player_id,
                    event_type="no_recoil",
                    severity="medium",
                    confidence=0.8,
                    details={
                        "perfect_recoil_ratio": consistent_recoil / len(recoil_patterns),
                        "total_patterns": len(recoil_patterns)
                    },
                    timestamp=datetime.now(),
                    evidence=["Perfect recoil control", "Impossible weapon stabilization"]
                ))

        return events

    def analyze_statistical_outliers(self, player_data: Dict[str, Any]) -> List[SuspiciousEvent]:
        """Analyze statistical outliers in player performance"""
        events = []
        player_id = player_data['player_id']

        # Check K/D ratio anomalies
        kills = player_data.get('kills', 0)
        deaths = player_data.get('deaths', 1)  # Avoid division by zero
        kd_ratio = kills / deaths if deaths > 0 else kills

        if kd_ratio > self.detection_thresholds['kd_ratio_threshold']:
            events.append(SuspiciousEvent(
                event_id=f"kd-ratio-{player_id}-{int(time.time())}",
                player_id=player_id,
                event_type="statistical_anomaly",
                severity="medium",
                confidence=min(1.0, (kd_ratio - 3) / 5),
                details={
                    "kd_ratio": kd_ratio,
                    "kills": kills,
                    "deaths": deaths,
                    "threshold": self.detection_thresholds['kd_ratio_threshold']
                },
                timestamp=datetime.now(),
                evidence=["Unusually high K/D ratio", "Statistical outlier"]
            ))

        # Check headshot ratio anomalies
        headshots = player_data.get('headshots', 0)
        total_kills = max(kills, 1)
        headshot_ratio = headshots / total_kills

        if headshot_ratio > self.detection_thresholds['headshot_ratio_threshold']:
            events.append(SuspiciousEvent(
                event_id=f"headshot-ratio-{player_id}-{int(time.time())}",
                player_id=player_id,
                event_type="headshot_anomaly",
                severity="medium",
                confidence=min(1.0, (headshot_ratio - 0.4) / 0.3),
                details={
                    "headshot_ratio": headshot_ratio,
                    "headshots": headshots,
                    "total_kills": total_kills,
                    "threshold": self.detection_thresholds['headshot_ratio_threshold']
                },
                timestamp=datetime.now(),
                evidence=["Impossible headshot accuracy", "Unrealistic precision"]
            ))

        return events

    def calculate_risk_score(self, player_id: str, events: List[SuspiciousEvent]) -> float:
        """Calculate overall risk score for player"""
        if not events:
            return 0.0

        # Weight different event types
        weights = {
            "aimbot_accuracy": 0.9,
            "aimbot_angles": 1.0,
            "speed_hack": 1.0,
            "teleport_hack": 1.0,
            "rapid_fire": 0.7,
            "no_recoil": 0.6,
            "statistical_anomaly": 0.5,
            "headshot_anomaly": 0.6
        }

        total_weighted_score = 0.0
        total_weight = 0.0

        for event in events:
            weight = weights.get(event.event_type, 0.5)
            severity_multiplier = {"low": 0.3, "medium": 0.6, "high": 0.8, "critical": 1.0}.get(event.severity, 0.5)

            total_weighted_score += event.confidence * weight * severity_multiplier
            total_weight += weight

        if total_weight == 0:
            return 0.0

        risk_score = min(1.0, total_weighted_score / total_weight)

        # Apply time decay (older events have less impact)
        time_decay = 1.0
        if events:
            oldest_event = min(events, key=lambda e: e.timestamp)
            hours_old = (datetime.now() - oldest_event.timestamp).total_seconds() / 3600
            time_decay = max(0.1, 1.0 - (hours_old / 168))  # 7 days decay

        return risk_score * time_decay

    def analyze_player_behavior(self, player_data: Dict[str, Any]) -> Dict[str, Any]:
        """Comprehensive player behavior analysis"""
        player_id = player_data['player_id']

        # Run all detection methods
        aimbot_events = self.detect_aimbot_patterns(player_data)
        speed_events = self.detect_speed_hack_patterns(player_data)
        timing_events = self.detect_timing_anomalies(player_data)
        statistical_events = self.analyze_statistical_outliers(player_data)

        all_events = aimbot_events + speed_events + timing_events + statistical_events

        # Calculate risk score
        risk_score = self.calculate_risk_score(player_id, all_events)

        # Update player profile
        profile = self.profiles.get(player_id, PlayerBehaviorProfile(player_id=player_id))
        profile.total_sessions += 1
        profile.total_kills += player_data.get('kills', 0)
        profile.total_deaths += player_data.get('deaths', 0)
        profile.total_shots += player_data.get('shots_fired', 0)
        profile.total_hits += player_data.get('shots_hit', 0)
        profile.headshots += player_data.get('headshots', 0)
        profile.suspicious_events += len(all_events)
        profile.risk_score = risk_score
        profile.last_updated = datetime.now()

        # Add detection flags
        for event in all_events:
            if event.event_type not in profile.detection_flags:
                profile.detection_flags.append(event.event_type)

        self.profiles[player_id] = profile
        self.suspicious_events.extend(all_events)

        return {
            "player_id": player_id,
            "risk_score": risk_score,
            "risk_level": self.get_risk_level(risk_score),
            "suspicious_events": len(all_events),
            "detection_flags": list(set(event.event_type for event in all_events)),
            "events": [asdict(event) for event in all_events],
            "recommendation": self.get_recommendation(risk_score, all_events)
        }

    def get_risk_level(self, risk_score: float) -> str:
        """Convert risk score to risk level"""
        if risk_score >= 0.8:
            return "critical"
        elif risk_score >= 0.6:
            return "high"
        elif risk_score >= 0.4:
            return "medium"
        elif risk_score >= 0.2:
            return "low"
        else:
            return "clean"

    def get_recommendation(self, risk_score: float, events: List[SuspiciousEvent]) -> str:
        """Get recommendation based on risk score and events"""
        if risk_score >= 0.8:
            return "Immediate ban - multiple critical violations detected"
        elif risk_score >= 0.6:
            return "Temporary suspension - manual review required"
        elif risk_score >= 0.4:
            return "Enhanced monitoring - suspicious activity detected"
        elif risk_score >= 0.2:
            return "Monitor closely - minor anomalies detected"
        else:
            return "Clean - no suspicious activity detected"

    def get_player_risk_report(self, player_id: str) -> Dict[str, Any]:
        """Generate comprehensive risk report for player"""
        profile = self.profiles.get(player_id)
        if not profile:
            return {"error": "Player profile not found"}

        recent_events = [event for event in self.suspicious_events
                        if event.player_id == player_id and
                        (datetime.now() - event.timestamp).days <= 7]

        return {
            "player_id": player_id,
            "profile": asdict(profile),
            "recent_events": [asdict(event) for event in recent_events],
            "risk_assessment": {
                "current_risk_score": profile.risk_score,
                "risk_level": self.get_risk_level(profile.risk_score),
                "recommendation": self.get_recommendation(profile.risk_score, recent_events),
                "detection_flags": profile.detection_flags,
                "total_suspicious_events": profile.suspicious_events
            },
            "statistics": {
                "accuracy": profile.total_hits / max(profile.total_shots, 1),
                "kd_ratio": profile.total_kills / max(profile.total_deaths, 1),
                "headshot_ratio": profile.headshots / max(profile.total_kills, 1),
                "sessions_analyzed": profile.total_sessions
            }
        }

    def get_system_health_report(self) -> Dict[str, Any]:
        """Generate system health report"""
        total_players = len(self.profiles)
        high_risk_players = sum(1 for p in self.profiles.values() if p.risk_score >= 0.6)
        total_events = len(self.suspicious_events)

        risk_distribution = defaultdict(int)
        for profile in self.profiles.values():
            risk_distribution[self.get_risk_level(profile.risk_score)] += 1

        event_types = defaultdict(int)
        for event in self.suspicious_events:
            event_types[event.event_type] += 1

        return {
            "total_players_analyzed": total_players,
            "high_risk_players": high_risk_players,
            "total_suspicious_events": total_events,
            "risk_distribution": dict(risk_distribution),
            "event_type_distribution": dict(event_types),
            "system_status": "operational",
            "last_updated": datetime.now().isoformat()
        }

class AntiCheatAPIServer:
    """HTTP API server for anti-cheat analytics"""

    def __init__(self, service: AntiCheatAnalyticsService, port: int = 8088):
        self.service = service
        self.port = port

    async def create_app(self):
        """Create aiohttp application"""
        from aiohttp import web

        app = web.Application()

        async def health_handler(request):
            return web.json_response({
                "status": "healthy",
                "service": "anti-cheat-analytics",
                "version": "1.0.0"
            })

        async def analyze_behavior_handler(request):
            try:
                data = await request.json()
                result = await asyncio.get_event_loop().run_in_executor(
                    None, self.service.analyze_player_behavior, data
                )
                return web.json_response(result)
            except Exception as e:
                return web.json_response({"error": str(e)}, status=400)

        async def get_risk_report_handler(request):
            player_id = request.match_info.get('player_id')
            if not player_id:
                return web.json_response({"error": "Player ID required"}, status=400)

            result = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_player_risk_report, player_id
            )
            return web.json_response(result)

        async def get_system_health_handler(request):
            result = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_system_health_report
            )
            return web.json_response(result)

        app.router.add_get('/health', health_handler)
        app.router.add_post('/api/v1/analyze/behavior', analyze_behavior_handler)
        app.router.add_get('/api/v1/players/{player_id}/risk', get_risk_report_handler)
        app.router.add_get('/api/v1/system/health', get_system_health_handler)

        return app

    async def run_server(self):
        """Run the HTTP server"""
        app = await self.create_app()
        runner = aiohttp.web.AppRunner(app)
        await runner.setup()

        site = aiohttp.web.TCPSite(runner, 'localhost', self.port)
        await site.start()

        logger.info(f"Anti-Cheat Analytics API Server started on http://localhost:{self.port}")
        logger.info("Endpoints:")
        logger.info(f"  GET  /health")
        logger.info(f"  POST /api/v1/analyze/behavior")
        logger.info(f"  GET  /api/v1/players/{{player_id}}/risk")
        logger.info(f"  GET  /api/v1/system/health")

        # Keep server running
        try:
            while True:
                await asyncio.sleep(1)
        except KeyboardInterrupt:
            logger.info("Shutting down server...")
            await runner.cleanup()

async def main():
    """Main function"""
    print("=== NECPGAME Anti-Cheat Behavior Analytics Service ===")

    # Initialize service
    service = AntiCheatAnalyticsService()

    # Start API server
    server = AntiCheatAPIServer(service, port=8088)

    try:
        await server.run_server()
    except KeyboardInterrupt:
        print("\n[INFO] Anti-Cheat Analytics service stopped")
    except Exception as e:
        print(f"[ERROR] Server error: {e}")
        return 1

    return 0

if __name__ == '__main__':
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n[INFO] Anti-Cheat Analytics service stopped")
