import os

file_path = r"c:\NECPGAME\proto\openapi\tournament-service\main.yaml"

with open(file_path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
skip = False

# Adjust line numbers because enumerate starts at 0, file lines are 1-based
# We want to skip 736 to 1136 (inclusive of 736, exclusive of 1137)
# And 1141 to 1242 (inclusive of 1141, exclusive of 1243)

for i, line in enumerate(lines):
    line_num = i + 1
    
    if line_num == 736:
        skip = True
    if line_num == 1137:
        skip = False
    
    if line_num == 1141:
        skip = True
    if line_num == 1243:
        skip = False
        
    if not skip:
        # Text replacements
        l = line
        l = l.replace("exampleDomainBatchHealthCheck", "tournamentServiceBatchHealthCheck")
        l = l.replace("example-domain", "tournament-service")
        l = l.replace("specialized-domain", "matchmaking-service")
        # Fix example URL in heartbeat
        l = l.replace("api/v1/tournament-service/health/ws", "api/v1/tournament/health/ws") # specific fix if needed
        # Actually line 681: ws://localhost:8080/api/v1/example-domain/health/ws
        # becomes ws://localhost:8080/api/v1/tournament-service/health/ws
        # but the server url is /api/v1/tournament (singular).
        # Check servers block:
        # - url: https://api.necpgame.com/v1/tournament
        # So it should probably be `tournament` not `tournament-service`.
        # However, domain name is tournament-service.
        # Let's check consistency.
        
        new_lines.append(l)

with open(file_path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)

print(f"Processed {len(lines)} lines, writing {len(new_lines)} lines.")
