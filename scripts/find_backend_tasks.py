import json

def find_tasks():
    try:
        with open('project_items_utf8.json', 'r', encoding='utf-8-sig') as f:
            data = json.load(f)
            
        items = data.get('items', [])
        tasks = []
        for item in items:
            status = item.get('status')
            if status != 'Done':
                tasks.append({
                    'id': item.get('id'),
                    'title': item.get('title'),
                    'number': item.get('content', {}).get('number'),
                    'agent': item.get('agent'),
                    'status': status,
                    'type': item.get('tYPE'),
                    'check': item.get('cHECK')
                })
        
        print(json.dumps(tasks, indent=2))
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    find_tasks()
