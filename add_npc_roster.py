from pathlib import Path
path = Path(r'C:\\NECPGAME\\.BRAIN\\06-tasks\\config\\readiness-tracker.yaml')
text = path.read_text(encoding='utf-8')
old = 'TEST-LINE\r\n\r\n  - path: ''.BRAIN/03-lore/_03-lore/characters/activity-npc-roster.md''\r\n'
new = \\" - path: "\\\.BRAIN/03-lore/_03-lore/characters/activity-npc-roster.md\\\\r\n\ 
new += \\" version: "\\\1.0.0\\\\r\n\ 
new += \\" status: "\\\not-applicable\\\\r\n\ 
new += \\" priority: "\\\medium\\\\r\n\ 
new += \\" checked: "\\\2025-11-09 10:15\\\\r\n\ 
new += \\" checker: "\\\Brain Manager\\\\r\n\ 
new += \\" "api_target:\r\n\ 
new += \\" microservice: "null\r\n\ 
new += \\" directory: "null\r\n\ 
new += \\" frontend_module: "null\r\n\ 
new += \\" notes: "\\\Перепроверено 2025-11-09 10:15: NPC ростер активностей - справочный каталог связей, не требует постановки API задач.\\\\r\n\ 
if old in text:
    path.write_text(text.replace(old, new), encoding='utf-8')
elif '.BRAIN/03-lore/_03-lore/characters/activity-npc-roster.md' not in text:
