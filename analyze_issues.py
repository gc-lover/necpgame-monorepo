import json
import sys

files = {
    'idea-writer': 'c:/Users/zzzle/.cursor/projects/c-NECPGAME/agent-tools/3748db95-268c-4308-bb28-d50e374138e1.txt',
    'devops': 'c:/Users/zzzle/.cursor/projects/c-NECPGAME/agent-tools/51d9b6c1-45e6-454c-82ca-67a757732c3b.txt'
}

results = {}

for agent, filepath in files.items():
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            data = json.load(f)
            results[agent] = [
                {
                    'number': item['number'],
                    'title': item['title'],
                    'url': item['html_url']
                }
                for item in data.get('items', [])
            ]
    except Exception as e:
        results[agent] = []
        print(f"Error reading {agent}: {e}", file=sys.stderr)

for agent, issues in results.items():
    print(f"\n{agent.upper()}: {len(issues)} issues")
    for issue in issues[:20]:  # Первые 20
        print(f"  #{issue['number']}: {issue['title']}")

