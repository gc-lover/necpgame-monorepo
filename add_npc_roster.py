from pathlib import Path
path = Path(r'C:\\NECPGAME\\.BRAIN\\06-tasks\\config\\readiness-tracker.yaml')
text = path.read_text(encoding='utf-8')
old = 'TEST-LINE\r\n\r\n  - path: ''.BRAIN/03-lore/_03-lore/characters/activity-npc-roster.md''\r\n'
