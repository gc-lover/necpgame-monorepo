#!/usr/bin/env python3
# Issue: #1858
# CI/CD Performance Analysis Tool

import os
import json
import subprocess
import sys
from datetime import datetime, timedelta
from pathlib import Path
from typing import Dict, List, Any
import argparse

class CIPerformanceAnalyzer:
    """Analyze CI/CD pipeline performance and suggest optimizations"""

    def __init__(self, repo_path: str = "."):
        self.repo_path = Path(repo_path)
        self.github_token = os.getenv('GITHUB_TOKEN')
        self.results = {}

    def analyze_workflow_runs(self, days: int = 7) -> Dict[str, Any]:
        """Analyze recent workflow runs performance"""
        print(f"📊 Analyzing CI/CD performance for last {days} days...")

        if not self.github_token:
            print("WARNING  GITHUB_TOKEN not found, using local git analysis")
            return self._analyze_local_performance(days)

        # Use GitHub CLI for workflow analysis
        try:
            cmd = [
                "gh", "run", "list",
                "--limit", "100",
                "--json", "name,status,conclusion,createdAt,updatedAt,duration",
                "--jq", f'[.[] | select(.createdAt >= "{(datetime.now() - timedelta(days=days)).isoformat()}Z")]'
            ]

            result = subprocess.run(cmd, capture_output=True, text=True, cwd=self.repo_path)
            if result.returncode == 0:
                runs = json.loads(result.stdout)
                return self._process_workflow_runs(runs)
            else:
                print(f"WARNING  GitHub CLI failed: {result.stderr}")
                return self._analyze_local_performance(days)

        except Exception as e:
            print(f"WARNING  Error analyzing workflows: {e}")
            return self._analyze_local_performance(days)

    def _analyze_local_performance(self, days: int) -> Dict[str, Any]:
        """Fallback analysis using local git data"""
        print("🔍 Using local git analysis...")

        # Analyze git history for CI-related changes
        try:
            # Get recent commits
            cmd = ["git", "log", f"--since='{days} days ago'", "--oneline", "--no-merges"]
            result = subprocess.run(cmd, capture_output=True, text=True, cwd=self.repo_path)

            commits = len(result.stdout.strip().split('\n')) if result.stdout.strip() else 0

            # Analyze file changes
            cmd = ["git", "diff", "--stat", f"HEAD~{min(commits, 50)}..HEAD"]
            result = subprocess.run(cmd, capture_output=True, text=True, cwd=self.repo_path)

            return {
                "commits_analyzed": commits,
                "file_changes": len(result.stdout.split('\n')) if result.stdout else 0,
                "analysis_type": "local_git",
                "recommendations": [
                    "Consider enabling GitHub CLI for detailed workflow analysis",
                    "Monitor build times and failure rates",
                    "Implement caching for dependencies"
                ]
            }

        except Exception as e:
            return {
                "error": str(e),
                "analysis_type": "failed"
            }

    def _process_workflow_runs(self, runs: List[Dict]) -> Dict[str, Any]:
        """Process GitHub workflow runs data"""
        if not runs:
            return {"workflows": [], "summary": "No workflow runs found"}

        # Group by workflow name
        workflows = {}
        for run in runs:
            name = run.get('name', 'unknown')
            if name not in workflows:
                workflows[name] = []

            workflows[name].append({
                'status': run.get('status'),
                'conclusion': run.get('conclusion'),
                'duration': run.get('duration', 0),
                'created_at': run.get('createdAt'),
                'updated_at': run.get('updatedAt')
            })

        # Calculate statistics
        summary = {}
        for name, runs_list in workflows.items():
            total_runs = len(runs_list)
            successful_runs = len([r for r in runs_list if r['conclusion'] == 'success'])
            failed_runs = len([r for r in runs_list if r['conclusion'] == 'failure'])

            durations = [r['duration'] for r in runs_list if r['duration'] > 0]
            avg_duration = sum(durations) / len(durations) if durations else 0

            summary[name] = {
                'total_runs': total_runs,
                'success_rate': successful_runs / total_runs if total_runs > 0 else 0,
                'failure_rate': failed_runs / total_runs if total_runs > 0 else 0,
                'avg_duration_seconds': avg_duration,
                'performance_score': self._calculate_performance_score(successful_runs, total_runs, avg_duration)
            }

        return {
            'workflows': workflows,
            'summary': summary,
            'total_runs': len(runs),
            'analysis_type': 'github_api'
        }

    def _calculate_performance_score(self, successes: int, total: int, avg_duration: float) -> float:
        """Calculate performance score (0-100)"""
        if total == 0:
            return 0.0

        success_rate = successes / total

        # Duration score (lower duration = higher score)
        # Assuming good duration is < 300 seconds (5 minutes)
        duration_score = max(0, min(100, 300 / max(avg_duration, 1) * 100))

        # Weighted score: 70% success rate, 30% duration
        return (success_rate * 70) + (duration_score * 0.3)

    def analyze_docker_performance(self) -> Dict[str, Any]:
        """Analyze Docker build performance"""
        print("🐳 Analyzing Docker performance...")

        dockerfiles = list(self.repo_path.rglob("Dockerfile*"))
        results = {}

        for dockerfile in dockerfiles:
            try:
                with open(dockerfile, 'r') as f:
                    content = f.read()

                # Analyze Dockerfile for optimization opportunities
                issues = []
                optimizations = []

                # Check for multi-stage builds
                if "FROM" in content:
                    from_count = content.count("FROM ")
                    if from_count > 1:
                        optimizations.append("Multi-stage build detected - good!")
                    else:
                        issues.append("Consider using multi-stage builds")

                # Check for apt cache cleanup
                if "apt-get" in content and "rm -rf /var/lib/apt/lists/*" not in content:
                    issues.append("Missing apt cache cleanup")

                # Check for large COPY commands
                copy_lines = [line for line in content.split('\n') if line.strip().startswith('COPY')]
                for line in copy_lines:
                    if len(line.split()) > 3:  # Multiple files copied together
                        optimizations.append("Consider using .dockerignore for efficient COPY")

                results[str(dockerfile.relative_to(self.repo_path))] = {
                    'issues': issues,
                    'optimizations': optimizations,
                    'stage_count': from_count if 'from_count' in locals() else 1
                }

            except Exception as e:
                results[str(dockerfile.relative_to(self.repo_path))] = {
                    'error': str(e)
                }

        return results

    def generate_recommendations(self, analysis_results: Dict[str, Any]) -> List[str]:
        """Generate optimization recommendations"""
        recommendations = []

        # Workflow recommendations
        if 'summary' in analysis_results.get('workflows', {}):
            summary = analysis_results['workflows']['summary']
            for workflow_name, stats in summary.items():
                if stats.get('avg_duration_seconds', 0) > 600:  # 10 minutes
                    recommendations.append(f"⚡ {workflow_name}: High build time ({stats['avg_duration_seconds']:.1f}s) - consider caching")

                if stats.get('success_rate', 1) < 0.9:
                    recommendations.append(f"🔴 {workflow_name}: Low success rate ({stats['success_rate']:.1%}) - investigate failures")

                if stats.get('performance_score', 100) < 70:
                    recommendations.append(f"📊 {workflow_name}: Poor performance score ({stats['performance_score']:.1f}) - optimize pipeline")

        # Docker recommendations
        docker_results = analysis_results.get('docker', {})
        for dockerfile, analysis in docker_results.items():
            if analysis.get('issues'):
                recommendations.extend([f"🐳 {dockerfile}: {issue}" for issue in analysis['issues']])

        # General recommendations
        recommendations.extend([
            "🚀 Consider using GitHub Actions cache for Go modules and dependencies",
            "⚡ Implement parallel job execution where possible",
            "📈 Add performance monitoring and alerting",
            "🔄 Use matrix builds for multi-environment testing",
            "💾 Implement artifact caching and reuse"
        ])

        return recommendations

    def run_analysis(self, days: int = 7) -> Dict[str, Any]:
        """Run complete CI/CD performance analysis"""
        results = {
            'workflows': self.analyze_workflow_runs(days),
            'docker': self.analyze_docker_performance(),
            'timestamp': datetime.now().isoformat(),
            'analysis_period_days': days
        }

        results['recommendations'] = self.generate_recommendations(results)

        return results

    def print_report(self, results: Dict[str, Any]):
        """Print formatted analysis report"""
        print("\n" + "="*80)
        print("🚀 CI/CD Performance Analysis Report")
        print("="*80)
        print(f"📅 Analysis Period: {results['analysis_period_days']} days")
        print(f"🕒 Generated: {results['timestamp']}")

        # Workflow summary
        workflows = results.get('workflows', {})
        if workflows.get('summary'):
            print(f"\n🔄 Workflow Performance:")
            print("-" * 40)
            for name, stats in workflows['summary'].items():
                success_rate = stats.get('success_rate', 0)
                avg_duration = stats.get('avg_duration_seconds', 0)
                score = stats.get('performance_score', 0)

                status = "OK" if score > 80 else "WARNING" if score > 60 else "🔴"
                print(f"{status} {name}")
                print(".1f"                   ".1f"        else:
            print("   No workflow data available")

        # Docker analysis
        docker = results.get('docker', {})
        if docker:
            print(f"\n🐳 Docker Optimization Opportunities:")
            print("-" * 40)
            for dockerfile, analysis in docker.items():
                if analysis.get('issues'):
                    print(f"📋 {dockerfile}:")
                    for issue in analysis['issues']:
                        print(f"   WARNING  {issue}")
                if analysis.get('optimizations'):
                    for opt in analysis['optimizations']:
                        print(f"   OK {opt}")

        # Recommendations
        recommendations = results.get('recommendations', [])
        if recommendations:
            print(f"\n💡 Recommendations:")
            print("-" * 40)
            for rec in recommendations[:10]:  # Show top 10
                print(f"   {rec}")

        print("\n" + "="*80)

def main():
    parser = argparse.ArgumentParser(description="CI/CD Performance Analyzer")
    parser.add_argument("--days", type=int, default=7, help="Analysis period in days")
    parser.add_argument("--output", type=str, help="Output JSON file")
    parser.add_argument("--quiet", action="store_true", help="Quiet mode")

    args = parser.parse_args()

    analyzer = CIPerformanceAnalyzer()
    results = analyzer.run_analysis(args.days)

    if not args.quiet:
        analyzer.print_report(results)

    if args.output:
        with open(args.output, 'w') as f:
            json.dump(results, f, indent=2, default=str)
        print(f"📄 Report saved to {args.output}")

    # Exit with success
    sys.exit(0)

if __name__ == "__main__":
    main()