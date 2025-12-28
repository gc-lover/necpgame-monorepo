#!/usr/bin/env python3
"""
NECPGAME - Close Completed GitHub Issues
Script to close issues that are marked as completed in comments
"""

import requests
import json
import os
from datetime import datetime

# GitHub API configuration
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
REPO_OWNER = 'gc-lover'
REPO_NAME = 'necpgame-monorepo'

def get_issue_comments(issue_number):
    """Get all comments for an issue"""
    url = f'https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/issues/{issue_number}/comments'
    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Failed to get comments for issue #{issue_number}: {response.status_code}")
        return []

def check_issue_completed(issue_number):
    """Check if issue is marked as completed in comments"""
    comments = get_issue_comments(issue_number)
    for comment in comments:
        body = comment.get('body', '').lower()
        if '[ok]' in body and ('completed' in body or 'ready' in body or 'verified' in body):
            return True
    return False

def close_issue(issue_number):
    """Close a GitHub issue"""
    url = f'https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/issues/{issue_number}'
    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }
    data = {
        'state': 'closed'
    }

    response = requests.patch(url, headers=headers, json=data)
    if response.status_code == 200:
        print(f"✅ Successfully closed issue #{issue_number}")
        return True
    else:
        print(f"❌ Failed to close issue #{issue_number}: {response.status_code}")
        return False

def main():
    """Main function to close completed issues"""
    if not GITHUB_TOKEN:
        print("[ERROR] GITHUB_TOKEN environment variable not set")
        return

    # List of issues to check (based on our verification)
    issues_to_check = [
        2252,  # Backend completed reset-service-go implementation
        2229,  # API Designer completed code generation for crafting-service
        2228,  # Work on [API] Генерация кода из OpenAPI спецификации крафт-системы
        2223,  # Backend validation comment
        2221,  # [Idea] Social System Enhancement - Dynamic Relationships & Reputation Networks
        2219,  # [Idea] Combat System Enhancement - Advanced Combos & Synergies
        2215,  # [DB] Event Store Schema for Microservice Orchestration
        2213,  # [UE5] Tournament Spectator Mode Implementation
        2212,  # [Security] Anti-Cheat Player Behavior Analytics
    ]

    closed_count = 0

    for issue_number in issues_to_check:
        if check_issue_completed(issue_number):
            if close_issue(issue_number):
                closed_count += 1
        else:
            print(f"⚠️ Issue #{issue_number} not marked as completed in comments")

    print(f"\n📊 Summary: Closed {closed_count} out of {len(issues_to_check)} issues")

if __name__ == '__main__':
    main()
