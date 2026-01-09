import json
import sys

def find_open_backend():
    try:
        # Try UTF-16
        with open('open_issues.json', 'r', encoding='utf-16') as f:
            issues = json.load(f)
            
        backend_issues = [i for i in issues if '[Backend]' in i.get('title', '') and 'COMPLETED' not in i.get('title', '')]
        
        for i in backend_issues[:10]:
            title = i.get('title', '')
            safe_title = title.encode('ascii', 'ignore').decode('ascii')
            print(f"#{i.get('number')}: {safe_title}")
            
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    find_open_backend()
