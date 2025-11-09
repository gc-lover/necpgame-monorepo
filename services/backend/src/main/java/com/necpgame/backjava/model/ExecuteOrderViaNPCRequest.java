package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("executeOrderViaNPC_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExecuteOrderViaNPCRequest {

    @Schema(name = "executor_id")
    @JsonProperty("executor_id")
    private UUID executorId;

    @Schema(name = "hired_npc_id")
    @JsonProperty("hired_npc_id")
    private UUID hiredNpcId;

    public UUID getExecutorId() {
        return executorId;
    }

    public void setExecutorId(UUID executorId) {
        this.executorId = executorId;
    }

    public UUID getHiredNpcId() {
        return hiredNpcId;
    }

    public void setHiredNpcId(UUID hiredNpcId) {
        this.hiredNpcId = hiredNpcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecuteOrderViaNPCRequest that = (ExecuteOrderViaNPCRequest) o;
        return Objects.equals(executorId, that.executorId)
            && Objects.equals(hiredNpcId, that.hiredNpcId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executorId, hiredNpcId);
    }

    @Override
    public String toString() {
        return "ExecuteOrderViaNPCRequest{" +
            "executorId=" + executorId +
            ", hiredNpcId=" + hiredNpcId +
            '}';
    }
}


