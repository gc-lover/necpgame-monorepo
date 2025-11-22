import os
import re
from pathlib import Path

knowledge_dir = Path('knowledge')

def check_file(file_path):
    full_path = knowledge_dir / file_path
    if not full_path.exists():
        return None
    
    try:
        with open(full_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        has_github_issue = bool(re.search(r'github_issue:\s*\d+', content))
        has_needs_task = 'needs_task:' in content
        needs_task_false = bool(re.search(r'needs_task:\s*false', content, re.IGNORECASE))
        
        return {
            'file_path': file_path,
            'has_github_issue': has_github_issue,
            'has_needs_task': has_needs_task,
            'needs_task_false': needs_task_false
        }
    except Exception as e:
        return {'file_path': file_path, 'error': str(e)}

files_without_issue = []
files_with_needs_task_false = []
all_files = []

for dirpath, _, filenames in os.walk(knowledge_dir):
    for filename in filenames:
        if filename.endswith(('.yaml', '.yml')):
            filepath = os.path.join(dirpath, filename)
            relative_filepath = os.path.relpath(filepath, knowledge_dir).replace('\\', '/')
            all_files.append(relative_filepath)

print(f"Всего YAML файлов: {len(all_files)}")
print("\nПроверка файлов...")

for i, file_path in enumerate(all_files, 1):
    result = check_file(file_path)
    if result:
        if not result.get('has_github_issue', False):
            files_without_issue.append(file_path)
        if result.get('needs_task_false', False) and not result.get('has_github_issue', False):
            files_with_needs_task_false.append(file_path)
    
    if i % 500 == 0:
        print(f"Проверено: {i}/{len(all_files)}")

print(f"\n{'='*80}")
print(f"РЕЗУЛЬТАТЫ:")
print(f"  Всего файлов: {len(all_files)}")
print(f"  Файлов без github_issue: {len(files_without_issue)}")
print(f"  Файлов с needs_task: false но без github_issue: {len(files_with_needs_task_false)}")

if files_without_issue:
    print(f"\nПервые 20 файлов без github_issue:")
    for i, file_path in enumerate(files_without_issue[:20], 1):
        print(f"{i}. {file_path}")
    if len(files_without_issue) > 20:
        print(f"... и еще {len(files_without_issue) - 20} файлов")
    
    with open('files_without_github_issue.txt', 'w', encoding='utf-8') as f:
        for file_path in files_without_issue:
            f.write(file_path + '\n')
    print(f"\nСписок сохранен в files_without_github_issue.txt")

if files_with_needs_task_false:
    print(f"\nФайлы с needs_task: false но без github_issue:")
    for i, file_path in enumerate(files_with_needs_task_false[:10], 1):
        print(f"{i}. {file_path}")
    if len(files_with_needs_task_false) > 10:
        print(f"... и еще {len(files_with_needs_task_false) - 10} файлов")

