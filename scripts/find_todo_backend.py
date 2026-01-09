import json

def find_todo_tasks():
    try:
        with open('project_items_utf8.json', 'r', encoding='utf-8-sig') as f:
            data = json.load(f)
            
        items = data.get('items', [])
        tasks = []
        for item in items:
            status = item.get('status')
            agent = item.get('agent')
            if status == 'Todo' and (agent == 'Backend' or agent is None):
                tasks.append({
                    'id': item.get('id'),
                    'title': item.get('title'),
                    'number': item.get('content', {}).get('number'),
                    'agent': agent,
                    'status': status,
                    'type': item.get('tYPE'),
                    'check': item.get('cHECK')
                })
        
        print(json.dumps(tasks, indent=2))
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    find_todo_tasks()
