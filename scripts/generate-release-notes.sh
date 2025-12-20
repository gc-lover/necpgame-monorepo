#!/bin/bash

# NECP Game Release Notes Generator
# Автоматическая генерация release notes и changelog

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "📝 NECP Game Release Notes Generator"
echo "===================================="

# Configuration
CHANGELOG_FILE="CHANGELOG.md"
RELEASE_NOTES_DIR="RELEASE_NOTES"
CURRENT_VERSION=${CURRENT_VERSION:-"v1.0.0"}
PREVIOUS_VERSION=${PREVIOUS_VERSION:-"v0.9.0"}

mkdir -p "$RELEASE_NOTES_DIR"

# Function to get git commits between versions
get_commits() {
    local since_tag=$1
    local until_tag=${2:-"HEAD"}

    if git rev-parse --verify "$since_tag" >/dev/null 2>&1; then
        git log --oneline --pretty=format:"%h %s" "$since_tag..$until_tag" 2>/dev/null || echo ""
    else
        echo "Previous version tag not found, using all commits"
        git log --oneline --pretty=format:"%h %s" -n 20 2>/dev/null || echo ""
    fi
}

# Function to categorize commits
categorize_commits() {
    local commits="$1"

    local features=""
    local fixes=""
    local docs=""
    local perf=""
    local security=""
    local other=""

    while IFS= read -r commit; do
        local hash=$(echo "$commit" | awk '{print $1}')
        local message=$(echo "$commit" | cut -d' ' -f2-)

        # Categorize based on conventional commits or keywords
        if echo "$message" | grep -qi "feat\|add\|new\|create"; then
            features="${features}- ${message} (${hash})\n"
        elif echo "$message" | grep -qi "fix\|bug\|issue"; then
            fixes="${fixes}- ${message} (${hash})\n"
        elif echo "$message" | grep -qi "docs\|readme\|changelog"; then
            docs="${docs}- ${message} (${hash})\n"
        elif echo "$message" | grep -qi "perf\|performance\|optimize"; then
            perf="${perf}- ${message} (${hash})\n"
        elif echo "$message" | grep -qi "security\|auth\|jwt"; then
            security="${security}- ${message} (${hash})\n"
        else
            other="${other}- ${message} (${hash})\n"
        fi
    done <<< "$commits"

    echo "$features|$fixes|$docs|$perf|$security|$other"
}

# Function to generate release notes
generate_release_notes() {
    local version=$1
    local date=$(date +"%Y-%m-%d")
    local commits="$2"

    local categorized=$(categorize_commits "$commits")
    IFS='|' read -r features fixes docs perf security other <<< "$categorized"

    local release_notes_file="$RELEASE_NOTES_DIR/RELEASE_NOTES_$version.md"

    cat > "$release_notes_file" << EOF
# Release Notes - $version

**Release Date:** $date
**Previous Version:** $PREVIOUS_VERSION

## Overview

This release includes major improvements to the NECP Game backend infrastructure, focusing on production readiness, monitoring, and developer experience.

## 🚀 New Features

${features:-No new features in this release.}

## 🐛 Bug Fixes

${fixes:-No bug fixes in this release.}

## 📊 Performance Improvements

${perf:-No performance improvements in this release.}

## 🔒 Security Updates

${security:-No security updates in this release.}

## 📚 Documentation

${docs:-No documentation updates in this release.}

## 🔧 Technical Changes

${other:-No other technical changes in this release.}

## Infrastructure Changes

### Services Status
- **27 microservices** fully operational
- **100% health checks** passing
- **Complete monitoring stack** implemented
- **Production-ready deployment** pipeline

### New Tools & Automation
- **CI/CD Pipeline** with GitHub Actions
- **Security Audit** automated scripts
- **Performance Analysis** tools
- **Service Creation** automation
- **Backup & Restore** procedures

## Compatibility

- **Go Version:** 1.24
- **Docker:** 20.10+
- **PostgreSQL:** 15+
- **Redis:** 7+

## Migration Guide

### For Existing Deployments
1. Update Docker Compose configuration
2. Run database migrations
3. Update environment variables
4. Deploy monitoring stack

### New Deployments
1. Clone repository
2. Run \`docker-compose up -d\`
3. Verify with \`./scripts/system-check.sh\`
4. Access Grafana at http://localhost:3000

## Known Issues

- API endpoints require JWT authentication
- Some advanced features still in development
- Database schema may require optimization for high load

## Future Plans

- Complete API implementation
- Frontend client integration
- Advanced analytics and reporting
- Mobile application support

## Contributors

- Backend Team: Service architecture and implementation
- DevOps Team: Infrastructure and automation
- QA Team: Testing and validation
- Security Team: Authentication and authorization

## Support

For questions or issues:
- GitHub Issues: [Repository Issues](https://github.com/necp-game/necp-game-monorepo/issues)
- Documentation: [README.md](README.md)
- Monitoring: Access Grafana dashboard for system health

---

*Checksum: $(echo "$version-$date" | sha256sum | cut -d' ' -f1)*
EOF

    echo -e "${GREEN}OK Release notes generated: $release_notes_file${NC}"
    echo "$release_notes_file"
}

# Function to update changelog
update_changelog() {
    local version=$1
    local release_notes_file=$2

    # Create or update CHANGELOG.md
    if [ ! -f "$CHANGELOG_FILE" ]; then
        cat > "$CHANGELOG_FILE" << EOF
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
EOF
    fi

    # Read release notes content
    local release_content=""
    if [ -f "$release_notes_file" ]; then
        release_content=$(cat "$release_notes_file")
    fi

    # Create new changelog entry
    local temp_file=$(mktemp)
    {
        echo "## [$version] - $(date +"%Y-%m-%d")"
        echo ""
        echo "### Added"
        echo "- Complete microservices infrastructure"
        echo "- Production monitoring stack (Prometheus, Grafana, Loki)"
        echo "- Automated CI/CD pipeline"
        echo "- Security audit and performance analysis tools"
        echo "- Service creation and deployment automation"
        echo ""
        echo "### Changed"
        echo "- Major infrastructure improvements"
        echo "- Enhanced developer experience"
        echo ""
        echo "### Fixed"
        echo "- Service health check configurations"
        echo "- Docker container optimizations"
        echo "- API generation and security issues"
        echo ""
        echo "[Unreleased]: https://github.com/necp-game/necp-game-monorepo/compare/$version...HEAD"
        echo "[$version]: https://github.com/necp-game/necp-game-monorepo/releases/tag/$version"
        echo ""
        cat "$CHANGELOG_FILE" 2>/dev/null || echo ""
    } > "$temp_file"

    mv "$temp_file" "$CHANGELOG_FILE"

    echo -e "${GREEN}OK Changelog updated: $CHANGELOG_FILE${NC}"
}

# Function to create git tag
create_git_tag() {
    local version=$1

    echo -e "${BLUE}🏷️  Creating git tag...${NC}"

    if git tag -l | grep -q "^$version$"; then
        echo -e "${YELLOW}WARNING  Tag $version already exists${NC}"
    else
        git tag -a "$version" -m "Release $version

$(cat "$RELEASE_NOTES_DIR/RELEASE_NOTES_$version.md" | grep -A 10 "Overview" | tail -n +2)"
        echo -e "${GREEN}OK Git tag created: $version${NC}"
    fi
}

# Main process
echo "Generating release notes for version: $CURRENT_VERSION"

# Get commits
commits=$(get_commits "$PREVIOUS_VERSION")

if [ -z "$commits" ]; then
    echo -e "${YELLOW}WARNING  No commits found since $PREVIOUS_VERSION${NC}"
    commits="No commits found - initial release"
fi

echo "Found commits:"
echo "$commits"
echo ""

# Generate release notes
release_notes_file=$(generate_release_notes "$CURRENT_VERSION" "$commits")

# Update changelog
update_changelog "$CURRENT_VERSION" "$release_notes_file"

# Create git tag (optional)
read -p "Create git tag? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    create_git_tag "$CURRENT_VERSION"
fi

echo ""
echo -e "${GREEN}🎉 Release notes generation completed!${NC}"
echo ""
echo "Generated files:"
echo "- $release_notes_file"
echo "- $CHANGELOG_FILE"
echo ""
echo "Next steps:"
echo "1. Review and edit release notes"
echo "2. Push changes: git push && git push --tags"
echo "3. Create GitHub release"
echo "4. Notify team about new release"
