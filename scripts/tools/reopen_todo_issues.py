#!/usr/bin/env python3
"""
Скрипт для переоткрытия GitHub Issues со статусом Todo в GitHub Projects

Использование:
1. Установите зависимости: pip install requests python-dotenv
2. Создайте .env файл с GITHUB_TOKEN=ваш_токен
3. Запустите: python reopen_todo_issues.py

Требования:
- GITHUB_TOKEN с правами на чтение projects и issues
- OWNER: владелец репозитория (по умолчанию: gc-lover)
- REPO: название репозитория (по умолчанию: necpgame-monorepo)
- PROJECT_NUMBER: номер проекта (по умолчанию: 1)
"""

import os
import sys
import json
import requests
from typing import List, Dict, Any
from dotenv import load_dotenv

# Загрузка переменных окружения
load_dotenv()

class GitHubTodoReopener:
    def __init__(self):
        self.token = os.getenv('GITHUB_TOKEN')
        if not self.token:
            print("ERROR: Не найден GITHUB_TOKEN в переменных окружения")
            print("Создайте .env файл с GITHUB_TOKEN=ваш_токен")
            sys.exit(1)

        self.owner = os.getenv('GITHUB_OWNER', 'gc-lover')
        self.repo = os.getenv('GITHUB_REPO', 'necpgame-monorepo')
        self.project_number = int(os.getenv('GITHUB_PROJECT_NUMBER', '1'))

        self.base_url = 'https://api.github.com'
        self.headers = {
            'Authorization': f'Bearer {self.token}',
            'Accept': 'application/vnd.github+json',
            'X-GitHub-Api-Version': '2022-11-28'
        }

    def get_project_id(self) -> str:
        """Получить ID проекта по номеру через GraphQL"""
        query = """
        query($owner: String!, $repo: String!, $number: Int!) {
          repository(owner: $owner, name: $repo) {
            projectV2(number: $number) {
              id
            }
          }
        }
        """

        variables = {
            "owner": self.owner,
            "repo": self.repo,
            "number": self.project_number
        }

        response = requests.post(
            'https://api.github.com/graphql',
            headers=self.headers,
            json={"query": query, "variables": variables}
        )

        if response.status_code != 200:
            print(f"ERROR: Ошибка получения проекта: {response.status_code}")
            print(response.text)
            return None

        data = response.json()
        if 'errors' in data:
            print(f"ERROR: GraphQL ошибки: {data['errors']}")
            return None

        project = data.get('data', {}).get('repository', {}).get('projectV2')
        if not project:
            print(f"ERROR: Проект с номером {self.project_number} не найден")
            return None

        return project['id']

    def get_project_items(self, project_id: str) -> List[Dict]:
        """Получить все items проекта через GraphQL"""
        query = """
        query($projectId: ID!, $after: String) {
          node(id: $projectId) {
            ... on ProjectV2 {
              items(first: 100, after: $after) {
                nodes {
                  id
                  fieldValues(first: 10) {
                    nodes {
                      ... on ProjectV2ItemFieldTextValue {
                        field {
                          ... on ProjectV2Field {
                            name
                          }
                        }
                        text
                      }
                      ... on ProjectV2ItemFieldSingleSelectValue {
                        field {
                          ... on ProjectV2SingleSelectField {
                            name
                          }
                        }
                        name
                      }
                    }
                  }
                  content {
                    ... on Issue {
                      number
                      title
                    }
                  }
                }
                pageInfo {
                  hasNextPage
                  endCursor
                }
              }
            }
          }
        }
        """

        items = []
        cursor = None

        while True:
            variables = {"projectId": project_id}
            if cursor:
                variables["after"] = cursor

            response = requests.post(
                'https://api.github.com/graphql',
                headers=self.headers,
                json={"query": query, "variables": variables}
            )

            if response.status_code != 200:
                print(f"ERROR: Ошибка получения items проекта: {response.status_code}")
                print(response.text)
                return []

            data = response.json()
            if 'errors' in data:
                print(f"ERROR: GraphQL ошибки: {data['errors']}")
                return []

            project_data = data.get('data', {}).get('node', {})
            items_data = project_data.get('items', {})

            page_items = items_data.get('nodes', [])
            items.extend(page_items)

            page_info = items_data.get('pageInfo', {})
            if not page_info.get('hasNextPage', False):
                break

            cursor = page_info.get('endCursor')

            # Ограничение на 10 страниц (1000 items)
            if len(items) >= 1000:
                print("WARNING: Достигнут лимит в 1000 items")
                break

        return items

    def get_todo_issues(self, items: List[Dict]) -> List[Dict]:
        """Найти issues со статусом Todo"""
        todo_issues = []

        for item in items:
            # Проверяем статус из fieldValues
            status = None
            field_values = item.get('fieldValues', {}).get('nodes', [])

            for field_value in field_values:
                field_name = None

                # Проверяем имя поля
                if 'field' in field_value:
                    field = field_value.get('field', {})
                    field_name = field.get('name')

                # Получаем значение статуса
                if field_name == 'Status':
                    # Может быть text или name в зависимости от типа поля
                    status = field_value.get('text') or field_value.get('name')
                    break

            if status == 'Todo':
                content = item.get('content', {})
                if content and 'number' in content:
                    todo_issues.append({
                        'number': content.get('number'),
                        'title': content.get('title'),
                        'status': status
                    })

        return todo_issues

    def reopen_issue(self, issue_number: int) -> bool:
        """Переоткрыть issue"""
        url = f'{self.base_url}/repos/{self.owner}/{self.repo}/issues/{issue_number}'
        data = {'state': 'open'}

        response = requests.patch(url, headers=self.headers, json=data)

        if response.status_code == 200:
            print(f"SUCCESS: Переоткрыта задача #{issue_number}")
            return True
        else:
            print(f"ERROR: Ошибка переоткрытия #{issue_number}: {response.status_code}")
            print(response.text)
            return False

    def run(self):
        """Основная логика скрипта"""
        print("START: Запуск скрипта переоткрытия задач Todo")
        print(f"REPO: Репозиторий: {self.owner}/{self.repo}")
        print(f"PROJECT: Проект: #{self.project_number}")
        print()

        # Получаем ID проекта
        print("SEARCH: Получение ID проекта...")
        project_id = self.get_project_id()
        if not project_id:
            return

        print(f"SUCCESS: Найден проект ID: {project_id}")

        # Получаем items проекта
        print("DOWNLOAD: Получение items проекта...")
        items = self.get_project_items(project_id)
        print(f"STATS: Найдено {len(items)} items в проекте")

        # Находим Todo задачи
        print("SEARCH: Поиск задач со статусом Todo...")
        todo_issues = self.get_todo_issues(items)
        print(f"FOUND: Найдено {len(todo_issues)} задач со статусом Todo")

        if not todo_issues:
            print("INFO: Нет задач для переоткрытия")
            return

        # Выводим найденные задачи
        print("\nTASKS: Задачи для переоткрытия:")
        for issue in todo_issues:
            print(f"  #{issue['number']}: {issue['title']}")

        # Запрашиваем подтверждение
        print(f"\nWARNING: Будет переоткрыто {len(todo_issues)} задач")
        if '--yes' not in sys.argv:
            answer = input("Продолжить? (y/N): ")
            if answer.lower() not in ['y', 'yes']:
                print("CANCEL: Отменено пользователем")
                return

        # Переоткрываем задачи
        print("\nREOPEN: Переоткрытие задач...")
        reopened_count = 0

        for issue in todo_issues:
            if self.reopen_issue(issue['number']):
                reopened_count += 1

        print(f"\nSUCCESS: Готово! Переоткрыто {reopened_count}/{len(todo_issues)} задач")

def main():
    try:
        reopener = GitHubTodoReopener()
        reopener.run()
    except KeyboardInterrupt:
        print("\nCANCEL: Прервано пользователем")
    except Exception as e:
        print(f"ERROR: Неожиданная ошибка: {e}")
        import traceback
        traceback.print_exc()

if __name__ == '__main__':
    main()