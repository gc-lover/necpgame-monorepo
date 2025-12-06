#!/bin/bash
# Check ogen migration status for all services

set -euo pipefail

echo "🔍 Checking ogen migration status..."
echo ""

SERVICES_DIR="services"
TOTAL=0
OGEN_COUNT=0
OAPI_COUNT=0
UNKNOWN_COUNT=0

OGEN_SERVICES=()
OAPI_SERVICES=()
UNKNOWN_SERVICES=()

for dir in "$SERVICES_DIR"/*-go/; do
    if [ ! -d "$dir" ]; then
        continue
    fi
    
    service=$(basename "$dir")
    makefile="$dir/Makefile"
    
    if [ ! -f "$makefile" ]; then
        continue
    fi
    
    TOTAL=$((TOTAL + 1))
    
    # Check if using ogen
    if grep -q "ogen" "$makefile" 2>/dev/null; then
        OGEN_COUNT=$((OGEN_COUNT + 1))
        OGEN_SERVICES+=("$service")
    elif grep -q "oapi-codegen" "$makefile" 2>/dev/null; then
        OAPI_COUNT=$((OAPI_COUNT + 1))
        OAPI_SERVICES+=("$service")
    else
        UNKNOWN_COUNT=$((UNKNOWN_COUNT + 1))
        UNKNOWN_SERVICES+=("$service")
    fi
done

echo "📊 Migration Statistics:"
echo "========================"
echo ""
echo "Total Services: $TOTAL"
echo "OK Migrated to ogen: $OGEN_COUNT ($(( OGEN_COUNT * 100 / TOTAL ))%)"
echo "❌ Still on oapi-codegen: $OAPI_COUNT ($(( OAPI_COUNT * 100 / TOTAL ))%)"
echo "WARNING  Unknown: $UNKNOWN_COUNT"
echo ""

if [ $OGEN_COUNT -gt 0 ]; then
    echo "OK Migrated Services ($OGEN_COUNT):"
    for service in "${OGEN_SERVICES[@]}"; do
        echo "  - $service"
    done
    echo ""
fi

if [ $OAPI_COUNT -gt 0 ]; then
    echo "❌ Services Remaining ($OAPI_COUNT):"
    
    # Categorize by priority
    echo ""
    echo "🔴 HIGH PRIORITY (Combat & Movement):"
    for service in "${OAPI_SERVICES[@]}"; do
        if [[ $service == combat-* ]] || [[ $service == movement-* ]] || [[ $service == world-* ]] || [[ $service == weapon-* ]] || [[ $service == projectile-* ]] || [[ $service == hacking-* ]] || [[ $service == gameplay-weapon-* ]]; then
            echo "  - $service"
        fi
    done
    
    echo ""
    echo "🟡 MEDIUM PRIORITY (Quest, Chat, Core):"
    for service in "${OAPI_SERVICES[@]}"; do
        if [[ $service == quest-* ]] || [[ $service == chat-* ]] || [[ $service == social-* ]] || [[ $service == achievement-* ]] || [[ $service == leaderboard-* ]] || [[ $service == league-* ]] || [[ $service == loot-* ]] || [[ $service == gameplay-service-* ]] || [[ $service == progression-* ]] || [[ $service == battle-pass-* ]] || [[ $service == seasonal-* ]] || [[ $service == companion-* ]] || [[ $service == cosmetic-* ]] || [[ $service == housing-* ]] || [[ $service == mail-* ]] || [[ $service == referral-* ]]; then
            echo "  - $service"
        fi
    done
    
    echo ""
    echo "🟢 LOW PRIORITY (Admin, Economy, Legacy):"
    for service in "${OAPI_SERVICES[@]}"; do
        if [[ $service == admin-* ]] || [[ $service == support-* ]] || [[ $service == maintenance-* ]] || [[ $service == feedback-* ]] || [[ $service == clan-* ]] || [[ $service == faction-* ]] || [[ $service == reset-* ]] || [[ $service == client-* ]] || [[ $service == realtime-* ]] || [[ $service == ws-* ]] || [[ $service == voice-* ]] || [[ $service == matchmaking-go ]] || [[ $service == character-engram-* ]] || [[ $service == stock-* ]] || [[ $service == economy-* ]] || [[ $service == trade-* ]]; then
            echo "  - $service"
        fi
    done
    echo ""
fi

if [ $UNKNOWN_COUNT -gt 0 ]; then
    echo "WARNING  Unknown Services ($UNKNOWN_COUNT):"
    for service in "${UNKNOWN_SERVICES[@]}"; do
        echo "  - $service"
    done
    echo ""
fi

echo "📈 Progress: $OGEN_COUNT/$TOTAL ($(( OGEN_COUNT * 100 / TOTAL ))%)"
echo ""
echo "🎯 Next Steps:"
if [ $OAPI_COUNT -gt 0 ]; then
    echo "  1. Review High Priority services (combat, movement, world)"
    echo "  2. See .cursor/ogen/README.md for reference"
    echo "  3. Track progress in GitHub Issues #1595-#1602"
    echo "  4. Main tracker: Issue #1603"
else
    echo "  🎉 All services migrated to ogen!"
fi
echo ""

echo "💡 Tip: Run 'make generate-api' in each service to regenerate with ogen"
echo ""

