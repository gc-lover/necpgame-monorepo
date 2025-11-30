#!/usr/bin/env python3
"""
Скрипт для проверки статуса GitHub Actions workflow runs и jobs.
Требует GITHUB_TOKEN в переменных окружения или --token параметр.
"""

import os
import sys
import json
import argparse
import requests
from typing import Optional, Dict, List


GITHUB_API_BASE = "https://api.github.com"
REPO_OWNER = "gc-lover"
REPO_NAME = "necpgame-monorepo"


def get_headers(token: str) -> Dict[str, str]:
    return {
        "Authorization": f"token {token}",
        "Accept": "application/vnd.github+json",
        "X-GitHub-Api-Version": "2022-11-28"
    }


def get_github_token() -> Optional[str]:
    token = os.getenv("GITHUB_TOKEN")
    if not token:
        token = os.getenv("GH_TOKEN")
    return token


def get_workflow_runs(token: str, workflow: Optional[str] = None, per_page: int = 10) -> List[Dict]:
    url = f"{GITHUB_API_BASE}/repos/{REPO_OWNER}/{REPO_NAME}/actions/runs"
    params = {"per_page": per_page}
    if workflow:
        params["name"] = workflow
    
    response = requests.get(url, headers=get_headers(token), params=params)
    response.raise_for_status()
    return response.json().get("workflow_runs", [])


def get_workflow_runs_by_commit(token: str, commit_sha: str) -> List[Dict]:
    url = f"{GITHUB_API_BASE}/repos/{REPO_OWNER}/{REPO_NAME}/actions/runs"
    params = {"head_sha": commit_sha, "per_page": 10}
    
    response = requests.get(url, headers=get_headers(token), params=params)
    response.raise_for_status()
    return response.json().get("workflow_runs", [])


def get_workflow_run(token: str, run_id: int) -> Dict:
    url = f"{GITHUB_API_BASE}/repos/{REPO_OWNER}/{REPO_NAME}/actions/runs/{run_id}"
    
    response = requests.get(url, headers=get_headers(token))
    response.raise_for_status()
    return response.json()


def get_run_jobs(token: str, run_id: int) -> List[Dict]:
    url = f"{GITHUB_API_BASE}/repos/{REPO_OWNER}/{REPO_NAME}/actions/runs/{run_id}/jobs"
    
    response = requests.get(url, headers=get_headers(token))
    response.raise_for_status()
    
    jobs = response.json().get("jobs", [])
    
    while "next" in response.links:
        response = requests.get(response.links["next"]["url"], headers=get_headers(token))
        response.raise_for_status()
        jobs.extend(response.json().get("jobs", []))
    
    return jobs


def format_status(status: str, conclusion: Optional[str]) -> str:
    if conclusion:
        icons = {
            "success": "OK",
            "failure": "❌",
            "cancelled": "⏹️",
            "skipped": "⏭️",
            "timed_out": "⏱️"
        }
        icon = icons.get(conclusion, "❓")
        return f"{icon} {conclusion.upper()}"
    elif status == "in_progress":
        return "🔄 IN_PROGRESS"
    elif status == "queued":
        return "⏳ QUEUED"
    else:
        return f"❓ {status.upper()}"


def print_workflow_runs(runs: List[Dict]):
    if not runs:
        print("No workflow runs found.")
        return
    
    print(f"\n📋 Found {len(runs)} workflow run(s):\n")
    print("─" * 100)
    
    for run in runs:
        status = format_status(run.get("status", "unknown"), run.get("conclusion"))
        workflow_name = run.get("name", "Unknown")
        run_id = run.get("id")
        commit_sha = run.get("head_sha", "")[:7]
        branch = run.get("head_branch", "unknown")
        created_at = run.get("created_at", "")
        html_url = run.get("html_url", "")
        
        print(f"🔄 {workflow_name}")
        print(f"   Status: {status}")
        print(f"   Run ID: {run_id}")
        print(f"   Commit: {commit_sha} on {branch}")
        print(f"   Created: {created_at}")
        print(f"   URL: {html_url}")
        print("─" * 100)


def print_run_jobs(jobs: List[Dict], run_id: int):
    if not jobs:
        print(f"\nNo jobs found for run {run_id}.")
        return
    
    print(f"\n🔧 Jobs for run {run_id} ({len(jobs)} total):\n")
    print("─" * 120)
    
    for job in sorted(jobs, key=lambda x: x.get("started_at", "") or ""):
        status = format_status(job.get("status", "unknown"), job.get("conclusion"))
        job_name = job.get("name", "Unknown")
        job_id = job.get("id")
        steps = job.get("steps", [])
        started_at = job.get("started_at", "Not started")
        completed_at = job.get("completed_at", "")
        
        print(f"⚙️  {job_name}")
        print(f"   Status: {status}")
        print(f"   Job ID: {job_id}")
        if started_at:
            print(f"   Started: {started_at}")
        if completed_at:
            print(f"   Completed: {completed_at}")
        
        if steps:
            print(f"   Steps ({len(steps)}):")
            for step in steps:
                step_status = format_status(step.get("status", "unknown"), step.get("conclusion"))
                step_name = step.get("name", "Unknown")
                print(f"     • {step_name}: {step_status}")
        
        print("─" * 120)


def main():
    parser = argparse.ArgumentParser(description="Check GitHub Actions workflow runs and jobs status")
    parser.add_argument("--token", help="GitHub Personal Access Token")
    parser.add_argument("--commit", help="Check runs for specific commit SHA")
    parser.add_argument("--workflow", help="Filter by workflow name (e.g., ci-backend.yml)")
    parser.add_argument("--run-id", type=int, help="Get details for specific run ID")
    parser.add_argument("--latest", action="store_true", help="Show latest runs")
    parser.add_argument("--jobs", type=int, help="Show jobs for specific run ID")
    parser.add_argument("--json", action="store_true", help="Output as JSON")
    
    args = parser.parse_args()
    
    token = args.token or get_github_token()
    if not token:
        print("❌ Error: GITHUB_TOKEN not found. Set it as environment variable or use --token")
        sys.exit(1)
    
    try:
        if args.jobs:
            jobs = get_run_jobs(token, args.jobs)
            if args.json:
                print(json.dumps(jobs, indent=2))
            else:
                print_run_jobs(jobs, args.jobs)
        
        elif args.run_id:
            run = get_workflow_run(token, args.run_id)
            jobs = get_run_jobs(token, args.run_id)
            
            if args.json:
                result = {"run": run, "jobs": jobs}
                print(json.dumps(result, indent=2))
            else:
                print_workflow_runs([run])
                print_run_jobs(jobs, args.run_id)
        
        elif args.commit:
            runs = get_workflow_runs_by_commit(token, args.commit)
            if args.json:
                print(json.dumps(runs, indent=2))
            else:
                print_workflow_runs(runs)
        
        else:
            runs = get_workflow_runs(token, args.workflow, per_page=10)
            if args.json:
                print(json.dumps(runs, indent=2))
            else:
                print_workflow_runs(runs)
    
    except requests.exceptions.HTTPError as e:
        print(f"❌ HTTP Error: {e}")
        if e.response.status_code == 401:
            print("   Authentication failed. Check your GITHUB_TOKEN.")
        elif e.response.status_code == 404:
            print("   Repository or resource not found.")
        sys.exit(1)
    except Exception as e:
        print(f"❌ Error: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()

