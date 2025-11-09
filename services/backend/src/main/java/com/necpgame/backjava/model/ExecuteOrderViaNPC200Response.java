package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

@JsonTypeName("executeOrderViaNPC_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExecuteOrderViaNPC200Response {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "estimated_completion")
    @JsonProperty("estimated_completion")
    private OffsetDateTime estimatedCompletion;

    @Schema(name = "npc_efficiency")
    @JsonProperty("npc_efficiency")
    private Double npcEfficiency;

    public OffsetDateTime getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(OffsetDateTime estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public Double getNpcEfficiency() {
        return npcEfficiency;
    }

    public void setNpcEfficiency(Double npcEfficiency) {
        this.npcEfficiency = npcEfficiency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecuteOrderViaNPC200Response that = (ExecuteOrderViaNPC200Response) o;
        return Objects.equals(estimatedCompletion, that.estimatedCompletion)
            && Objects.equals(npcEfficiency, that.npcEfficiency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estimatedCompletion, npcEfficiency);
    }

    @Override
    public String toString() {
        return "ExecuteOrderViaNPC200Response{" +
            "estimatedCompletion=" + estimatedCompletion +
            ", npcEfficiency=" + npcEfficiency +
            '}';
    }
}


