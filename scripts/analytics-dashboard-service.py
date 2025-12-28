#!/usr/bin/env python3
"""
NECPGAME Analytics Dashboard Service
Real-time game analytics and player behavior tracking

Features:
- Player activity monitoring
- Game session analytics
- Performance metrics collection
- Real-time dashboard data
- Player behavior patterns
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
class AnalyticsMetrics:
    """Real-time analytics metrics"""
    active_players: int = 0
    total_sessions: int = 0
    avg_session_duration: float = 0.0
    peak_concurrent_users: int = 0
    player_retention_rate: float = 0.0
    popular_game_modes: Dict[str, int] = None
    revenue_metrics: Dict[str, float] = None
    error_rate: float = 0.0
    performance_metrics: Dict[str, float] = None

    def __post_init__(self):
        if self.popular_game_modes is None:
            self.popular_game_modes = {}
        if self.revenue_metrics is None:
            self.revenue_metrics = {"daily": 0.0, "monthly": 0.0}
        if self.performance_metrics is None:
            self.performance_metrics = {"avg_response_time": 0.0, "error_rate": 0.0}

class AnalyticsDashboardService:
    """Main analytics dashboard service"""

    def __init__(self):
        self.db_config = {
            'host': 'localhost',
            'port': 5432,
            'database': 'necpgame',
            'user': 'postgres',
            'password': 'postgres'
        }
        self.metrics = AnalyticsMetrics()
        self.executor = ThreadPoolExecutor(max_workers=4)

    def get_db_connection(self):
        """Get database connection"""
        try:
            return psycopg2.connect(**self.db_config)
        except Exception as e:
            logger.error(f"Database connection failed: {e}")
            return None

    def collect_realtime_metrics(self) -> Dict[str, Any]:
        """Collect real-time analytics metrics"""
        conn = self.get_db_connection()
        if not conn:
            return {}

        try:
            with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
                # Active players in last 5 minutes
                cur.execute("""
                    SELECT COUNT(DISTINCT player_id) as active_players
                    FROM gameplay.player_sessions
                    WHERE start_time > NOW() - INTERVAL '5 minutes'
                """)
                active_result = cur.fetchone()
                active_players = active_result['active_players'] if active_result else 0

                # Total sessions today
                cur.execute("""
                    SELECT COUNT(*) as total_sessions
                    FROM gameplay.player_sessions
                    WHERE DATE(start_time) = CURRENT_DATE
                """)
                session_result = cur.fetchone()
                total_sessions = session_result['total_sessions'] if session_result else 0

                # Average session duration
                cur.execute("""
                    SELECT AVG(EXTRACT(EPOCH FROM (end_time - start_time))) as avg_duration
                    FROM gameplay.player_sessions
                    WHERE end_time IS NOT NULL
                    AND DATE(start_time) = CURRENT_DATE
                """)
                duration_result = cur.fetchone()
                avg_duration = duration_result['avg_duration'] if duration_result and duration_result['avg_duration'] else 0

                # Popular game modes
                cur.execute("""
                    SELECT game_mode, COUNT(*) as count
                    FROM gameplay.player_sessions
                    WHERE DATE(start_time) = CURRENT_DATE
                    GROUP BY game_mode
                    ORDER BY count DESC
                    LIMIT 5
                """)
                mode_results = cur.fetchall()
                popular_modes = {row['game_mode']: row['count'] for row in mode_results}

                # Peak concurrent users
                cur.execute("""
                    SELECT MAX(concurrent_users) as peak_users
                    FROM analytics.concurrent_users_log
                    WHERE timestamp > NOW() - INTERVAL '24 hours'
                """)
                peak_result = cur.fetchone()
                peak_users = peak_result['peak_users'] if peak_result and peak_result['peak_users'] else 0

                # Error rate
                cur.execute("""
                    SELECT
                        COUNT(CASE WHEN error_occurred THEN 1 END)::float /
                        NULLIF(COUNT(*), 0) * 100 as error_rate
                    FROM gameplay.player_sessions
                    WHERE DATE(start_time) = CURRENT_DATE
                """)
                error_result = cur.fetchone()
                error_rate = error_result['error_rate'] if error_result and error_result['error_rate'] else 0

                # Performance metrics
                cur.execute("""
                    SELECT
                        AVG(response_time) as avg_response_time,
                        COUNT(CASE WHEN response_time > 100 THEN 1 END)::float /
                        NULLIF(COUNT(*), 0) * 100 as slow_requests_rate
                    FROM analytics.performance_metrics
                    WHERE timestamp > NOW() - INTERVAL '1 hour'
                """)
                perf_result = cur.fetchone()
                perf_metrics = {
                    "avg_response_time": perf_result['avg_response_time'] if perf_result and perf_result['avg_response_time'] else 0,
                    "slow_requests_rate": perf_result['slow_requests_rate'] if perf_result and perf_result['slow_requests_rate'] else 0
                }

                return {
                    "timestamp": datetime.now().isoformat(),
                    "active_players": active_players,
                    "total_sessions_today": total_sessions,
                    "avg_session_duration_minutes": avg_duration / 60 if avg_duration else 0,
                    "peak_concurrent_users": peak_users,
                    "popular_game_modes": popular_modes,
                    "error_rate_percent": error_rate,
                    "performance_metrics": perf_metrics,
                    "revenue_metrics": {"daily": 0.0, "monthly": 0.0}  # Placeholder
                }

        except Exception as e:
            logger.error(f"Failed to collect metrics: {e}")
            return {}
        finally:
            conn.close()

    def get_player_behavior_analytics(self) -> Dict[str, Any]:
        """Get detailed player behavior analytics"""
        conn = self.get_db_connection()
        if not conn:
            return {}

        try:
            with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
                # Player retention analysis
                cur.execute("""
                    SELECT
                        COUNT(DISTINCT CASE WHEN last_login > NOW() - INTERVAL '1 day' THEN player_id END) as daily_active,
                        COUNT(DISTINCT CASE WHEN last_login > NOW() - INTERVAL '7 days' THEN player_id END) as weekly_active,
                        COUNT(DISTINCT CASE WHEN last_login > NOW() - INTERVAL '30 days' THEN player_id END) as monthly_active
                    FROM social.players
                """)
                retention_result = cur.fetchone()

                # Top activities
                cur.execute("""
                    SELECT activity_type, COUNT(*) as count
                    FROM analytics.player_activities
                    WHERE timestamp > NOW() - INTERVAL '24 hours'
                    GROUP BY activity_type
                    ORDER BY count DESC
                    LIMIT 10
                """)
                activity_results = cur.fetchall()
                top_activities = {row['activity_type']: row['count'] for row in activity_results}

                # Geographic distribution
                cur.execute("""
                    SELECT region, COUNT(*) as players
                    FROM social.players
                    GROUP BY region
                    ORDER BY players DESC
                    LIMIT 10
                """)
                geo_results = cur.fetchall()
                geo_distribution = {row['region']: row['players'] for row in geo_results}

                return {
                    "retention": {
                        "daily_active_users": retention_result['daily_active'] if retention_result else 0,
                        "weekly_active_users": retention_result['weekly_active'] if retention_result else 0,
                        "monthly_active_users": retention_result['monthly_active'] if retention_result else 0
                    },
                    "top_activities": top_activities,
                    "geographic_distribution": geo_distribution,
                    "engagement_metrics": {
                        "avg_daily_playtime": 0.0,  # Placeholder
                        "avg_session_length": 0.0,   # Placeholder
                        "feature_adoption_rate": 0.0 # Placeholder
                    }
                }

        except Exception as e:
            logger.error(f"Failed to get behavior analytics: {e}")
            return {}
        finally:
            conn.close()

    def get_dashboard_data(self) -> Dict[str, Any]:
        """Get complete dashboard data"""
        realtime_metrics = self.collect_realtime_metrics()
        behavior_analytics = self.get_player_behavior_analytics()

        return {
            "realtime_metrics": realtime_metrics,
            "behavior_analytics": behavior_analytics,
            "system_health": {
                "database_status": "healthy" if realtime_metrics else "unhealthy",
                "last_updated": datetime.now().isoformat(),
                "version": "1.0.0"
            }
        }

    def export_dashboard_report(self, filename: str = None) -> str:
        """Export dashboard data to JSON file"""
        if not filename:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            filename = f"analytics_dashboard_report_{timestamp}.json"

        data = self.get_dashboard_data()

        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2, ensure_ascii=False)

        logger.info(f"Dashboard report exported to {filename}")
        return filename

class AnalyticsAPIServer:
    """Simple HTTP API server for analytics dashboard"""

    def __init__(self, service: AnalyticsDashboardService, port: int = 8085):
        self.service = service
        self.port = port
        self.app = None

    async def create_app(self):
        """Create aiohttp application"""
        from aiohttp import web

        app = web.Application()

        async def health_handler(request):
            return web.json_response({"status": "healthy", "service": "analytics-dashboard"})

        async def dashboard_handler(request):
            data = await asyncio.get_event_loop().run_in_executor(
                None, self.service.get_dashboard_data
            )
            return web.json_response(data)

        async def export_handler(request):
            filename = await asyncio.get_event_loop().run_in_executor(
                None, self.service.export_dashboard_report
            )
            return web.json_response({"status": "exported", "filename": filename})

        app.router.add_get('/health', health_handler)
        app.router.add_get('/api/v1/dashboard', dashboard_handler)
        app.router.add_post('/api/v1/dashboard/export', export_handler)

        self.app = app
        return app

    async def run_server(self):
        """Run the HTTP server"""
        app = await self.create_app()
        runner = aiohttp.web.AppRunner(app)
        await runner.setup()

        site = aiohttp.web.TCPSite(runner, 'localhost', self.port)
        await site.start()

        logger.info(f"Analytics Dashboard API Server started on http://localhost:{self.port}")
        logger.info("Endpoints:")
        logger.info(f"  GET  /health")
        logger.info(f"  GET  /api/v1/dashboard")
        logger.info(f"  POST /api/v1/dashboard/export")

        # Keep server running
        try:
            while True:
                await asyncio.sleep(1)
        except KeyboardInterrupt:
            logger.info("Shutting down server...")
            await runner.cleanup()

async def main():
    """Main function"""
    print("=== NECPGAME Analytics Dashboard Service ===")

    # Initialize service
    service = AnalyticsDashboardService()

    # Test database connection
    conn = service.get_db_connection()
    if not conn:
        print("[ERROR] Cannot connect to database. Please ensure PostgreSQL is running.")
        return 1

    conn.close()
    print("[OK] Database connection established")

    # Start API server
    server = AnalyticsAPIServer(service, port=8085)

    try:
        await server.run_server()
    except KeyboardInterrupt:
        print("\n[INFO] Server shutdown requested")
    except Exception as e:
        print(f"[ERROR] Server error: {e}")
        return 1

    return 0

if __name__ == '__main__':
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n[INFO] Analytics service stopped")
