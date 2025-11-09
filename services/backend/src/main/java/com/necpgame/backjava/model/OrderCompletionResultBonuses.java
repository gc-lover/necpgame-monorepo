package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import java.util.Objects;

@JsonTypeName("OrderCompletionResult_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OrderCompletionResultBonuses {

    @Schema(name = "early_completion")
    @JsonProperty("early_completion")
    private Integer earlyCompletion;

    @Schema(name = "quality_bonus")
    @JsonProperty("quality_bonus")
    private Integer qualityBonus;

    public Integer getEarlyCompletion() {
        return earlyCompletion;
    }

    public void setEarlyCompletion(Integer earlyCompletion) {
        this.earlyCompletion = earlyCompletion;
    }

    public Integer getQualityBonus() {
        return qualityBonus;
    }

    public void setQualityBonus(Integer qualityBonus) {
        this.qualityBonus = qualityBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderCompletionResultBonuses that = (OrderCompletionResultBonuses) o;
        return Objects.equals(earlyCompletion, that.earlyCompletion)
            && Objects.equals(qualityBonus, that.qualityBonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(earlyCompletion, qualityBonus);
    }

    @Override
    public String toString() {
        return "OrderCompletionResultBonuses{" +
            "earlyCompletion=" + earlyCompletion +
            ", qualityBonus=" + qualityBonus +
            '}';
    }
}


