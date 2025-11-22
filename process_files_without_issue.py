import os
import re
import yaml
from pathlib import Path
from datetime import datetime

knowledge_dir = Path('knowledge')
files_list_path = Path('files_without_github_issue.txt')

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
                        
                        has_github_issue = bool(re.search(r'github_issue:\s*\d+', content))
                        
                        if not has_github_issue:
                            files_without_issue.append(relative_filepath.replace('\\', '/'))
                except Exception as e:
                    print(f"Ошибка при чтении {filepath}: {e}")
    
    return files_without_issue

def get_file_info(file_path):
    full_path = knowledge_dir / file_path
    if not full_path.exists():
        return None
    
    try:
        with open(full_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        title = None
        summary = None
        
        try:
            data = yaml.safe_load(content)
            if isinstance(data, dict):
                if 'metadata' in data and 'title' in data['metadata']:
                    title = data['metadata']['title']
                elif 'title' in data:
                    title = data['title']
                elif 'summary' in data and 'essence' in data['summary']:
                    title = data['summary']['essence']
                
                if 'summary' in data:
                    if isinstance(data['summary'], dict):
                        if 'essence' in data['summary']:
                            summary = data['summary']['essence']
                        elif 'problem' in data['summary']:
                            summary = data['summary']['problem']
                    elif isinstance(data['summary'], str):
                        summary = data['summary']
        except:
            pass
        
        if not title:
            title = file_path.split('/')[-1].replace('.yaml', '').replace('.yml', '')
        
        return {
            'title': title,
            'summary': summary or f"Обработка файла {file_path}",
            'file_path': file_path
        }
    except Exception as e:
        print(f"Ошибка при чтении {file_path}: {e}")
        return None

def determine_category(file_path):
    if file_path.startswith('analysis/'):
        return 'analysis', 134
    elif file_path.startswith('canon/narrative/'):
        return 'narrative', 133
    elif file_path.startswith('canon/lore/'):
        return 'lore', 132
    elif file_path.startswith('implementation/'):
        return 'implementation', 136
    elif file_path.startswith('mechanics/'):
        return 'mechanics', 137
    elif file_path.startswith('content/'):
        return 'content', 103
    else:
        return 'other', None

if __name__ == "__main__":
    print("Поиск файлов без github_issue...")
    files_without_issue = find_yaml_files_without_github_issue(knowledge_dir)
    
    print(f"\nНайдено файлов без github_issue: {len(files_without_issue)}")
    
    if files_without_issue:
        with open(files_list_path, 'w', encoding='utf-8') as f:
            for file in files_without_issue:
                f.write(file + '\n')
        
        print(f"\nСписок сохранен в {files_list_path}")
        print(f"\nПервые 20 файлов:")
        for i, file in enumerate(files_without_issue[:20], 1):
            print(f"{i}. {file}")
        
        if len(files_without_issue) > 20:
            print(f"\n... и еще {len(files_without_issue) - 20} файлов")
        
        print("\n" + "="*80)
        print("Группировка файлов по категориям для создания Issues:")
        
        categories = {}
        for file_path in files_without_issue:
            category, issue_num = determine_category(file_path)
            if category not in categories:
                categories[category] = {'issue': issue_num, 'files': []}
            categories[category]['files'].append(file_path)
        
        for category, data in sorted(categories.items()):
            print(f"\n{category.upper()}: {len(data['files'])} файлов")
            if data['issue']:
                print(f"  Issue: #{data['issue']}")
            else:
                print(f"  Issue: нужно создать новый")
    else:
        print("\nВсе файлы уже имеют github_issue!")
        with open(files_list_path, 'w', encoding='utf-8') as f:
            f.write('')

