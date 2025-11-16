package com.necpgame.workqueue.web;

import com.necpgame.workqueue.config.WorkqueueIngestionProperties;
import com.necpgame.workqueue.service.TaskIngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TaskIngestionControllerTest {

    @Test
    void schemaEndpointReturnsJson() {
        TaskIngestionService service = mock(TaskIngestionService.class);
        WorkqueueIngestionProperties props = new WorkqueueIngestionProperties();
        TaskIngestionController controller = new TaskIngestionController(service, props, new ClassPathResource("contracts/task-ingestion-request.schema.json"));

        var response = controller.schema();

        assertThat(response.getBody()).isNotBlank();
        assertThat(response.getBody()).contains("\"TaskIngestionRequest\"");
    }
}

