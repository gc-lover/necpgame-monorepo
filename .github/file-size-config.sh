#!/bin/bash

export MAX_LINES=600

export FILE_EXTENSIONS=(
  "md"
  "mdx"
  "yaml"
  "yml"
  "json"
  "proto"
  "go"
  "cpp"
  "h"
  "hpp"
  "java"
  "kt"
  "ts"
  "tsx"
  "js"
  "jsx"
  "py"
  "rs"
  "sql"
  "sh"
  "bat"
  "ps1"
  "c"
  "cs"
)

export EXCLUDED_PATTERNS=(
  "*.gen.go"
  "*.pb.go"
  "*_generated.go"
  "vendor/*"
  "node_modules/*"
  "*/pkg/api/api.gen.go"
  "*.bundled.yaml"
  "*.merged.yaml"
  "docker-compose*.yml"
  "client/UE5/*"
  "*.uasset"
  "*.umap"
  "*.upk"
  "*.uexp"
  "*.ubulk"
  "*.ufont"
  # Note: Additional patterns loaded from .githooks/pre-commit-exemptions.txt
)

check_file_extension() {
  local file="$1"
  local ext="${file##*.}"
  
  for allowed_ext in "${FILE_EXTENSIONS[@]}"; do
    if [[ "$ext" == "$allowed_ext" ]]; then
      return 0
    fi
  done
  return 1
}

load_exemptions_from_file() {
  local exemptions_file=".githooks/pre-commit-exemptions.txt"
  
  if [ -f "$exemptions_file" ]; then
    while IFS= read -r line || [ -n "$line" ]; do
      # Skip empty lines and comments
      if [[ -z "$line" || "$line" =~ ^[[:space:]]*# ]]; then
        continue
      fi
      # Add to EXCLUDED_PATTERNS
      EXCLUDED_PATTERNS+=("$line")
    done < "$exemptions_file"
  fi
}

is_excluded() {
  local file="$1"
  
  for pattern in "${EXCLUDED_PATTERNS[@]}"; do
    if [[ "$file" == $pattern ]]; then
      return 0
    fi
  done
  return 1
}

# Load exemptions on script load
load_exemptions_from_file

