import json

def list_meta():
    try:
        with open('project_items_utf8.json', 'r', encoding='utf-8-sig') as f:
            data = json.load(f)
            
        items = data.get('items', [])
        statuses = set()
        agents = set()
        for item in items:
            statuses.add(item.get('status'))
            agents.add(item.get('agent'))
        
        print(f"Statuses: {statuses}")
        print(f"Agents: {agents}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    list_meta()
