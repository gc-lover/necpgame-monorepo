package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("OrderCompletionResult")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OrderCompletionResult {

    @Schema(name = "order_id")
    @JsonProperty("order_id")
    private UUID orderId;

    @Schema(name = "payment_released")
    @JsonProperty("payment_released")
    private Integer paymentReleased;

    @Schema(name = "reputation_earned")
    @JsonProperty("reputation_earned")
    private Integer reputationEarned;

    @Valid
    @Schema(name = "bonuses")
    @JsonProperty("bonuses")
    private OrderCompletionResultBonuses bonuses;

    @Schema(name = "next_tier_unlocked")
    @JsonProperty("next_tier_unlocked")
    private Boolean nextTierUnlocked;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Integer getPaymentReleased() {
        return paymentReleased;
    }

    public void setPaymentReleased(Integer paymentReleased) {
        this.paymentReleased = paymentReleased;
    }

    public Integer getReputationEarned() {
        return reputationEarned;
    }

    public void setReputationEarned(Integer reputationEarned) {
        this.reputationEarned = reputationEarned;
    }

    public OrderCompletionResultBonuses getBonuses() {
        return bonuses;
    }

    public void setBonuses(OrderCompletionResultBonuses bonuses) {
        this.bonuses = bonuses;
    }

    public Boolean getNextTierUnlocked() {
        return nextTierUnlocked;
    }

    public void setNextTierUnlocked(Boolean nextTierUnlocked) {
        this.nextTierUnlocked = nextTierUnlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderCompletionResult that = (OrderCompletionResult) o;
        return Objects.equals(orderId, that.orderId)
            && Objects.equals(paymentReleased, that.paymentReleased)
            && Objects.equals(reputationEarned, that.reputationEarned)
            && Objects.equals(bonuses, that.bonuses)
            && Objects.equals(nextTierUnlocked, that.nextTierUnlocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, paymentReleased, reputationEarned, bonuses, nextTierUnlocked);
    }

    @Override
    public String toString() {
        return "OrderCompletionResult{" +
            "orderId=" + orderId +
            ", paymentReleased=" + paymentReleased +
            ", reputationEarned=" + reputationEarned +
            ", bonuses=" + bonuses +
            ", nextTierUnlocked=" + nextTierUnlocked +
            '}';
    }
}


