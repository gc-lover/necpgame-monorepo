import os
import re
from pathlib import Path

knowledge_dir = Path('knowledge')

issue_mapping = {
    'analysis/tasks': 134,
    'canon/narrative': 133,
    'canon/lore/_03-lore': 132,
    'canon/lore': 132,
    'implementation': 136,
    'mechanics': 137,
}

with open('files_without_github_issue.txt', 'r', encoding='utf-8') as f:
    files_to_update = [line.strip() for line in f if line.strip()]

def get_issue_number(file_path):
    for prefix, issue_num in issue_mapping.items():
        if file_path.startswith(prefix):
            return issue_num
    return None

updated_count = 0
skipped_count = 0
errors = []

for file_path in files_to_update[:100]:
    full_path = knowledge_dir / file_path
    if not full_path.exists():
        skipped_count += 1
        continue
    
    try:
        content = full_path.read_text(encoding='utf-8', errors='ignore')
        
        if re.search(r'github_issue:\s*\d+', content):
            skipped_count += 1
            continue
        
        issue_num = get_issue_number(file_path)
        if not issue_num:
            skipped_count += 1
            continue
        
        if 'implementation:' in content:
            if re.search(r'implementation:\s*\n\s*needs_task:', content, re.MULTILINE):
                content = re.sub(
                    r'(implementation:\s*\n\s*needs_task:)',
                    f'implementation:\n  github_issue: {issue_num}\n  needs_task:',
                    content,
                    count=1
                )
            elif re.search(r'implementation:\s*\n\s*queue_reference:', content, re.MULTILINE):
                content = re.sub(
                    r'(implementation:\s*\n\s*queue_reference:)',
                    f'implementation:\n  github_issue: {issue_num}\n  queue_reference:',
                    content,
                    count=1
                )
            elif re.search(r'implementation:\s*\n\s*blockers:', content, re.MULTILINE):
                content = re.sub(
                    r'(implementation:\s*\n\s*blockers:)',
                    f'implementation:\n  github_issue: {issue_num}\n  blockers:',
                    content,
                    count=1
                )
            else:
                content = re.sub(
                    r'(implementation:)',
                    f'implementation:\n  github_issue: {issue_num}',
                    content,
                    count=1
                )
        else:
            if 'metadata:' in content:
                content = re.sub(
                    r'(metadata:)',
                    f'metadata:\n  github_issue: {issue_num}',
                    content,
                    count=1
                )
            else:
                content = f'github_issue: {issue_num}\n\n{content}'
        
        full_path.write_text(content, encoding='utf-8')
        updated_count += 1
        print(f"✓ {file_path} -> Issue #{issue_num}")
        
    except Exception as e:
        errors.append(f"{file_path}: {e}")

print(f"\nОбновлено: {updated_count}")
print(f"Пропущено: {skipped_count}")
if errors:
    print(f"Ошибки: {len(errors)}")
    for error in errors[:10]:
        print(f"  - {error}")

