#!/usr/bin/env python3
# Issue: #1858
# Docker Security and Optimization Validator

import argparse
import json
import os
import re
import subprocess
import sys
from pathlib import Path
from typing import Dict, List, Any, Tuple


class DockerSecurityValidator:
    """Validate Docker images for security and optimization"""

    def __init__(self, project_root: str = "."):
        self.project_root = Path(project_root)
        self.issues: List[str] = []
        self.warnings: List[str] = []
        self.optimizations: List[str] = []

    def validate_all_dockerfiles(self) -> Dict[str, Any]:
        """Validate all Dockerfiles in the project"""
        print("🔍 Scanning for Dockerfiles...")

        dockerfiles = list(self.project_root.rglob("Dockerfile*"))
        results = {}

        for dockerfile in dockerfiles:
            print(f"📋 Analyzing {dockerfile.relative_to(self.project_root)}")
            results[str(dockerfile.relative_to(self.project_root))] = self._analyze_dockerfile(dockerfile)

        return {
            'dockerfiles': results,
            'summary': self._generate_summary(results),
            'issues': self.issues,
            'warnings': self.warnings,
            'optimizations': self.optimizations
        }

    def _analyze_dockerfile(self, dockerfile_path: Path) -> Dict[str, Any]:
        """Analyze a single Dockerfile"""
        try:
            with open(dockerfile_path, 'r') as f:
                content = f.read()
        except Exception as e:
            return {'error': f'Cannot read file: {e}'}

        analysis = {
            'security_issues': [],
            'optimization_opportunities': [],
            'best_practices': [],
            'metrics': {}
        }

        lines = content.split('\n')

        # Check for multi-stage builds
        from_lines = [i for i, line in enumerate(lines) if line.upper().strip().startswith('FROM ')]
        analysis['metrics']['stages'] = len(from_lines)

        if len(from_lines) > 1:
            analysis['best_practices'].append("Multi-stage build detected")
        else:
            analysis['optimization_opportunities'].append("Consider using multi-stage builds to reduce image size")

        # Check for root user usage
        user_lines = [line for line in lines if line.upper().strip().startswith('USER ')]
        if not user_lines:
            analysis['security_issues'].append("No USER directive found - running as root")
        else:
            # Check if user is not root
            user_match = re.search(r'USER\s+(\w+)', '\n'.join(user_lines), re.IGNORECASE)
            if user_match and user_match.group(1).lower() in ['root', '0']:
                analysis['security_issues'].append("Running as root user")

        # Check for apt cache cleanup
        apt_get_lines = [line for line in lines if 'apt-get' in line.lower()]
        if apt_get_lines:
            has_cleanup = any('rm -rf /var/lib/apt/lists/*' in line for line in lines)
            if not has_cleanup:
                analysis['optimization_opportunities'].append(
                    "Missing apt cache cleanup - add 'rm -rf /var/lib/apt/lists/*'")

        # Check for unnecessary packages
        curl_wget = any('curl' in line.lower() or 'wget' in line.lower() for line in lines)
        if curl_wget:
            analysis['warnings'].append("curl/wget found - ensure removal after use in multi-stage builds")

        # Check for large COPY commands
        copy_lines = [line for line in lines if
                      line.upper().strip().startswith('COPY ') or line.upper().strip().startswith('ADD ')]
        large_copies = []
        for line in copy_lines:
            # Simple heuristic: multiple files or . in path
            if len(line.split()) > 3 or ' .' in line or './' in line:
                large_copies.append(line.strip())

        if large_copies:
            analysis['optimization_opportunities'].extend([
                f"Large COPY detected: {copy[:50]}..."
                for copy in large_copies[:3]  # Show first 3
            ])

        # Check for health checks
        has_healthcheck = any(line.upper().strip().startswith('HEALTHCHECK ') for line in lines)
        if not has_healthcheck:
            analysis['optimization_opportunities'].append("Consider adding HEALTHCHECK for container monitoring")

        # Check for exposed ports
        expose_lines = [line for line in lines if line.upper().strip().startswith('EXPOSE ')]
        if not expose_lines:
            analysis['warnings'].append("No EXPOSE directives found")

        # Calculate basic metrics
        analysis['metrics']['lines'] = len(lines)
        analysis['metrics']['copy_commands'] = len(copy_lines)
        analysis['metrics']['run_commands'] = len([line for line in lines if line.upper().strip().startswith('RUN ')])

        # Update global lists
        self.issues.extend([f"{dockerfile_path.name}: {issue}" for issue in analysis['security_issues']])
        self.warnings.extend([f"{dockerfile_path.name}: {warning}" for warning in analysis.get('warnings', [])])
        self.optimizations.extend([f"{dockerfile_path.name}: {opt}" for opt in analysis['optimization_opportunities']])

        return analysis

    def _generate_summary(self, results: Dict[str, Any]) -> Dict[str, Any]:
        """Generate summary statistics"""
        total_files = len(results)
        total_issues = len(self.issues)
        total_warnings = len(self.warnings)
        total_optimizations = len(self.optimizations)

        # Calculate scores
        security_score = max(0, 100 - (total_issues * 20))
        optimization_score = max(0, 100 - (total_optimizations * 10))

        return {
            'total_dockerfiles': total_files,
            'security_issues': total_issues,
            'warnings': total_warnings,
            'optimization_opportunities': total_optimizations,
            'security_score': security_score,
            'optimization_score': optimization_score,
            'overall_score': (security_score + optimization_score) / 2
        }

    def validate_kubernetes_manifests(self) -> Dict[str, Any]:
        """Validate Kubernetes manifests for security and best practices"""
        print("☸️  Scanning for Kubernetes manifests...")

        k8s_files = list(self.repo_path.rglob("*.yaml")) + list(self.repo_path.rglob("*.yml"))
        k8s_files = [f for f in k8s_files if
                     any(pattern in str(f) for pattern in ['k8s', 'kubernetes', 'kube', 'deploy'])]

        results = {}

        for k8s_file in k8s_files:
            try:
                with open(k8s_file, 'r') as f:
                    content = f.read()

                analysis = self._analyze_k8s_manifest(content)
                results[str(k8s_file.relative_to(self.project_root))] = analysis

            except Exception as e:
                results[str(k8s_file.relative_to(self.project_root))] = {'error': str(e)}

        return results

    def _analyze_k8s_manifest(self, content: str) -> Dict[str, Any]:
        """Analyze Kubernetes manifest content"""
        analysis = {
            'security_issues': [],
            'best_practices': [],
            'warnings': []
        }

        # Check for security contexts
        if 'securityContext:' not in content:
            analysis['security_issues'].append("Missing securityContext")

        # Check for resource limits
        if 'resources:' not in content:
            analysis['warnings'].append("Missing resource limits/requests")

        # Check for latest image tags
        if ':latest' in content:
            analysis['warnings'].append("Using 'latest' tag - consider specific versions")

        # Check for secrets in plain text (basic check)
        if 'password:' in content or 'secret:' in content:
            analysis['security_issues'].append("Potential secrets in manifest - use Kubernetes secrets")

        # Check for probes
        has_probes = any(probe in content for probe in ['livenessProbe:', 'readinessProbe:'])
        if not has_probes:
            analysis['best_practices'].append("Consider adding health probes")

        return analysis

    def generate_report(self) -> Dict[str, Any]:
        """Generate complete validation report"""
        docker_results = self.validate_all_dockerfiles()
        k8s_results = self.validate_kubernetes_manifests()

        report = {
            'docker': docker_results,
            'kubernetes': k8s_results,
            'summary': {
                'docker_score': docker_results['summary']['overall_score'],
                'k8s_files_analyzed': len(k8s_results),
                'total_issues': len(docker_results['issues']) + sum(
                    len(analysis.get('security_issues', [])) for analysis in k8s_results.values()),
                'total_warnings': len(docker_results['warnings']) + sum(
                    len(analysis.get('warnings', [])) for analysis in k8s_results.values()),
                'total_optimizations': len(docker_results['optimizations'])
            },
            'recommendations': self._generate_recommendations(docker_results, k8s_results)
        }

        return report

    def _generate_recommendations(self, docker_results: Dict, k8s_results: Dict) -> List[str]:
        """Generate improvement recommendations"""
        recommendations = []

        # Docker recommendations
        if docker_results['summary']['security_score'] < 80:
            recommendations.append("🔒 Improve Docker security - address securityContext and USER directives")

        if docker_results['summary']['optimization_score'] < 80:
            recommendations.append("⚡ Optimize Docker images - implement multi-stage builds and cache cleanup")

        # K8s recommendations
        k8s_issues = sum(len(analysis.get('security_issues', [])) for analysis in k8s_results.values())
        if k8s_issues > 0:
            recommendations.append(
                "☸️  Address Kubernetes security issues - add security contexts and proper secrets management")

        # General recommendations
        recommendations.extend([
            "🔍 Implement automated container scanning (Trivy, Clair)",
            "📊 Add container resource monitoring and alerting",
            "🔄 Implement proper CI/CD security scanning",
            "📝 Document container security policies and procedures",
            "🎯 Use distroless base images for minimal attack surface"
        ])

        return recommendations


def main():
    parser = argparse.ArgumentParser(description="Docker Security and Optimization Validator")
    parser.add_argument("--output", type=str, help="Output JSON report file")
    parser.add_argument("--format", choices=['json', 'text'], default='text', help="Output format")
    parser.add_argument("--quiet", action="store_true", help="Quiet mode")

    args = parser.parse_args()

    validator = DockerSecurityValidator()
    report = validator.generate_report()

    if not args.quiet:
        print("\n" + "=" * 80)
        print("🐳 Docker & Kubernetes Security Validation Report")
        print("=" * 80)

        summary = report['summary']
        print(f"📊 Docker Score: {summary['docker_score']:.1f}/100")
        print(f"📁 K8s Files: {summary['k8s_files_analyzed']}")
        print(f"🔴 Issues: {summary['total_issues']}")
        print(f"WARNING  Warnings: {summary['total_warnings']}")
        print(f"💡 Optimizations: {summary['total_optimizations']}")

        if report['recommendations']:
            print(f"\n💡 Recommendations:")
            print("-" * 40)
            for rec in report['recommendations'][:8]:
                print(f"   {rec}")

        print("\n" + "=" * 80)

    if args.output:
        with open(args.output, 'w') as f:
            if args.format == 'json':
                json.dump(report, f, indent=2)
            else:
                # Simple text format
                f.write("Docker & Kubernetes Security Validation Report\n")
                f.write(f"Generated: {json.dumps(report, indent=2)}")
        print(f"📄 Report saved to {args.output}")

    # Exit with code based on issues
    exit_code = 0 if report['summary']['total_issues'] == 0 else 1
    sys.exit(exit_code)


if __name__ == "__main__":
    main()
