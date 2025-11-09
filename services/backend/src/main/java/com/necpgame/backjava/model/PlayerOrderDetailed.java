package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonTypeName("PlayerOrderDetailed")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrderDetailed extends PlayerOrder {

    @Schema(name = "requirements")
    @JsonProperty("requirements")
    private Map<String, Object> requirements;

    @Schema(name = "deliverables", description = "Что нужно предоставить")
    @JsonProperty("deliverables")
    private Map<String, Object> deliverables;

    @Valid
    @Schema(name = "escrow")
    @JsonProperty("escrow")
    private PlayerOrderDetailedAllOfEscrow escrow;

    @Valid
    @Schema(name = "reviews")
    @JsonProperty("reviews")
    private List<PlayerOrderDetailedAllOfReviews> reviews;

    public Map<String, Object> getRequirements() {
        return requirements;
    }

    public void setRequirements(Map<String, Object> requirements) {
        this.requirements = requirements;
    }

    public Map<String, Object> getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(Map<String, Object> deliverables) {
        this.deliverables = deliverables;
    }

    public PlayerOrderDetailedAllOfEscrow getEscrow() {
        return escrow;
    }

    public void setEscrow(PlayerOrderDetailedAllOfEscrow escrow) {
        this.escrow = escrow;
    }

    public List<PlayerOrderDetailedAllOfReviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<PlayerOrderDetailedAllOfReviews> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerOrderDetailed that)) {
            return false;
        }
        return super.equals(o)
            && Objects.equals(requirements, that.requirements)
            && Objects.equals(deliverables, that.deliverables)
            && Objects.equals(escrow, that.escrow)
            && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requirements, deliverables, escrow, reviews);
    }

    @Override
    public String toString() {
        return "PlayerOrderDetailed{" +
            "requirements=" + requirements +
            ", deliverables=" + deliverables +
            ", escrow=" + escrow +
            ", reviews=" + reviews +
            "} " + super.toString();
    }
}


