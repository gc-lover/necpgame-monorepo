import os
import yaml
from pathlib import Path

def find_yaml_files_without_github_issue(root_dir):
    files_without_issue = []
    
    for dirpath, _, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename.endswith(('.yaml', '.yml')):
                filepath = os.path.join(dirpath, filename)
                relative_filepath = os.path.relpath(filepath, root_dir)
                
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        content = f.read()
                        
                        if 'github_issue:' not in content:
                            files_without_issue.append(relative_filepath.replace('\\', '/'))
                except Exception as e:
                    print(f"Ошибка при чтении {filepath}: {e}")
    
    return files_without_issue

if __name__ == "__main__":
    root_directory = Path('knowledge')
    
    files_without_issue = find_yaml_files_without_github_issue(root_directory)
    
    print(f"Найдено файлов без github_issue: {len(files_without_issue)}")
    
    with open('files_without_github_issue.txt', 'w', encoding='utf-8') as f:
        for file in files_without_issue:
            f.write(file + '\n')
    
    print(f"\nСписок сохранен в files_without_github_issue.txt")
    print(f"\nПервые 20 файлов:")
    for i, file in enumerate(files_without_issue[:20], 1):
        print(f"{i}. {file}")

