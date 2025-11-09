package com.necpgame.backjava.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;

@JsonTypeName("acceptPlayerOrder_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AcceptPlayerOrderRequest {

    private @Nullable UUID executorId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private @Nullable OffsetDateTime estimatedCompletionTime;

    public AcceptPlayerOrderRequest executorId(@Nullable UUID executorId) {
        this.executorId = executorId;
        return this;
    }

    @Valid
    @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("executor_id")
    public @Nullable UUID getExecutorId() {
        return executorId;
    }

    public void setExecutorId(@Nullable UUID executorId) {
        this.executorId = executorId;
    }

    public AcceptPlayerOrderRequest estimatedCompletionTime(@Nullable OffsetDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
        return this;
    }

    @Valid
    @Schema(name = "estimated_completion_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("estimated_completion_time")
    public @Nullable OffsetDateTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(@Nullable OffsetDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcceptPlayerOrderRequest that = (AcceptPlayerOrderRequest) o;
        return Objects.equals(executorId, that.executorId)
            && Objects.equals(estimatedCompletionTime, that.estimatedCompletionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executorId, estimatedCompletionTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcceptPlayerOrderRequest {\n");
        sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
        sb.append("    estimatedCompletionTime: ").append(toIndentedString(estimatedCompletionTime)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}


