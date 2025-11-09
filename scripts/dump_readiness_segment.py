from pathlib import Path

lines = Path(".BRAIN/06-tasks/config/readiness-tracker.yaml").read_text(encoding="utf-8").splitlines()

start = 224
end = 240

for idx in range(start, min(end, len(lines))):
    print(idx, repr(lines[idx]))

