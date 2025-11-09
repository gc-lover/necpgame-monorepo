package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;

@JsonTypeName("getOrdersMarket_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetOrdersMarket200Response {

    @Schema(name = "active_orders_count")
    @JsonProperty("active_orders_count")
    private Integer activeOrdersCount;

    @Schema(name = "average_payment")
    @JsonProperty("average_payment")
    private Integer averagePayment;

    @Valid
    @Schema(name = "popular_types")
    @JsonProperty("popular_types")
    private List<PlayerOrderMarketTypeStats> popularTypes;

    @Schema(name = "high_demand_skills")
    @JsonProperty("high_demand_skills")
    private List<String> highDemandSkills;

    public Integer getActiveOrdersCount() {
        return activeOrdersCount;
    }

    public void setActiveOrdersCount(Integer activeOrdersCount) {
        this.activeOrdersCount = activeOrdersCount;
    }

    public Integer getAveragePayment() {
        return averagePayment;
    }

    public void setAveragePayment(Integer averagePayment) {
        this.averagePayment = averagePayment;
    }

    public List<PlayerOrderMarketTypeStats> getPopularTypes() {
        return popularTypes;
    }

    public void setPopularTypes(List<PlayerOrderMarketTypeStats> popularTypes) {
        this.popularTypes = popularTypes;
    }

    public List<String> getHighDemandSkills() {
        return highDemandSkills;
    }

    public void setHighDemandSkills(List<String> highDemandSkills) {
        this.highDemandSkills = highDemandSkills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetOrdersMarket200Response that = (GetOrdersMarket200Response) o;
        return Objects.equals(activeOrdersCount, that.activeOrdersCount)
            && Objects.equals(averagePayment, that.averagePayment)
            && Objects.equals(popularTypes, that.popularTypes)
            && Objects.equals(highDemandSkills, that.highDemandSkills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeOrdersCount, averagePayment, popularTypes, highDemandSkills);
    }

    @Override
    public String toString() {
        return "GetOrdersMarket200Response{" +
            "activeOrdersCount=" + activeOrdersCount +
            ", averagePayment=" + averagePayment +
            ", popularTypes=" + popularTypes +
            ", highDemandSkills=" + highDemandSkills +
            '}';
    }
}


