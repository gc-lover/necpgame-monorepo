package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import java.util.Objects;

@JsonTypeName("PlayerOrderDetailed_allOf_escrow")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrderDetailedAllOfEscrow {

    @Schema(name = "amount_held")
    @JsonProperty("amount_held")
    private Integer amountHeld;

    @Schema(name = "release_condition")
    @JsonProperty("release_condition")
    private String releaseCondition;

    public Integer getAmountHeld() {
        return amountHeld;
    }

    public void setAmountHeld(Integer amountHeld) {
        this.amountHeld = amountHeld;
    }

    public String getReleaseCondition() {
        return releaseCondition;
    }

    public void setReleaseCondition(String releaseCondition) {
        this.releaseCondition = releaseCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerOrderDetailedAllOfEscrow that = (PlayerOrderDetailedAllOfEscrow) o;
        return Objects.equals(amountHeld, that.amountHeld)
            && Objects.equals(releaseCondition, that.releaseCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountHeld, releaseCondition);
    }

    @Override
    public String toString() {
        return "PlayerOrderDetailedAllOfEscrow{" +
            "amountHeld=" + amountHeld +
            ", releaseCondition='" + releaseCondition + '\'' +
            '}';
    }
}


