package com.necpgame.workqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EnumCatalogService {
    private static final String TASK_STATUS_GROUP = "task_status";
    private final JdbcTemplate jdbcTemplate;
    private final Map<String, UUID> cache = new ConcurrentHashMap<>();

    public UUID requireTaskStatus(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("status code required");
        }
        String normalized = code.trim().toLowerCase(Locale.ROOT);
        return cache.computeIfAbsent(normalized, this::loadTaskStatusId);
    }

    public List<UUID> requireTaskStatuses(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return List.of();
        }
        return codes.stream()
                .filter(code -> code != null && !code.isBlank())
                .map(this::requireTaskStatus)
                .distinct()
                .toList();
    }

    private UUID loadTaskStatusId(String code) {
        return fetchValueId(code, TASK_STATUS_GROUP)
                .orElseThrow(() -> new IllegalStateException("Unknown task status code: " + code));
    }

    private Optional<UUID> fetchValueId(String code, String groupCode) {
        String sql = """
                select v.id
                from enum_values v
                join enum_groups g on g.id = v.group_id
                where lower(v.code) = ? and g.code = ?
                limit 1
                """;
        return jdbcTemplate.query(sql, this::extractUuid, code, groupCode).stream().findFirst();
    }

    private UUID extractUuid(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getObject(1, UUID.class);
    }
}


