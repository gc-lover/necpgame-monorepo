package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import java.util.Objects;

@JsonTypeName("PlayerOrderMarketTypeStats")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrderMarketTypeStats {

    @Schema(name = "type")
    @JsonProperty("type")
    private String type;

    @Schema(name = "count")
    @JsonProperty("count")
    private Long count;

    @Schema(name = "average_payment")
    @JsonProperty("average_payment")
    private Double averagePayment;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getAveragePayment() {
        return averagePayment;
    }

    public void setAveragePayment(Double averagePayment) {
        this.averagePayment = averagePayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerOrderMarketTypeStats that = (PlayerOrderMarketTypeStats) o;
        return Objects.equals(type, that.type)
            && Objects.equals(count, that.count)
            && Objects.equals(averagePayment, that.averagePayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, count, averagePayment);
    }

    @Override
    public String toString() {
        return "PlayerOrderMarketTypeStats{" +
            "type='" + type + '\'' +
            ", count=" + count +
            ", averagePayment=" + averagePayment +
            '}';
    }
}


