import os
import re
import yaml
from pathlib import Path

knowledge_dir = Path('knowledge')

def find_yaml_files_without_github_issue(root_dir):
    files_without_issue = []
    
    for dirpath, _, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename.endswith(('.yaml', '.yml')):
                filepath = os.path.join(dirpath, filename)
                relative_filepath = os.path.relpath(filepath, root_dir).replace('\\', '/')
                
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        content = f.read()
                        
                        has_github_issue = bool(re.search(r'github_issue:\s*\d+', content))
                        
                        if not has_github_issue:
                            files_without_issue.append(relative_filepath)
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
        description = None
        
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
                        elif 'goal' in data['summary']:
                            summary = data['summary']['goal']
                    elif isinstance(data['summary'], str):
                        summary = data['summary']
                
                if 'content' in data:
                    if isinstance(data['content'], dict):
                        description = str(data['content'])[:500]
                    elif isinstance(data['content'], str):
                        description = data['content'][:500]
        except:
            pass
        
        if not title:
            title = file_path.split('/')[-1].replace('.yaml', '').replace('.yml', '')
        
        body = f"""## Файл: `{file_path}`

{summary or description or f"Обработка файла {file_path}"}

### Детали
- Путь: `{file_path}`
- Требуется обработка файла и регистрация в системе задач

### Примечание
Этот Issue создан автоматически для файла без `github_issue`.
"""
        
        return {
            'title': title,
            'body': body,
            'file_path': file_path
        }
    except Exception as e:
        print(f"Ошибка при чтении {file_path}: {e}")
        return None

def determine_category_and_labels(file_path):
    if file_path.startswith('analysis/'):
        return 'analysis', ['agent:idea-writer', 'stage:idea', 'analysis']
    elif file_path.startswith('canon/narrative/'):
        return 'narrative', ['agent:idea-writer', 'stage:idea', 'narrative', 'canon']
    elif file_path.startswith('canon/lore/'):
        return 'lore', ['agent:idea-writer', 'stage:idea', 'lore', 'canon']
    elif file_path.startswith('implementation/'):
        return 'implementation', ['agent:architect', 'stage:design', 'implementation']
    elif file_path.startswith('mechanics/'):
        return 'mechanics', ['agent:idea-writer', 'stage:idea', 'mechanics']
    elif file_path.startswith('content/'):
        return 'content', ['agent:idea-writer', 'stage:idea', 'content']
    else:
        return 'other', ['agent:idea-writer', 'stage:idea']

if __name__ == "__main__":
    print("Поиск файлов без github_issue в knowledge/...")
    print("(Игнорируя needs_task: false - создаем Issues для всех файлов без github_issue)\n")
    
    files_without_issue = find_yaml_files_without_github_issue(knowledge_dir)
    
    print(f"Найдено файлов без github_issue: {len(files_without_issue)}")
    
    if files_without_issue:
        print("\n" + "="*80)
        print("Группировка файлов по категориям:")
        
        categories = {}
        for file_path in files_without_issue:
            category, labels = determine_category_and_labels(file_path)
            if category not in categories:
                categories[category] = {'labels': labels, 'files': []}
            categories[category]['files'].append(file_path)
        
        for category, data in sorted(categories.items()):
            print(f"\n{category.upper()}: {len(data['files'])} файлов")
            print(f"  Метки: {', '.join(data['labels'])}")
            print(f"  Примеры файлов:")
            for file_path in data['files'][:5]:
                file_info = get_file_info(file_path)
                if file_info:
                    print(f"    - {file_path}: {file_info['title']}")
            if len(data['files']) > 5:
                print(f"    ... и еще {len(data['files']) - 5} файлов")
        
        print("\n" + "="*80)
        print("ИНСТРУКЦИЯ ДЛЯ СОЗДАНИЯ ISSUES:")
        print("\nДля каждого файла нужно:")
        print("1. Использовать MCP GitHub: mcp_github_issue_write")
        print("2. method='create'")
        print("3. owner='gc-lover', repo='necpgame-monorepo'")
        print("4. title из get_file_info()['title']")
        print("5. body из get_file_info()['body']")
        print("6. labels из determine_category_and_labels()")
        print("7. После создания Issue - обновить файл, добавив github_issue номер")
        print("8. Удалить файл из списка files_without_github_issue.txt")
        
        with open('files_without_github_issue.txt', 'w', encoding='utf-8') as f:
            for file_path in files_without_issue:
                f.write(file_path + '\n')
        print(f"\nСписок сохранен в files_without_github_issue.txt")
    else:
        print("\n[OK] Все файлы уже имеют github_issue!")
        with open('files_without_github_issue.txt', 'w', encoding='utf-8') as f:
            f.write('')

