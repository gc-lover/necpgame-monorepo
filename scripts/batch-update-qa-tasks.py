#!/usr/bin/env python3
"""
Batch update remaining QA tasks to Todo status and QA agent.
"""

import subprocess
import sys
from pathlib import Path

# Remaining task IDs to update
remaining_tasks = [
    140922144, 140922148, 140922153,  # Kiev quests
    140922165, 140922173, 140922175   # Baku quests
]

def run_command(cmd):
    """Run shell command and return success"""
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    return result.returncode == 0

def main():
    print(f"[INFO] Updating {len(remaining_tasks)} tasks to QA agent...")

    success_count = 0

    for task_id in remaining_tasks:
        print(f"[INFO] Updating task {task_id}...")

        # Update status to Todo
        status_cmd = f'python scripts/update-github-fields.py --item-id {task_id} --type DATA --check 1'
        if run_command(status_cmd):
            print(f"[OK] Updated fields for task {task_id}")
            success_count += 1
        else:
            print(f"[ERROR] Failed to update fields for task {task_id}")

    print(f"[SUMMARY] Successfully updated {success_count}/{len(remaining_tasks)} tasks")

if __name__ == "__main__":
    main()
