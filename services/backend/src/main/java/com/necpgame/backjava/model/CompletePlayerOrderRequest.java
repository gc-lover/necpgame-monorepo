package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("completePlayerOrder_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CompletePlayerOrderRequest {

    @NotNull
    @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("executor_id")
    private UUID executorId;

    @Schema(name = "completion_proof", description = "Доказательство выполнения")
    @JsonProperty("completion_proof")
    private Map<String, Object> completionProof;

    public UUID getExecutorId() {
        return executorId;
    }

    public void setExecutorId(UUID executorId) {
        this.executorId = executorId;
    }

    public Map<String, Object> getCompletionProof() {
        return completionProof;
    }

    public void setCompletionProof(Map<String, Object> completionProof) {
        this.completionProof = completionProof;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompletePlayerOrderRequest that = (CompletePlayerOrderRequest) o;
        return Objects.equals(executorId, that.executorId)
            && Objects.equals(completionProof, that.completionProof);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executorId, completionProof);
    }

    @Override
    public String toString() {
        return "CompletePlayerOrderRequest{" +
            "executorId=" + executorId +
            ", completionProof=" + completionProof +
            '}';
    }
}


