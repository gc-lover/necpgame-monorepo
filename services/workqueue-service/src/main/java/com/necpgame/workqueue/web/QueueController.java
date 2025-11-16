package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.QueueQueryService;
import com.necpgame.workqueue.web.dto.QueueItemSummaryDto;
import com.necpgame.workqueue.web.dto.QueueListItemDto;
import com.necpgame.workqueue.web.mapper.QueueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
public class QueueController {
    private final QueueQueryService queueQueryService;
    private final QueueMapper queueMapper;

    @GetMapping("/{segment}")
    public List<QueueListItemDto> listQueues(@PathVariable String segment) {
        return queueQueryService.findQueuesBySegment(segment).stream().map(queueMapper::toQueueDto).toList();
    }

    @GetMapping("/{segment}/items")
    public List<QueueItemSummaryDto> listQueueItems(@PathVariable String segment, @RequestParam(value = "status", required = false) List<String> statuses, @RequestParam(value = "assignedTo", required = false) UUID assignedTo) {
        Set<String> normalized = statuses == null ? Set.of() : statuses.stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        return queueQueryService.findItems(segment, normalized, assignedTo).stream().map(queueMapper::toSummary).toList();
    }
}


