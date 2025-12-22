#!/usr/bin/env python3
"""
Git Push Fix Script
Diagnoses and fixes common git push issues
"""
import subprocess
import sys

def run_command(cmd, description):
    """Run command and return result"""
    print(f"\n[CHECK] {description}")
    print(f"Running: {cmd}")
    try:
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True, timeout=30)
        print(f"Exit code: {result.returncode}")
        if result.stdout:
            print(f"Output: {result.stdout.strip()}")
        if result.stderr:
            print(f"Errors: {result.stderr.strip()}")
        return result
    except Exception as e:
        print(f"Error running command: {e}")
        return None

def main():
    print("GIT PUSH DIAGNOSTIC AND FIX SCRIPT")
    print("=" * 50)

    # 1. Check git status
    run_command("git status --porcelain", "Checking git status")

    # 2. Check current branch
    branch_result = run_command("git branch --show-current", "Checking current branch")
    if branch_result and branch_result.stdout:
        current_branch = branch_result.stdout.strip()
        print(f"Current branch: {current_branch}")
    else:
        current_branch = "develop"  # fallback

    # 3. Check remote
    run_command("git remote -v", "Checking git remotes")

    # 4. Fetch latest changes
    print("\n[FIX] Fetching latest changes...")
    run_command("git fetch origin", "Fetching from origin")

    # 5. Check if branch is ahead/behind
    run_command(f"git status -b --ahead-behind origin/{current_branch}", "Checking branch status")

    # 6. Try to pull with rebase
    print("\n[FIX] Attempting to pull with rebase...")
    pull_result = run_command(f"git pull --rebase origin {current_branch}", "Pulling with rebase")

    if pull_result and pull_result.returncode == 0:
        print("[SUCCESS] Pull successful, now trying push...")
        # Try push again
        push_result = run_command(f"git push origin {current_branch}:{current_branch}", "Pushing to remote")
        if push_result and push_result.returncode == 0:
            print("[SUCCESS] Push successful!")
            return
        else:
            print("[ERROR] Push still failed")
    else:
        print("[ERROR] Pull failed, trying alternative approaches...")

    # 7. Try force push with lease (safer than --force)
    print("\n[FIX] Trying force push with lease...")
    force_push = run_command(f"git push --force-with-lease origin {current_branch}:{current_branch}", "Force pushing with lease")

    if force_push and force_push.returncode == 0:
        print("[SUCCESS] Force push successful!")
    else:
        print("[ERROR] Force push failed")
        print("\n[INFO] Possible solutions:")
        print("1. Check GitHub branch protection rules")
        print("2. Ensure you have write access to the repository")
        print("3. Check if remote branch exists: git ls-remote origin")
        print("4. Try: git push -u origin develop")

if __name__ == '__main__':
    main()
