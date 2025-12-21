#!/bin/bash

# NECPGAME Emoji and Special Characters Ban Validator
# Checks for forbidden Unicode characters that can break scripts on Windows

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Forbidden Unicode ranges (emoji and special symbols)
# These can break scripts on Windows and cause encoding issues
FORBIDDEN_RANGES=(
    "1F600-1F64F"  # Emoticons
    "1F300-1F5FF"  # Misc Symbols and Pictographs
    "1F680-1F6FF"  # Transport and Map
    "1F1E6-1F1FF"  # Regional indicator symbols
    "2600-26FF"    # Misc symbols
    "2700-27BF"    # Dingbats
    "1f926-\1f937" # Gestures
    "1F1F2-1F1F4"  # Regional indicators for Macau
    "1F918-1F98F"  # Additional emoticons
    "1F990-1F9BF"  # More emoticons
    "1F9C0-1F9FF"  # Even more emoticons
    "0020-007E"    # Basic ASCII (allowed)
    "00A0-00FF"    # Latin-1 Supplement (mostly allowed)
    "0400-04FF"    # Cyrillic (allowed - Russian text)
    "2000-206F"    # General Punctuation (some special chars)
    "2190-21FF"    # Arrows
    "2200-22FF"    # Mathematical Operators
    "25A0-25FF"    # Geometric Shapes
    "2300-23FF"    # Misc Technical
    "2500-257F"    # Box Drawing
    "2580-259F"    # Block Elements
    "25B2-25CF"    # More geometric shapes
    "2600-26FF"    # More symbols (already included)
    "2700-27BF"    # More dingbats (already included)
)

# Specific forbidden characters
FORBIDDEN_CHARS=(
    "★" "♦" "♠" "♥" "♣" "►" "◄" "▲" "▼" "◆" "◇"
    "✓" "✗" "✖" "✕" "✚" "✙" "✘" "✖" "✕" "✚" "✙"
    "●" "○" "■" "□" "▲" "▼" "▶" "◀" "◆" "◇"
    "…" "–" "—" "―" "‖" "‗" "‘" "’" "‚" "‛" "‟"
    "†" "‡" "•" "‣" "․" "‥" "…" "‰" "‱" "′" "″" "‴" "‵"
)

# Files to exclude from checking
EXCLUDED_EXTENSIONS=(
    ".png" ".jpg" ".jpeg" ".gif" ".svg" ".ico" ".woff" ".woff2" ".ttf" ".eot"
    ".pdf" ".zip" ".tar" ".gz" ".7z" ".rar"
    ".mp3" ".mp4" ".avi" ".mkv" ".mov" ".wmv"
)

EXCLUDED_FILES=(
    ".git"
    "node_modules"
)

EXCLUDED_PATTERNS=(
    ".cursor/"  # All documentation may contain emoji examples for illustration
    ".githooks/"  # Git hooks may contain emoji in comments
    "scripts/validate-emoji-ban"  # This script contains forbidden character lists for validation
)

# Function to check if file should be excluded
should_exclude_file() {
    local file="$1"

    # Check excluded files
    for excluded in "${EXCLUDED_FILES[@]}"; do
        if [[ "$file" == *"$excluded"* ]]; then
            return 0
        fi
    done

    # Check excluded patterns
    for pattern in "${EXCLUDED_PATTERNS[@]}"; do
        # Simple string matching for excluded directories/files
        if [[ "$file" == ${pattern}* ]]; then
            return 0
        fi
    done

    # Check excluded extensions
    for ext in "${EXCLUDED_EXTENSIONS[@]}"; do
        if [[ "$file" == *"$ext" ]]; then
            return 0
        fi
    done

    return 1
}

# Function to check if character is forbidden
is_forbidden_char() {
    local char="$1"
    local code

    # Get Unicode code point
    code=$(printf "%x" "'$char" 2>/dev/null || echo "")

    # Check if it's empty or not a valid Unicode
    if [[ -z "$code" ]]; then
        return 1
    fi

    # Convert to uppercase for comparison
    code=$(echo "$code" | tr 'a-z' 'A-Z')

    # Check specific forbidden characters
    for forbidden in "${FORBIDDEN_CHARS[@]}"; do
        if [[ "$char" == "$forbidden" ]]; then
            return 0
        fi
    done

    # Check Unicode ranges
    if [[ "$code" =~ ^1F[0-9A-F]{3}$ ]]; then
        # Emoji range 1F000-1FFFF
        return 0
    fi

    if [[ "$code" =~ ^2[0-9A-F]{3}$ ]]; then
        # Various symbol ranges (but allow Cyrillic in 0400-04FF)
        # Convert to decimal for range check
        decimal=$(printf "%d" "0x$code" 2>/dev/null || echo "0")
        if [[ $decimal -ge 1024 && $decimal -le 1279 ]]; then
            # Cyrillic range U+0400-U+04FF (1024-1279 decimal) - ALLOWED
            return 1
        fi
        return 0
    fi

    return 1
}

# Function to validate a single file
validate_file() {
    local file="$1"
    local found_errors=""

    if [[ ! -f "$file" ]]; then
        return 0
    fi

    if should_exclude_file "$file"; then
        return 0
    fi

    # Read file line by line
    local line_num=1
    while IFS= read -r line || [[ -n "$line" ]]; do
        # Check each character in the line
        local char_pos=1
        local line_copy="$line"

        while [[ -n "$line_copy" ]]; do
            local char="${line_copy:0:1}"
            line_copy="${line_copy:1}"

            if is_forbidden_char "$char"; then
                local code=$(printf "%x" "'$char" 2>/dev/null || echo "unknown")
                code=$(echo "$code" | tr 'a-z' 'A-Z')
                found_errors="${found_errors}Line $line_num, pos $char_pos: '$char' (U+$code) - ${line:0:50}...\n"
            fi

            ((char_pos++))
        done

        ((line_num++))
    done < "$file"

    if [[ -n "$found_errors" ]]; then
        echo -e "${RED}[FORBIDDEN] EMOJI/SPECIAL CHARACTERS FOUND in $file:${NC}"
        echo -e "$found_errors"
        return 1
    fi

    return 0
}

# Main validation function
main() {
    echo -e "${BLUE}[CHECK] Checking for forbidden emoji and special characters...${NC}"

    local files_to_check
    local has_errors=0

    # Get files to check
    if [[ $# -eq 0 ]]; then
        # Check all tracked files
        while IFS= read -r file; do
            files_to_check+=("$file")
        done < <(git ls-files)
    else
        # Check specified files
        files_to_check=("$@")
    fi

    local total_files=${#files_to_check[@]}
    local checked_files=0

    for file in "${files_to_check[@]}"; do
        ((checked_files++))
        echo -n "Checking $checked_files/$total_files: $file... "

        if validate_file "$file"; then
            echo -e "${GREEN}[OK]${NC}"
        else
            echo -e "${RED}[ERROR]${NC}"
            has_errors=1
        fi
    done

    echo -e "${BLUE}==================================================${NC}"

    if [[ $has_errors -eq 1 ]]; then
        echo -e "${RED}[CRITICAL] Forbidden emoji/special characters detected!${NC}"
        echo ""
        echo -e "${YELLOW}Why this matters:${NC}"
        echo "• Emojis break script execution on Windows"
        echo "• Special Unicode characters cause encoding issues"
        echo "• Cross-platform compatibility problems"
        echo ""
        echo -e "${YELLOW}Fix suggestions:${NC}"
        echo "• Replace emoji with ASCII text (:smile: instead of emoji)"
        echo "• Remove decorative Unicode symbols"
        echo "• Use plain text for all code comments"
        echo ""
        echo -e "${RED}COMMIT BLOCKED: Fix the issues and try again${NC}"
        exit 1
    else
        echo -e "${GREEN}[SUCCESS] No forbidden emoji/special characters found${NC}"
        exit 0
    fi
}

# Run main function with all arguments
main "$@"
