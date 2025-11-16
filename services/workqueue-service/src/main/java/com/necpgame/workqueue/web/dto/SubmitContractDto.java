package com.necpgame.workqueue.web.dto;

import java.util.List;
import java.util.Map;

public record SubmitContractDto(
        String method,
        String path,
        String contentType,
        List<String> requiredParts,
        Map<String, String> arrayEncoding,
        Map<String, String> requiredHeaders,
        String exampleCurl,
        String responseExample,
        String payloadExample
) {
}


