# Optimized Dockerfile for NECPGAME Performance Monitor
# Issue: Performance monitoring system

FROM python:3.11-slim

# Performance: Install only essential packages
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Security: Create non-root user
RUN groupadd -r monitor && useradd -r -g monitor monitor

# Performance: Set working directory
WORKDIR /app

# Dependencies: Copy requirements first for better caching
COPY requirements-monitor.txt .

# Performance: Install Python dependencies
RUN pip install --no-cache-dir -r requirements-monitor.txt

# Security: Copy application with proper ownership
COPY --chown=monitor:monitor scripts/ ./scripts/
COPY --chown=monitor:monitor infrastructure/docker/monitor-entrypoint.sh ./entrypoint.sh

# Security: Make entrypoint executable
RUN chmod +x ./entrypoint.sh

# Security: Don't run as root
USER monitor

# Health check
HEALTHCHECK --interval=60s --timeout=10s --start-period=30s --retries=3 \
    CMD python -c "import psutil; print('Monitor healthy')" || exit 1

# Labels
LABEL org.opencontainers.image.title="NECPGAME Performance Monitor" \
      org.opencontainers.image.description="Performance monitoring service for NECPGAME" \
      org.opencontainers.image.vendor="NECPGAME"

# Entrypoint
ENTRYPOINT ["./entrypoint.sh"]
CMD ["python", "scripts/performance_monitor.py"]
